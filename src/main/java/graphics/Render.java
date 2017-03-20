package main.java.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.java.constants.CellSize;
import main.java.constants.CellState;
import main.java.constants.GameOutcome;
import main.java.constants.ScreenSize;
import main.java.event.Event;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.arguments.GameDisplayInvalidatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.LocalPlayerDespawnEventArgs;
import main.java.event.arguments.LocalPlayerSpawnEventArgs;
import main.java.event.arguments.SingleplayerGameStartingEventArgs;
import main.java.event.listener.EntityRemovingListener;
import main.java.event.listener.GameDisplayInvalidatedListener;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.LocalPlayerDespawnListener;
import main.java.event.listener.LocalPlayerSpawnListener;
import main.java.event.listener.PlayerLeavingGameListener;
import main.java.event.listener.SingleplayerGameStartingListener;
import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.*;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.ui.GameUI;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements GameDisplayInvalidatedListener, GameEndedListener,
		LocalPlayerSpawnListener, LocalPlayerDespawnListener, EntityRemovingListener {
	private Pane root;
	private Timeline timeLine;
	private ControlledPlayer controlledPlayer;
	private int localPlayerID;
	private GameUI gameUI;
	private Game game;
	private GameLogic gameLogic;
	private InGameScreens inGameScreens;
    private Node[][] worldNodes;
	private HashMap<Integer, Visualisation> allEntities;
	private HashMap<Integer, RotateTransition> rotations;
	private HashMap<Integer, TranslateTransition> transitions;
	private HashMap<Integer, Node> shieldsActivated;
	private Node playerRespawnWindow;
	private Node gameOverWindow;
	private Event<PlayerLeavingGameListener, Object> onPlayerLeavingGame;
	private Set<Integer> removedEntityIDs;
	private Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> onStartingSingleplayerGame;

	/**
	 * Initialise new visualisation of the map
	 *
	 * @param gameUI
	 * @param game
	 */
	public Render(final GameUI gameUI, final Game game, final GameLogic gameLogic) {
		this.gameUI = gameUI;
		this.game = game;
		this.gameLogic = gameLogic;
        this.inGameScreens = new InGameScreens(game);

		this.gameLogic.getOnGameDisplayInvalidated().addListener(this);
		this.gameLogic.getOnGameEnded().addOneTimeListener(this);
		this.gameLogic.getOnLocalPlayerSpawn().addListener(this);
		this.gameLogic.getOnLocalPlayerDespawn().addListener(this);
		this.game.getWorld().getOnEntityRemovingEvent().addListener(this);

		this.transitions = new HashMap<>();
		this.rotations = new HashMap<>();
		this.allEntities = new HashMap<>();
		this.shieldsActivated = new HashMap<>();
        this.removedEntityIDs = new HashSet<>();
		
		this.onPlayerLeavingGame = new Event<>((l, a) -> l.onPlayerLeavingGame());
		this.onStartingSingleplayerGame = new Event<>((l, s) -> l.onSingleplayerGameStarting(s));
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene setupWorld() {
		int size = game.getWorld().getMap().getMapSize();
		worldNodes = new Node[size][size];

		root = new Pane();
		root.setStyle("-fx-background-color: black");

		final Scene scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		redrawWorld();
		gameLogic.readyToStart();

		return scene;
	}

	/**
	 * Redraw the map
	 */
    void redrawWorld() {
		PositionVisualisation.initScreenDimensions();

    	redrawCells();
    	
    	synchronized (removedEntityIDs) {
			for(int id : removedEntityIDs) {
				removeEntityFromStage(id);
			}
			
			removedEntityIDs.clear();
		}

		for (final Player player : game.getWorld().getPlayers()) {
    	    PacmanVisualisation pacmanVisualisation = new PacmanVisualisation(player);
		    Node nextNode = pacmanVisualisation.getNode();
		    
		    if(!allEntities.containsKey(player.getID()))
		    	setupPlayerAnimation(player);

		    if(player.getShield() > 0  && !shieldsActivated.containsKey(player.getID())){
		        shieldsActivated.put(player.getID(), nextNode);
                shieldVisulisation(player, pacmanVisualisation, nextNode, allEntities.get(player.getID()).getNode(), player.getID());
            } else if (player.getShield() <= 0 && shieldsActivated.containsKey(player.getID())){
                shieldVisulisation(player, pacmanVisualisation, nextNode, shieldsActivated.get(player.getID()), player.getID());
                shieldsActivated.remove(player.getID());
            }

            if(player.getLaserFired()){
                laserVisualisation(player);
            }

            transitions.get(player.getID()).setToY(nextNode.getTranslateY());
            transitions.get(player.getID()).setToX(nextNode.getTranslateX());
            rotations.get(player.getID()).setToAngle(nextNode.getRotate());

            rotations.get(player.getID()).play();
            transitions.get(player.getID()).play();
        }

		for (final Ghost ghost : game.getWorld().getGhosts()) {
		    ImageView nextNode = new GhostVisualisation(ghost.getPosition()).getNode();
		    
		    if(!allEntities.containsKey(ghost.getID())) {
		    	setupGhostAnimation(ghost);
		    }

		    transitions.get(ghost.getID()).setToY(nextNode.getTranslateY());
            transitions.get(ghost.getID()).setToX(nextNode.getTranslateX());

            transitions.get(ghost.getID()).play();
		}

		for (final Spawner spawner : game.getWorld().getEntities(Spawner.class)) {
		    if(!allEntities.containsKey(spawner.getID())) {
		    	setupSpawnerAnimation(spawner);
		    }
			SpawnerVisualisation vis = (SpawnerVisualisation) allEntities.get(spawner.getID());
		    
		    vis.setNumber(spawner.getTimeRemaining());
		}

		root.requestFocus();
	}

    /**
	 *
	 * Click listener for moving the player
	 */
	public void addClickListener() {
		root.setOnKeyPressed(event -> {
			if(controlledPlayer != null) {
				// ie. the player isn't dead
				
				if (event.getCode() == KeyCode.UP) {
					controlledPlayer.moveUp();
					redrawWorld();
				} else if (event.getCode() == KeyCode.DOWN) {
					controlledPlayer.moveDown();
					redrawWorld();
				} else if (event.getCode() == KeyCode.LEFT) {
					controlledPlayer.moveLeft();
					redrawWorld();
				} else if (event.getCode() == KeyCode.RIGHT) {
					controlledPlayer.moveRight();
					redrawWorld();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					timeLine.pause();
					root.getChildren().add(this.inGameScreens.pauseGameScreen());
					pauseClickListener();
				} else if (event.getCode() == KeyCode.Q) {
                    controlledPlayer.getSkillSet().activateQ();
                    redrawWorld();
				} else if (event.getCode() == KeyCode.W) {
					controlledPlayer.getSkillSet().activateW();
					redrawWorld();
				}
			} 
		});
	}

	public void startTimeline() {
		timeLine = new Timeline(new KeyFrame(Duration.millis(GameLogic.GAME_STEP_DURATION), event -> {
            gameLogic.gameStep(GameLogic.GAME_STEP_DURATION);
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	//Override methods

	@Override
	public void onGameDisplayInvalidated(final GameDisplayInvalidatedEventArgs args) {
		if (!game.hasEnded()) {
			Platform.runLater(this::redrawWorld);
		}
	}

	@Override
	public void onGameEnded(final GameEndedEventArgs args) {
		Platform.runLater(() -> {
			gameEnded(args.getOutcome());
		});
	}

	@Override
	public void onLocalPlayerDespawn(LocalPlayerDespawnEventArgs args) {
		this.controlledPlayer = null;
		Platform.runLater(() -> {
			playerDied(args.getMessage(), args.canRespawn());
		});
	}

	@Override
	public void onLocalPlayerSpawn(LocalPlayerSpawnEventArgs args) {
		this.controlledPlayer = args.getPlayer();
		this.localPlayerID = this.controlledPlayer.getID();
		Platform.runLater(this::playerRespawn);
	}

	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Platform.runLater(() -> {
			synchronized (removedEntityIDs) {
				removedEntityIDs.add(args.getEntityID());
			}
		});
	}

	//Getter on the events
	
	public Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> getOnStartingSingleplayerGame() {
		return onStartingSingleplayerGame;
	}
	
	public Event<PlayerLeavingGameListener, Object> getOnPlayerLeavingGame() {
		return onPlayerLeavingGame;
	}

	//Private methods

    private void leaveGame() {
        onPlayerLeavingGame.fire(null);
        gameUI.switchToMenu();
    }

    private void gameEnded(final GameOutcome gameOutcome) {
        clearWindows();
        timeLine.stop();

        Node playerNode = root.getChildren().get(root.getChildren().indexOf(allEntities.get(0).getNode()));
        if(playerNode != null) {
	        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), playerNode);
	        fadeTransition.setFromValue(1.0);
	        fadeTransition.setToValue(0.0);
	        fadeTransition.setOnFinished(e2 -> {
	            displayGameEndedScreen(gameOutcome);
	        });
	        fadeTransition.play();
        } else {
        	displayGameEndedScreen(gameOutcome);
        }
    }
    
    private void displayGameEndedScreen(final GameOutcome gameOutcome) {
    	root.getChildren().add(this.inGameScreens.endGameScreen(localPlayerID, gameOutcome));
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                getOnStartingSingleplayerGame().fire(
                        new SingleplayerGameStartingEventArgs(new GameSettings(), this.gameUI.getName()));
            } else if (e.getCode() == KeyCode.ESCAPE) {
                leaveGame();
            }
        });
    }

    private void playerDied(String message, boolean canRejoin) {
        clearWindows();
        playerRespawnWindow = inGameScreens.getPlayerRespawnWindow(message, canRejoin);
        root.getChildren().add(playerRespawnWindow);
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                gameLogic.readyToStart();
            }
        });
    }

    private void setupGhostAnimation(final Ghost ghost) {
        GhostVisualisation ghostVis = new GhostVisualisation(ghost.getPosition());
        Node ghostNode = ghostVis.getNode();
        TranslateTransition transitionGhost = new TranslateTransition(Duration.millis(140), ghostNode);

        transitions.put(ghost.getID(), transitionGhost);
        allEntities.put(ghost.getID(), ghostVis);
        root.getChildren().add(ghostNode);
    }

    private void setupSpawnerAnimation(final Spawner spawner) {
        SpawnerVisualisation spawnerVis = new SpawnerVisualisation(spawner);
        Node spawnerNode = spawnerVis.getNode();
        spawnerVis.setNumber(spawner.getTimeRemaining());
        allEntities.put(spawner.getID(), spawnerVis);
        root.getChildren().add(spawnerNode);

        if(spawner.getColor() == SpawnerColor.GREEN) {
            clearWindows();
        }
    }

    private void setupPlayerAnimation(final Player player) {
        PacmanVisualisation playerVis = new PacmanVisualisation(player);
        Node playerNode = playerVis.getNode();
        TranslateTransition transitionPlayer = new TranslateTransition(Duration.millis(140), playerNode);
        RotateTransition rotatePlayer = new RotateTransition(Duration.millis(30), playerNode);

        transitions.put(player.getID(), transitionPlayer);
        rotations.put(player.getID(), rotatePlayer);

        allEntities.put(player.getID(), playerVis);
        root.getChildren().add(playerNode);
    }

    private void removeEntityFromStage(int entityID) {
        if(allEntities.containsKey(entityID)) {
            root.getChildren().remove(allEntities.remove(entityID).getNode());

            if(transitions.containsKey(entityID)) {
                root.getChildren().remove(transitions.remove(entityID));
            }

            if(rotations.containsKey(entityID)) {
                root.getChildren().remove(rotations.remove(entityID));
            }
        }
    }

    private void playerRespawn() {
        clearWindows();
        this.addClickListener();
    }

    private void clearWindows() {
        if(root.getChildren().contains(gameOverWindow))
            root.getChildren().remove(gameOverWindow);
        if(root.getChildren().contains(playerRespawnWindow))
            root.getChildren().remove(playerRespawnWindow);
    }

    private void redrawCells() {
        final Cell[][] cells = game.getWorld().getMap().getCells();
        int rows = game.getWorld().getMap().getMapSize(), columns = rows;

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                boolean firstDraw = worldNodes[row][column] == null;
                if(firstDraw || cells[row][column].needsRedraw()) {
                    if(!firstDraw) root.getChildren().remove(worldNodes[row][column]);
                    Node cv = new CellVisualisation(cells[row][column]).getNode();
                    worldNodes[row][column] = cv;
                    root.getChildren().add(cv);
                    cv.toBack();
                    cells[row][column].clearNeedsRedrawFlag();
                }
            }
        }
    }

    private void pauseClickListener() {
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                root.getChildren().remove(root.getChildren().size()-1);
                timeLine.play();
                addClickListener();
            } else if (event.getCode() == KeyCode.Q) {
                gameUI.switchToMenu();
            }
        });
    }

    private void laserVisualisation(Player player) {
        Node shout = new CellVisualisation(new Cell(CellState.LASER, player.getPosition())).getNode();
        shout.toFront();

        Node shoutStopNode;
        Position shoutStopPosition;
        switch ((int)player.getAngle()){
            case 90:
                shoutStopPosition = new Position(CellSize.Rows, player.getPosition().getColumn());
                shoutStopNode = new CellVisualisation(new Cell(CellState.LASER, shoutStopPosition)).getNode();
                shout.setRotate(90);
                break;
            case 0:
                shoutStopPosition = new Position(player.getPosition().getRow(), CellSize.Columns);
                shoutStopNode = new CellVisualisation(new Cell(CellState.LASER, shoutStopPosition)).getNode();
                break;
            case -90:
                shoutStopPosition = new Position(0, player.getPosition().getColumn());
                shoutStopNode = new CellVisualisation(new Cell(CellState.LASER, shoutStopPosition)).getNode();
                shout.setRotate(90);
                break;
            case 180:
                shoutStopPosition = new Position(player.getPosition().getRow(), 0);
                shoutStopNode = new CellVisualisation(new Cell(CellState.LASER, shoutStopPosition)).getNode();
                break;
            default:
                shoutStopPosition = new Position(0,0);
                shoutStopNode = new CellVisualisation(new Cell(CellState.LASER, shoutStopPosition)).getNode();
                break;

        }
        root.getChildren().add(shout);
        TranslateTransition shoutTransition = new TranslateTransition(Duration.millis(1000), shout);

        shoutTransition.setToX(shoutStopNode.getTranslateX());
        shoutTransition.setToY(shoutStopNode.getTranslateY());

        shoutTransition.setOnFinished(e -> {
            root.getChildren().remove(shout);
        });
        shoutTransition.play();
        player.setLaserFired(false);
    }

    private void shieldVisulisation(Player player, PacmanVisualisation pacmanVisualisation, Node nextNode, Node node, int id) {
        transitions.get(player.getID()).setNode(nextNode);
        rotations.get(player.getID()).setNode(nextNode);

        root.getChildren().remove(node);
        root.getChildren().add(nextNode);

        allEntities.remove(id);
        allEntities.put(id, pacmanVisualisation);
    }
}