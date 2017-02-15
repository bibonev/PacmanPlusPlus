package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import teamproject.ai.DefaultBehaviour;
import teamproject.constants.CellSize;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.gamelogic.domain.*;
import teamproject.gamelogic.domain.Behaviour.Type;

import java.util.HashMap;
import java.util.Optional;

import static java.lang.System.exit;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render {

	private Pane root;
	private Timeline timeLine;
	private Scene scene;
	private MapVisualisation grid;

    /**
	 * Initialize new visualisation of the map
	 * 
	 * @param grid
	 *            - grid representation for visualizing
	 */
	public Render(final MapVisualisation grid) {
		this.grid = grid;
	}

	/**
	 * Generate the map
	 *
	 * @return the stage that contians the scene with the map
	 */
	public Scene drawMap(Cell[][] cells) {
		Images.Border = new ImageView("border.jpg");
		root = new Pane();
		root.setStyle("-fx-background-color: black");

		scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
			    PositionVisualisation positionVisualisation =
                        new PositionVisualisation(cells[i][j].getPosition().getRow(), cells[i][j].getPosition().getColumn());
			    CellVisualisation cv = new CellVisualisation(cells[i][j].getType(), cells[i][j].getState(), positionVisualisation);
				grid.addVisualCell(cv);
				root.getChildren().add(cv.getNode());
			}
		}

		return scene;
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
		Map newMap = new MapVisualisation();
		Inventory stash = new Inventory(new HashMap<>(2));
		Behaviour bh = new DefaultBehaviour(grid, new PositionVisualisation(0,0), 2, stash, Type.DEFAULT);
		newMap.generateMap();

		Player pl = new Player(Optional.empty(),"Player1");
		Ghost gh = new GLGhost(bh, "Ghost1");
		// Generate Map
		drawMap(newMap.getCells());

		// Add CLick istener
		addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(pl, grid, this);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(gh, grid, GamePlay.pacman, this);

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
	 * 
	 * @return the passed grid
	 */
	public MapVisualisation getGrid() {
		return grid;
	}
}
