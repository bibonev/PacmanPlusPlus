package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import teamproject.ai.AIGhost;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.listener.LocalEntityUpdatedListener;
import teamproject.gamelogic.domain.*;
import teamproject.ui.GameUI;

import java.util.*;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements LocalEntityUpdatedListener {
	private Pane root;
	private Timeline timeLine;
    private ControlledPlayer controlledPlayer;
	private GameUI gameUI;
	private World world;
	private boolean serverMode;

	private EntityMovement moveControlledPlayer;
	private HashMap<Entity, EntityMovement> ghostMovements;

    /**
     * Initialize new visualisation of the map
     *
     * @param gameUI
     * @param player
     * @param world
     * @param serverMode
     */
	public Render(GameUI gameUI, ControlledPlayer player, World world, boolean serverMode) {
		this.gameUI = gameUI;
		this.world = world;
		this.serverMode = serverMode;

		this.controlledPlayer = player;
		this.moveControlledPlayer = new EntityMovement(controlledPlayer, world);

		this.ghostMovements = new HashMap<>();
		for (AIGhost ghost : world.getGhosts()) {
			this.ghostMovements.put(ghost, new EntityMovement(ghost, world));
		}
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawMap() {
		Cell[][] cells = this.world.getMap().getCells();
		Images.Border = new ImageView("border.jpg");
		root = new Pane();
		root.setStyle("-fx-background-color: black");

        Scene scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		addToRoot(root, cells);

		return scene;
	}

	/**
	 * Redraw the map
	 */
	public void redrawMap() {
		Cell[][] cells = this.world.getMap().getCells();
		PositionVisualisation.initScreenDimensions();

		root.getChildren().clear();

		addToRoot(root, cells);

		for(Player player : world.getPlayers()) {
			root.getChildren().add(new PacmanVisualisation(player).getNode());
		}
		
		for(Ghost ghost : world.getGhosts()) {
			root.getChildren().add(new GhostVisualisation(ghost, controlledPlayer).getNode());
		}

		root.requestFocus();
	}

	private void addToRoot(Pane root, Cell[][] cells) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				Cell c = cells[i][j];
				CellVisualisation cv = new CellVisualisation(cells[i][j]);
				root.getChildren().add(cv.getNode());
			}
		}
	}
	/**
	 * Click listener for moving the ghost
	 */
	public void addClickListener() {
		root.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP) {
				moveControlledPlayer.moveUp();
				redrawMap();
			} else if (event.getCode() == KeyCode.DOWN) {
				moveControlledPlayer.moveDown();
				redrawMap();
			} else if (event.getCode() == KeyCode.LEFT) {
				moveControlledPlayer.moveLeft();
				redrawMap();
			} else if (event.getCode() == KeyCode.RIGHT) {
				moveControlledPlayer.moveRight();
				redrawMap();
			}
		});
	}

	/**
	 * End the game
	 */
	void gameEnded() {
		timeLine.stop();
		gameUI.switchToMenu();
		root.setOnKeyPressed(e -> {});
	}

	/**
	 * Start the timeline
	 */
	public void startTimeline() {
		for(AIGhost ghost : world.getGhosts()) {
			ghost.getBehaviour().getOnMovedEvent().addListener(this);
		}

		timeLine = new Timeline(new KeyFrame(Duration.millis(250), event -> {
				if(serverMode) {
					for(AIGhost ghost : world.getGhosts()) {
						ghost.run();
					}
				}
			}
		));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	@Override
	public void onEntityMoved(EntityMovedEventArgs args) {
		this.ghostMovements.get(args.getEntity()).moveTo(args.getRow(), args.getCol(), args.getAngle());
		redrawMap();
	}
}
