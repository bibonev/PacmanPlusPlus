package teamproject.graphics;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Behaviour.Type;

import static java.lang.System.exit;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class MapVisualisation {

	private Pane root;
	private Timeline timeLine;
	private Scene scene;
	private Stage stage;
	private GridVisualisation grid;

	private ArrayList<PositionVisualisation> obstacles = new ArrayList<>();
    private boolean isPaused;

    /**
	 * Initialize new visualisation of the map
	 * @param grid - grid representation for visualizing
	 */
	public MapVisualisation(final GridVisualisation grid) {
		this.grid = grid;
	}

	/**
	 * Generate the map
	 * @param stage
	 * @return the stage that contians the scene with the map
	 */
	public Stage generateMap(Stage stage) {
		this.stage = stage;

		Images.Border = new ImageView("border.jpg");
		root = new Pane();
		root.setStyle("-fx-background-color: black");

		scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		// Create obstacles
		initObstacles();
		CellState state = CellState.EMPTY;

		for (int i = 0; i < CellSize.Columns; i++) {
			for (int j = 0; j < CellSize.Rows; j++) {
				final PositionVisualisation position = new PositionVisualisation(i, j);

				// Check if not boundary
				if (i != CellSize.Columns && j != CellSize.Rows) {
					if (i == 1 && j == 1) {
						state = CellState.EMPTY;
					} else if (isObstacle(position)) {
						state = CellState.OBSTACLE;
					} else {
						state = CellState.FOOD;
					}
				}

				final CellVisualisation cell = new CellVisualisation(CellType.NORMAL, state, position);
				grid.addVisualCell(cell);

				root.getChildren().add(cell.getNode());
			}

		}

		this.stage.setScene(scene);
		return this.stage;
	}

	/**
	 * Redrwa the map
	 */
	public void redrawMap() {
		PositionVisualisation.initScreenDimensions();

		root.getChildren().clear();

		for (int i = 0; i < CellSize.Rows; i++) {
			for (int j = 0; j < CellSize.Columns; j++) {
				root.getChildren().add(grid.getCell(i, j).getNode());
			}
		}

		root.getChildren().add(GamePlay.pacman.getNode());

		root.getChildren().add(GamePlay.ghost1.getNode());

		root.requestFocus();
	}

	/**
	 * Click listener for moving the ghost
	 */
	public void addClickListener() {
		root.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP) {
				GamePlay.pacman.moveUp();
			} else if (event.getCode() == KeyCode.DOWN) {
				GamePlay.pacman.moveDown();
			} else if (event.getCode() == KeyCode.LEFT) {
				GamePlay.pacman.moveLeft();
			} else if (event.getCode() == KeyCode.RIGHT) {
                GamePlay.pacman.moveRight();
            } else if (event.getCode() == KeyCode.R){
                invalidateClickListener();
                timeLine.pause();
            } else if (event.getCode() == KeyCode.Q){
			    timeLine.stop();
			    exit(0);
            }
		});
	}

	/**
	 * Click listener for restarting the game
	 */
	public void invalidateClickListener() {
		root.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SPACE) {
                replay();
			} else if (event.getCode() == KeyCode.Q){
                timeLine.stop();
                exit(0);
            } else if (event.getCode() == KeyCode.R) {
			    addClickListener();
                timeLine.play();
            }
		});
	}

	/**
	 * Restart the game
	 */
	public void replay() {
		final Behaviour sampleBehavior = new BasicBehaviour(Type.DEFAULT);

		// Generate Map
		generateMap(stage);

		// Add CLick istener
		addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(sampleBehavior, "Player1", grid, this);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(sampleBehavior, "Ghost1", grid, GamePlay.pacman, this);

		// Redraw Map
		redrawMap();

		// Start Timeline
		startTimeline();
	}

	/**
	 * End the game
	 */
	public void gameEnded() {
		invalidateClickListener();
		timeLine.stop();
	}

	/**
	 * Start the timeline
	 */
	public void startTimeline() {
		timeLine = new Timeline(new KeyFrame(Duration.millis(250), event -> {
			GamePlay.ghost1.moveGhost();
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	/**
	 * Get the grid
	 * @return the passed grid
	 */
	public GridVisualisation getGrid() {
		return grid;
	}

	private boolean isObstacle(final PositionVisualisation position) {
		for (final PositionVisualisation tmpPosition : obstacles) {
			if (position.getRow() == tmpPosition.getRow() && position.getColumn() == tmpPosition.getColumn()) {
				return true;
			}
		}

		return false;
	}

	private void initObstacles() {

		// Generate Left Obstacles
		obstacles.add(new PositionVisualisation(2, 2));
		obstacles.add(new PositionVisualisation(1, 4));
		obstacles.add(new PositionVisualisation(2, 4));
		obstacles.add(new PositionVisualisation(3, 4));
		obstacles.add(new PositionVisualisation(4, 4));

		obstacles.add(new PositionVisualisation(4, 2));
		obstacles.add(new PositionVisualisation(5, 2));
		obstacles.add(new PositionVisualisation(6, 2));

		obstacles.add(new PositionVisualisation(6, 3));

		obstacles.add(new PositionVisualisation(13, 4));
		obstacles.add(new PositionVisualisation(12, 4));
		obstacles.add(new PositionVisualisation(11, 4));
		obstacles.add(new PositionVisualisation(10, 4));

		obstacles.add(new PositionVisualisation(12, 2));
		obstacles.add(new PositionVisualisation(8, 2));
		obstacles.add(new PositionVisualisation(9, 2));
		obstacles.add(new PositionVisualisation(11, 2));

		obstacles.add(new PositionVisualisation(3, 6));

		// Generate Reflection
		final int loopSize = obstacles.size();
		for (int i = 0; i < loopSize; i++) {

			final PositionVisualisation tmpPosition = obstacles.get(i);
			final PositionVisualisation newPosition = new PositionVisualisation(tmpPosition.getRow(),
					CellSize.Columns - 1 - tmpPosition.getColumn());
			obstacles.add(newPosition);

		}

		// Generate Center Obstacles
		obstacles.add(new PositionVisualisation(6, 6));
		obstacles.add(new PositionVisualisation(7, 6));
		obstacles.add(new PositionVisualisation(8, 6));
		obstacles.add(new PositionVisualisation(8, 7));
		obstacles.add(new PositionVisualisation(8, 8));

		obstacles.add(new PositionVisualisation(7, 8));
		obstacles.add(new PositionVisualisation(6, 8));

		obstacles.add(new PositionVisualisation(10, 7));
		obstacles.add(new PositionVisualisation(11, 7));
		obstacles.add(new PositionVisualisation(12, 7));

		obstacles.add(new PositionVisualisation(2, 7));
		obstacles.add(new PositionVisualisation(3, 7));
		obstacles.add(new PositionVisualisation(4, 7));
	}
}
