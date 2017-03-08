package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import teamproject.constants.*;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.event.listener.GameEndedListener;
import teamproject.gamelogic.core.GameLogic;
import teamproject.gamelogic.domain.*;
import teamproject.ui.GameUI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements GameDisplayInvalidatedListener, GameEndedListener  {
	private Pane root;
	private Timeline timeLine;
	private ControlledPlayer controlledPlayer;
	private GameUI gameUI;
	private Game game;
	private GameLogic gameLogic;
	private ArrayList<Node> allEntities;
	private HashMap<String, RotateTransition> rotations;
	private HashMap<String, TranslateTransition> transitions;
    private boolean flag;

    /**
	 * Initialize new visualisation of the map
	 *
	 * @param gameUI
	 * @param game
	 */
	public Render(final GameUI gameUI, final Game game, final GameLogic gameLogic) {
		this.gameUI = gameUI;
		this.game = game;
		this.gameLogic = gameLogic;
		this.gameLogic.getOnGameDisplayInvalidated().addListener(this);
		this.gameLogic.getOnGameEnded().addListener(this);
        this.flag = false;

		this.transitions = new HashMap<>();
		this.rotations = new HashMap<>();
		this.allEntities = new ArrayList<>();
		controlledPlayer = game.getPlayer();
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawWorld() {
		final Cell[][] cells = game.getWorld().getMap().getCells();

		root = new Pane();
		root.setStyle("-fx-background-color: black");

		final Scene scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		addToRoot(cells);

        for (final Player player : game.getWorld().getPlayers()) {
            Node playerNode = new PacmanVisualisation(player).getNode();
            TranslateTransition transitionPlayer = new TranslateTransition(Duration.millis(140), playerNode);
			RotateTransition rotatePlayer = new RotateTransition(Duration.millis(30), playerNode);

			transitions.put(player.getName(), transitionPlayer);
			rotations.put(player.getName(), rotatePlayer);

			allEntities.add(playerNode);
            root.getChildren().add(playerNode);
        }

        for (final Ghost ghost : game.getWorld().getGhosts()) {
            Node ghostNode = new GhostVisualisation(ghost.getPosition()).getNode();
            TranslateTransition transitionGhost = new TranslateTransition(Duration.millis(140), ghostNode);

            transitions.put(Integer.toString(ghost.getID()), transitionGhost);

            allEntities.add(ghostNode);

            root.getChildren().add(ghostNode);
        }

		return scene;
	}

	/**
	 * Redraw the map
	 */
	public void redrawWorld() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		PositionVisualisation.initScreenDimensions();

        clearRoot();
        addToRoot(cells);

		for (final Player player : game.getWorld().getPlayers()) {
		    ImageView nextNode = new PacmanVisualisation(player).getNode();

            transitions.get(player.getName()).setToY(nextNode.getTranslateY());
            transitions.get(player.getName()).setToX(nextNode.getTranslateX());
            rotations.get(player.getName()).setToAngle(nextNode.getRotate());

            root.getChildren().get(root.getChildren().indexOf(transitions.get(player.getName()).getNode())).toFront();

            rotations.get(player.getName()).play();
            transitions.get(player.getName()).play();
        }

		for (final Ghost ghost : game.getWorld().getGhosts()) {
		    ImageView nextNode = new GhostVisualisation(ghost.getPosition()).getNode();

		    transitions.get(Integer.toString(ghost.getID())).setToY(nextNode.getTranslateY());
            transitions.get(Integer.toString(ghost.getID())).setToX(nextNode.getTranslateX());

            root.getChildren().get(root.getChildren().indexOf(transitions.get(Integer.toString(ghost.getID())).getNode())).toFront();

            transitions.get(Integer.toString(ghost.getID())).play();
		}

		root.requestFocus();
	}

	/**
	 *
	 * Click listener for moving the player
	 */
	public void addClickListener() {
		root.setOnKeyPressed(event -> {
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
			}
		});
	}

	/**
	 * Start the timeline
	 */
	public void startTimeline() {
		timeLine = new Timeline(new KeyFrame(Duration.millis(200), event -> {
			    gameLogic.gameStep(200);
			}
		));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

    /**
     * Add all nodes to parent root
     * @param cells
     */
    private void addToRoot(final Cell[][] cells) {
        for (final Cell[] cell : cells) {
            for (final Cell c : cell) {
                final Node cv = new CellVisualisation(c).getNode();
                root.getChildren().add(cv);
            }
        }
    }

    /**
     * Remove nodes that have been affected (like tokens)
     */
    private void clearRoot() {
        int countOfNodes = root.getChildren().size();
        for (int index = 0; index < countOfNodes; index++) {
            if (index < root.getChildren().size()){
                if(!allEntities.contains(root.getChildren().get(index))){
                    root.getChildren().remove(index);
                }
            }
        }
    }

    /**
     * End the game
     */
    private void gameEnded() {
        timeLine.stop();
        gameUI.switchToMenu();
        root.setOnKeyPressed(e -> {
        });
    }

	@Override
	public void onGameDisplayInvalidated(GameDisplayInvalidatedEventArgs args) {
		Platform.runLater(this::redrawWorld);
	}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {
		gameEnded();
		
		// TODO do something with the game outcome - eg. display it on screen
	}
}
