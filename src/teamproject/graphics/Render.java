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
	private HashMap<Integer, Node> allEntities;
	private Node[][] worldNodes;
	private HashMap<Integer, RotateTransition> rotations;
	private HashMap<Integer, TranslateTransition> transitions;
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
		this.allEntities = new HashMap<>();
		controlledPlayer = game.getPlayer();
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawWorld() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		int rows = game.getWorld().getMap().getMapSize(), columns = rows;
		worldNodes = new Node[rows][columns];

		root = new Pane();
		root.setStyle("-fx-background-color: black");

		final Scene scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);
		
		for (final Player player : game.getWorld().getPlayers()) {
            Node playerNode = new PacmanVisualisation(player).getNode();
            TranslateTransition transitionPlayer = new TranslateTransition(Duration.millis(140), playerNode);
			RotateTransition rotatePlayer = new RotateTransition(Duration.millis(30), playerNode);

			transitions.put(player.getID(), transitionPlayer);
			rotations.put(player.getID(), rotatePlayer);

			allEntities.put(player.getID(), playerNode);
            root.getChildren().add(playerNode);
        }

        for (final Ghost ghost : game.getWorld().getGhosts()) {
            Node ghostNode = new GhostVisualisation(ghost.getPosition()).getNode();
            TranslateTransition transitionGhost = new TranslateTransition(Duration.millis(140), ghostNode);

            transitions.put(ghost.getID(), transitionGhost);

            allEntities.put(ghost.getID(), ghostNode);

            root.getChildren().add(ghostNode);
        }

		redrawWorld();

		return scene;
	}

	private int test = 0;
	/**
	 * Redraw the map
	 */
	public void redrawWorld() {
		System.out.println("redraw " + test++);
		PositionVisualisation.initScreenDimensions();

    	redrawCells();

		for (final Player player : game.getWorld().getPlayers()) {
		    ImageView nextNode = new PacmanVisualisation(player).getNode();

            transitions.get(player.getID()).setToY(nextNode.getTranslateY());
            transitions.get(player.getID()).setToX(nextNode.getTranslateX());
            rotations.get(player.getID()).setToAngle(nextNode.getRotate());

            root.getChildren().get(root.getChildren().indexOf(allEntities.get(player.getID()))).toFront();
            rotations.get(player.getID()).play();
            transitions.get(player.getID()).play();
        }

		for (final Ghost ghost : game.getWorld().getGhosts()) {
		    ImageView nextNode = new GhostVisualisation(ghost.getPosition()).getNode();

		    transitions.get(ghost.getID()).setToY(nextNode.getTranslateY());
            transitions.get(ghost.getID()).setToX(nextNode.getTranslateX());

            root.getChildren().get(root.getChildren().indexOf(allEntities.get(ghost.getID()))).toFront();

            transitions.get(ghost.getID()).play();
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
			} else if (event.getCode() == KeyCode.DOWN) {
				controlledPlayer.moveDown();
			} else if (event.getCode() == KeyCode.LEFT) {
				controlledPlayer.moveLeft();
			} else if (event.getCode() == KeyCode.RIGHT) {
				controlledPlayer.moveRight();
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
	
	// only redraw cells that have changed state
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
					cells[row][column].clearNeedsRedrawFlag();
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
