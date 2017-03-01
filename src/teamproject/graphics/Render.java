package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import teamproject.ai.AIGhost;
import teamproject.constants.*;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.listener.RemoteEntityUpdatedListener;
import teamproject.gamelogic.domain.*;
import teamproject.constants.GameType;
import teamproject.ui.GameUI;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements RemoteEntityUpdatedListener {
	private Pane root;
	private Timeline timeLine;
	private ControlledPlayer controlledPlayer;
	private GameUI gameUI;
	private World world;
	private Game game;

	/**
	 * Initialize new visualisation of the map
	 *
	 * @param gameUI
	 * @param game
	 */
	public Render(final GameUI gameUI, final Game game) {
		this.gameUI = gameUI;
		this.world = game.getWorld();
		this.game = game;

		controlledPlayer = game.getPlayer();
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawWorld() {
		final Cell[][] cells = world.getMap().getCells();
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
		final Cell[][] cells = world.getMap().getCells();
		PositionVisualisation.initScreenDimensions();

		root.getChildren().clear();

		addToRoot(root, cells);

		for (final Player player : world.getPlayers()) {
			root.getChildren().add(new PacmanVisualisation(player).getNode());
		}

		for (final Ghost ghost : world.getGhosts()) {
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
				checkGameEnding();
			} else if (event.getCode() == KeyCode.DOWN) {
				controlledPlayer.moveDown();
				redrawWorld();
				checkGameEnding();
			} else if (event.getCode() == KeyCode.LEFT) {
				controlledPlayer.moveLeft();
				redrawWorld();
				checkGameEnding();
			} else if (event.getCode() == KeyCode.RIGHT) {
				controlledPlayer.moveRight();
				redrawWorld();
				checkGameEnding();
			}
		});
	}

	/**
	 * Start the timeline
	 */
	public void startTimeline() {
		for(Ghost ghost : world.getGhosts()) {
			ghost.getOnMovedEvent().addListener(this);
		}
		timeLine = new Timeline(new KeyFrame(Duration.millis(250), event -> {
			if(game.getGameType() == GameType.SINGLEPLAYER) {
				for(AIGhost ghost : world.getEntities(AIGhost.class)) {
					ghost.run();
				}
			}
			redrawWorld();
			}
		));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	@Override
	public void onEntityMoved(final EntityMovedEventArgs args) {
		redrawWorld();
		checkGameEnding();
	}

	private void checkGameEnding(){
		if(RuleChecker.getGameOutcome(game)
				== GameOutcome.LOCAL_PLAYER_LOST){
			gameEnded();
		} else if(RuleChecker.getGameOutcome(game)
				== GameOutcome.LOCAL_PLAYER_WON){
			gameEnded();
		}
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
}
