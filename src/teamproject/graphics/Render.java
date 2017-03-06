package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import teamproject.constants.*;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.event.listener.GameEndedListener;
import teamproject.gamelogic.core.GameLogic;
import teamproject.gamelogic.domain.*;
import teamproject.ui.GameUI;

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

		controlledPlayer = game.getPlayer();
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawWorld() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		Images.Border = new ImageView("border.jpg");
		root = new Pane();
		root.setStyle("-fx-background-color: black");

		final Scene scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		addToRoot(root, cells);

		return scene;
	}

	/**
	 * Redraw the map
	 */
	public void redrawWorld() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		PositionVisualisation.initScreenDimensions();

		root.getChildren().clear();

		addToRoot(root, cells);

		for (final Player player : game.getWorld().getPlayers()) {
			root.getChildren().add(new PacmanVisualisation(player).getNode());
		}

		for (final Ghost ghost : game.getWorld().getGhosts()) {
			root.getChildren().add(new GhostVisualisation(ghost.getPosition()).getNode());
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
		timeLine = new Timeline(new KeyFrame(Duration.millis(250), event -> {
			gameLogic.gameStep(250);
			}
		));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

    /**
     * Add to parent root all nodes
     * @param root
     * @param cells
     */
    private void addToRoot(final Pane root, final Cell[][] cells) {
        for (final Cell[] cell : cells) {
            for (final Cell c : cell) {
				final CellVisualisation cv = new CellVisualisation(c);
				root.getChildren().add(cv.getNode());
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
		Platform.runLater(() -> {
			redrawWorld();
		});
	}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {
		gameEnded();
		
		// TODO do something with the game outcome - eg. display it on screen
	}
}
