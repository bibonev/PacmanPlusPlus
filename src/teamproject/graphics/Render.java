package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import teamproject.ai.AIGhost;
import teamproject.ai.DefaultBehaviour;
import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.listener.EntityMovedListener;
import teamproject.event.listener.LocalEntityUpdatedListener;
import teamproject.gamelogic.domain.*;
import teamproject.ui.GameUI;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import static java.lang.System.exit;

/**
 * Created by Boyan Bonev on 09/02/2017.
 */
public class Render implements LocalEntityUpdatedListener {
	private Pane root;
	private Timeline timeLine;
	private Scene scene;
	private ControlledPlayer controlledPlayer;
	private GameUI gameUI;
	private World world;
	private boolean serverMode;

	/**
	 * Initialize new visualisation of the map
	 * 
	 * @param world
	 */
	public Render(GameUI gameUI, ControlledPlayer player, World world, boolean serverMode) {
		this.gameUI = gameUI;
		this.controlledPlayer = player;
		this.world = world;
		this.serverMode = serverMode;
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

		scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				Cell c = cells[i][j];
				CellVisualisation cv = new CellVisualisation(cells[i][j]);
				root.getChildren().add(cv.getNode());
			}
		}

		return scene;
	}

	/**
	 * Redraw the map
	 */
	public void redrawMap() {
		Cell[][] cells = this.world.getMap().getCells();
		PositionVisualisation.initScreenDimensions();

		root.getChildren().clear();

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				Cell c = cells[i][j];
				CellVisualisation cv = new CellVisualisation(cells[i][j]);
				root.getChildren().add(cv.getNode());
			}
		}

		for(Player player : world.getPlayers()) {
			root.getChildren().add(new PacmanVisualisation(player).getNode());
		}
		
		for(Ghost ghost : world.getGhosts()) {
			root.getChildren().add(new GhostVisualisation(ghost, controlledPlayer).getNode());
		}

		root.requestFocus();
	}

	/**
	 * Move Ghost randomly
	 */
	void moveGhost(Ghost ghostToMove) {
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 0) + 1) + 0;

		if (controlledPlayer.getPosition().getRow() > ghostToMove.getPosition().getRow()) {
			if (controlledPlayer.getPosition().getColumn() > ghostToMove.getPosition().getColumn()) {
				randomNum = rand.nextInt((1 - 0) + 1) + 0;

				if (randomNum == 0)
					moveDown(ghostToMove);
				else
					moveRight(ghostToMove);
			}

			if (controlledPlayer.getPosition().getColumn() <= ghostToMove.getPosition().getColumn()) {
				randomNum = rand.nextInt((1 - 0) + 1) + 0;

				if (randomNum == 0)
					moveDown(ghostToMove);
				else
					moveLeft(ghostToMove);
			}

		} else if (controlledPlayer.getPosition().getRow() <= ghostToMove.getPosition().getRow()) {
			if (controlledPlayer.getPosition().getColumn() >= ghostToMove.getPosition().getColumn()) {
				randomNum = rand.nextInt((1 - 0) + 1) + 0;

				if (randomNum == 0)
					moveUp(ghostToMove);
				else
					moveRight(ghostToMove);
			}

			if (controlledPlayer.getPosition().getColumn() < ghostToMove.getPosition().getColumn()) {
				randomNum = rand.nextInt((1 - 0) + 1) + 0;

				if (randomNum == 0)
					moveUp(ghostToMove);
				else
					moveLeft(ghostToMove);
			}
		}

		if (ghostToMove.getPosition().equals(controlledPlayer.getPosition())) {
			gameEnded();
		} else {
			redrawMap();
		}
	}

	public boolean moveTo(final int row, final int column, double angle, Entity entity) {
		if (RuleEnforcer.isOutOfBounds(row, column))
			return false;

		if (world.getMap().getCell(row, column).getState() == CellState.OBSTACLE)
			return false;
		entity.setPosition(new Position(row, column));
		if(entity == controlledPlayer)
			controlledPlayer.setAngle(angle);

		if (world.getMap().getCell(row, column).getState() == CellState.FOOD) {
			world.getMap().getCell(row, column).setState(CellState.EMPTY);
		}

		redrawMap();

		return true;
	}

	/**
	 * Move the PacMan up
	 * 
	 * @return true/false depending on whether the move is legit or not
	 */
	boolean moveUp(Entity toMove) {
		return moveTo(toMove.getPosition().getRow() - 1, toMove.getPosition().getColumn(), 270,
				toMove);
	}

	/**
	 * Move the PacMan down
	 * 
	 * @return true/false depending on whether the move is legit or not
	 */
	boolean moveDown(Entity toMove) {
		return moveTo(toMove.getPosition().getRow() + 1, toMove.getPosition().getColumn(), 90,
				toMove);
	}

	/**
	 * Move the PacMan left
	 * 
	 * @return true/false depending on whether the move is legit or not
	 */
	boolean moveLeft(Entity toMove) {
		return moveTo(toMove.getPosition().getRow(), toMove.getPosition().getColumn() - 1, -180,
				toMove);
	}

	/**
	 * Move the PacMan right
	 * 
	 * @return true/false depending on whether the move is legit or not
	 */
	boolean moveRight(Entity toMove) {
		return moveTo(toMove.getPosition().getRow(), toMove.getPosition().getColumn() + 1, 0,
				toMove);
	}

	/**
	 * Click listener for moving the ghost
	 */
	public void addClickListener() {
		root.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP) {
				moveUp(controlledPlayer);
			} else if (event.getCode() == KeyCode.DOWN) {
				moveDown(controlledPlayer);
			} else if (event.getCode() == KeyCode.LEFT) {
				moveLeft(controlledPlayer);
			} else if (event.getCode() == KeyCode.RIGHT) {
				moveRight(controlledPlayer);
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
		timeLine = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
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
		moveTo(args.getRow(),args.getCol(),args.getAngle(),args.getEntity());
		
	}
}
