package main.java.graphics;

import java.awt.*;
import java.awt.Label;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.java.constants.*;
import main.java.event.Event;
import main.java.event.arguments.*;
import main.java.event.listener.*;
import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.*;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.ui.GameUI;
import main.java.ui.Screen;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements GameDisplayInvalidatedListener, GameEndedListener, EntityAddedListener,
		LocalPlayerSpawnListener, LocalPlayerDespawnListener, EntityRemovingListener, PlayerCooldownChangedListener, PlayerLaserActivatedListener, PlayerShieldActivatedListener, PlayerShieldRemovedListener {
	private Pane root;
	private StackPane inventory;
	private BorderPane parentRoot;
    private ImageView shieldImage;
    private ImageView laserImage;
    private Timeline timeLine;
	private ControlledPlayer controlledPlayer;
	private int localPlayerID;
	private GameUI gameUI;
	private Game game;
	private GameLogic gameLogic;
	private InGameScreens inGameScreens;
    private Node[][] worldNodes;
	private HashMap<Integer, Visualisation> allEntities;
    private HashMap<Integer, Visualisation> playersEntities;
	private HashMap<Integer, RotateTransition> rotations;
	private HashMap<Integer, TranslateTransition> transitions;
	private HashMap<Integer, Node> shieldsActivated;
	private Node playerRespawnWindow;
	private Node gameOverWindow;
	private Event<PlayerLeavingGameListener, Object> onPlayerLeavingGame;
	private Set<Integer> addedEntityIDs, removedEntityIDs;
	private Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> onStartingSingleplayerGame;

	/**
	 * Initialise a new visualisation of the game
	 *
	 * @param gameUI, the GUI
	 * @param game, the actual game that has in itself the world and the players
     * @param gameLogic, the game logic
	 */
	public Render(final GameUI gameUI, final Game game, final GameLogic gameLogic) {
		this.gameUI = gameUI;
		this.game = game;
		this.gameLogic = gameLogic;
        this.inGameScreens = new InGameScreens();

		this.gameLogic.getOnGameDisplayInvalidated().addListener(this);
		this.gameLogic.getOnGameEnded().addOneTimeListener(this);
		this.gameLogic.getOnLocalPlayerSpawn().addListener(this);
		this.gameLogic.getOnLocalPlayerDespawn().addListener(this);
		this.game.getWorld().getOnEntityAddedEvent().addListener(this);
		
		this.game.getWorld().getOnEntityRemovingEvent().addListener(this);

		this.transitions = new HashMap<>();
		this.rotations = new HashMap<>();
		this.allEntities = new HashMap<>();
		this.playersEntities = new HashMap<>();
		this.shieldsActivated = new HashMap<>();
		this.addedEntityIDs = new HashSet<>();
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

		parentRoot = new BorderPane();
        parentRoot.setStyle("-fx-background-color: black");

        setupInventory();

		root = new Pane();
		root.setStyle("-fx-background-color: black");

        parentRoot.setCenter(root);
        parentRoot.setBottom(inventory);

		final Scene scene = new Scene(parentRoot, ScreenSize.Width, ScreenSize.Height+30);

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
    	
    	synchronized (addedEntityIDs) {
			for(int id : addedEntityIDs) {
				Entity e = game.getWorld().getEntity(id);
				if(e instanceof Player) {
					setupPlayerAnimation((Player) e);
				} else if(e instanceof Ghost) {
					setupGhostAnimation((Ghost) e);
				} else if(e instanceof Spawner) {
					setupSpawnerAnimation((Spawner) e);
				}
			}
			
			addedEntityIDs.clear();
		}
    	
    	synchronized (removedEntityIDs) {
			for(int id : removedEntityIDs) {
				removeEntityFromStage(id);
			}
			
			removedEntityIDs.clear();
		}

		for (final Player player : game.getWorld().getPlayers()) {
    	    PacmanVisualisation pacmanVisualisation = new PacmanVisualisation(player);
    	    
    	    if(pacmanVisualisation != null) {
	    	    Node nextNode = pacmanVisualisation.getNode();
	
	            transitions.get(player.getID()).setToY(nextNode.getTranslateY());
	            transitions.get(player.getID()).setToX(nextNode.getTranslateX());
	            rotations.get(player.getID()).setToAngle(nextNode.getRotate());
	
	            rotations.get(player.getID()).play();
	            transitions.get(player.getID()).play();
    	    }
        }

		for (final Ghost ghost : game.getWorld().getGhosts()) {
		    GhostVisualisation ghostVis = new GhostVisualisation(ghost.getPosition());

		    if(ghostVis != null) {
		    	Node nextNode = ghostVis.getNode();
			    transitions.get(ghost.getID()).setToY(nextNode.getTranslateY());
	            transitions.get(ghost.getID()).setToX(nextNode.getTranslateX());
	
	            transitions.get(ghost.getID()).play();
		    }
		}

		for (final Spawner spawner : game.getWorld().getEntities(Spawner.class)) {
			SpawnerVisualisation vis = (SpawnerVisualisation) allEntities.get(spawner.getID());
		    
			if(vis != null) {
				vis.setNumber(spawner.getTimeRemaining());
			}
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
	                gameUI.pausePlay();
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

    /**
     * Starting the timeline
     */
	public void startTimeline() {
		timeLine = new Timeline(new KeyFrame(Duration.millis(GameLogic.GAME_STEP_DURATION), event -> {
            gameLogic.gameStep(GameLogic.GAME_STEP_DURATION);
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	//Override methods

    /**
     * Invoked when the display needs to be redrawn
     * @param args, arguments that contain some logic
     */
	@Override
	public void onGameDisplayInvalidated(final GameDisplayInvalidatedEventArgs args) {
		if (!game.hasEnded()) {
			Platform.runLater(this::redrawWorld);
		}
	}

    /**
     * Invoked when the game ends
     * @param args, arguments containing the outcome
     */
	@Override
	public void onGameEnded(final GameEndedEventArgs args) {
		Platform.runLater(() -> {
			gameEnded(args.getOutcome());
		});
	}

    /**
     * Invoked when a local player has been despawn
     * @param args, arguments contaning the reason for despawning and whether it can respawn
     */
	@Override
	public void onLocalPlayerDespawn(LocalPlayerDespawnEventArgs args) {
		this.controlledPlayer = null;
		Platform.runLater(() -> {
			playerDied(args.getMessage(), args.canRespawn());
		});
	}

    /**
     * Invoked when a local player has been spawned
     * @param args, arguments containing the player
     */
	@Override
	public void onLocalPlayerSpawn(LocalPlayerSpawnEventArgs args) {
		this.controlledPlayer = args.getPlayer();

		this.localPlayerID = this.controlledPlayer.getID();
		Platform.runLater(this::playerRespawn);
	}

    /**
     * Invoked when a player has been removed
     * @param args, arguments containing the id of the player being removed
     */
	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Platform.runLater(() -> {
			synchronized (removedEntityIDs) {
				removedEntityIDs.add(args.getEntityID());
			}

			Entity e = game.getWorld().getEntity(args.getEntityID());
			
			if (e instanceof Player) {
				Player thiscontrolledPlayer = (Player)e;
				thiscontrolledPlayer.getSkillSet().getOnPlayerCooldownChanged().removeListener(this);
				thiscontrolledPlayer.getSkillSet().getOnPlayerLaserActivated().removeListener(this);
		        thiscontrolledPlayer.getSkillSet().getOnPlayerShieldActivated().removeListener(this);
		        thiscontrolledPlayer.getSkillSet().getOnPlayerShieldRemoved().removeListener(this);
			}
		});
	}

	//Getters on the events

    /**
     * Get the event for satring new game
     * @return Event
     */
	public Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> getOnStartingSingleplayerGame() {
		return onStartingSingleplayerGame;
	}

    /**
     * Get the event for player leaving a game
     * @return Event
     */
	public Event<PlayerLeavingGameListener, Object> getOnPlayerLeavingGame() {
		return onPlayerLeavingGame;
	}

	//Private methods

    private void leaveGame() {
        onPlayerLeavingGame.fire(null);
        gameUI.stopMusic();
        gameUI.switchToMenu();
    }

    private void gameEnded(final GameOutcome gameOutcome) {
        clearWindows();
        timeLine.stop();

        for (Visualisation player: playersEntities.values()) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), player.getNode());
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(e2 -> {
                displayGameEndedScreen(gameOutcome);
            });
            fadeTransition.play();
        }

        if (playersEntities.isEmpty())
            displayGameEndedScreen(gameOutcome);
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
        playersEntities.put(player.getID(), playerVis);
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
                gameUI.pausePlay();
            } else if (event.getCode() == KeyCode.Q) {
                leaveGame();
            } else if(event.getCode() == KeyCode.SPACE){
            	gameUI.switchToSettingsGame();
            }
        });
    }

    private void shieldVisulisation(Player player, PacmanVisualisation pacmanVisualisation, Node nextNode, Node node, int id) {
    	gameUI.playShield();
    	transitions.get(player.getID()).setNode(nextNode);
        rotations.get(player.getID()).setNode(nextNode);

        root.getChildren().remove(node);
        root.getChildren().add(nextNode);

        allEntities.remove(id);
        playersEntities.remove(id);
        allEntities.put(id, pacmanVisualisation);
        playersEntities.put(id, pacmanVisualisation);
    }

    private void setupInventory() {
        this.shieldImage = new ImageView("shield.png");
        this.laserImage = new ImageView("laser.png");

        inventory = new StackPane();
        inventory.setBackground(new Background(new BackgroundImage(
                new Image("inventory.jpg",ScreenSize.Width,30,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));
        inventory.setPrefSize(ScreenSize.Width, 30);

        StackPane shieldPane = new StackPane();
        StackPane laserPane = new StackPane();

        final javafx.scene.control.Label wLabel = new javafx.scene.control.Label("W - ");
        wLabel.setStyle(
                "-fx-text-fill: #565656; -fx-font: bold 20 \"serif\"; -fx-padding: 0 110 0 0; -fx-text-alignment: center");

        final javafx.scene.control.Label qLabel = new javafx.scene.control.Label("Q - ");
        qLabel.setStyle(
                "-fx-text-fill: #565656; -fx-font: bold 20 \"serif\"; -fx-padding: 0 0 0 50; -fx-text-alignment: center");

        shieldImage.setFitWidth(ScreenSize.Width / 30);
        shieldImage.setFitHeight(25);
        shieldPane.setStyle("-fx-padding: 0 50 0 0");
        shieldImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(0,1,0), 10, 0, 0, 0));
        shieldPane.getChildren().addAll(shieldImage);

        laserImage.setFitWidth(ScreenSize.Width / 30);
        laserImage.setFitHeight(25);
        laserPane.setStyle("-fx-padding: 0 0 0 110");
        laserImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(0,1,0), 10, 0, 0, 0));
        laserPane.getChildren().add(laserImage);

        StackPane.setAlignment(shieldPane, Pos.CENTER);
        StackPane.setAlignment(wLabel, Pos.CENTER);
        StackPane.setAlignment(laserPane, Pos.CENTER);
        StackPane.setAlignment(qLabel, Pos.CENTER);

        inventory.getChildren().addAll(wLabel, shieldPane, qLabel, laserPane);
    }

    @Override
    public void onPlayerCooldownChanged(PlayerCooldownChangedEventArgs args) {
        Platform.runLater(() -> {
            if(args.getSlot() == 'q') { //laser
                if (args.getCooldownLevel() < 20) {
                    laserImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(1, 0, 0), 10, 0, 0, 0));
                } else {
                    laserImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(0, 1, 0), 10, 0, 0, 0));
                }
            } else if(args.getSlot() == 'w') { //shield
                if (args.getCooldownLevel() < 40) {
                    shieldImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(1, 0, 0), 10, 0, 0, 0));
                } else {
                    shieldImage.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(0, 1, 0), 10, 0, 0, 0));
                }
            }
        });
    }

    @Override
    public void onPlayerLaserActivated(PlayerLaserActivatedEventArgs args) {
        Platform.runLater(() -> {
            Player player = args.getPlayer();
            double direction = args.getDirection();
            int cooldown = args.getCoolDown();

            if(cooldown == 20) {

                Node shout = new CellVisualisation(new Cell(CellState.LASER, player.getPosition())).getNode();
                shout.toFront();

                Node shoutStopNode;
                Position shoutStopPosition;
                switch ((int) direction) {
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
                        shoutStopPosition = new Position(0, 0);
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
                gameUI.fireLaser();
                player.setLaserFired(false);
            }
        });
    }

    @Override
    public void onPlayerShieldActivated(PlayerShieldActivatedEventArgs args) {
        Platform.runLater(() -> {
            Player player = args.getPlayer();
            int shieldValue = args.getShieldValue();
            PacmanVisualisation pacmanVisualisation = new PacmanVisualisation(player);
            Node nextNode = pacmanVisualisation.getShieldNode();

           if (shieldValue > 0 && !shieldsActivated.containsKey(player.getID())) {
                shieldsActivated.put(player.getID(), nextNode);
                shieldVisulisation(player, pacmanVisualisation, nextNode, allEntities.get(player.getID()).getNode(), player.getID());
            }
        });
    }

    @Override
    public void onPlayerShieldRemoved(PlayerShieldRemovedEventArgs args) {
	    Platform.runLater(() -> {
            Player player = args.getPlayer();
            int shieldValue = args.getShieldValue();

            PacmanVisualisation pacmanVisualisation = new PacmanVisualisation(player);
            Node nextNode = pacmanVisualisation.getNode();


            if (shieldValue <= 0 && shieldsActivated.containsKey(player.getID())) {
                shieldVisulisation(player, pacmanVisualisation, nextNode, shieldsActivated.get(player.getID()), player.getID());
                shieldsActivated.remove(player.getID());
            }

        });
    }

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		synchronized (addedEntityIDs) {
			addedEntityIDs.add(args.getEntityID());
			
			Entity e = game.getWorld().getEntity(args.getEntityID());
			
			if (e instanceof Player) {
				Player thiscontrolledPlayer = (Player)e;
				thiscontrolledPlayer.getSkillSet().getOnPlayerCooldownChanged().addListener(this);
				thiscontrolledPlayer.getSkillSet().getOnPlayerLaserActivated().addListener(this);
		        thiscontrolledPlayer.getSkillSet().getOnPlayerShieldActivated().addListener(this);
		        thiscontrolledPlayer.getSkillSet().getOnPlayerShieldRemoved().addListener(this);
			}
		}
	}
}