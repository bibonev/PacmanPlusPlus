package teamproject.gamelogic.domain;

import java.util.ArrayList;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.graphics.PositionVisualisation;

/**
 * Represent a Pacman game map
 *
 * @author aml
 *
 */
public class Map {

	public static int defaultNumberOfCells = 15;
	private Cell[][] cells;
	private ArrayList<PositionVisualisation> obstacles;

	public Map(final int numberOfCells) {
		cells = new Cell[numberOfCells][numberOfCells];
	}

	public Map() {
		this(defaultNumberOfCells);
	}

	public Map(final Cell[][] cells) {
		this.cells = cells;
	}

	/**
	 * Fetch the map's cells
	 *
	 * @return a matrix of cells
	 */
	public Cell[][] getCells() {
		return cells;
	}

	/**
	 * Fetch the cell at position (x, y)
	 *
	 * @param x
	 * @param y
	 * @return a cell
	 */
	public Cell getCell(final int x, final int y) {
		return cells[x][y];
	}

	/**
	 * Get the map size
	 *
	 * @return size as integer
	 */
	public int getMapSize() {
		return cells.length;
	}

	/**
	 * Update the map's cells
	 *
	 * @param cells
	 *            the new matrix of cells
	 */
	public void setCells(final Cell[][] cells) {
		this.cells = cells;
	}

	/**
	 * Add a new cell on the map
	 *
	 * @param cell
	 *            the cell object to be added
	 */
	public void addCell(final Cell cell) {
		final int x = cell.getPosition().getRow();
		final int y = cell.getPosition().getColumn();
		cells[x][y] = cell;
	}

	/**
	 * Generate a new map
	 *
	 * @return a map object
	 */
	// TODO: Should a map be able generate itself? I don't think so...
	public Map generateMap() {
		initializeObstacles();
		CellState state;

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				final PositionVisualisation position = new PositionVisualisation(i, j);

				if (i == 1 && j == 1) {
					state = CellState.PLAYER;
				} else if (isObstacle(position)) {
					state = CellState.OBSTACLE;
				} else {
					state = CellState.FOOD;
				}

				final Cell cell = new Cell(state, position);
				cells[position.getRow()][position.getColumn()] = cell;
			}
		}

		return new Map(cells);
	}

	/**
	 * Check whether a PositionVisualisation holds an obstacle
	 *
	 * @param position
	 * @return boolean indicating whether or not it's an obstacle
	 */
	private boolean isObstacle(final PositionVisualisation position) {
		for (final PositionVisualisation tmpPosition : obstacles) {
			if (position.getRow() == tmpPosition.getRow() && position.getColumn() == tmpPosition.getColumn()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Initialises obstacles
	 */
	private void initializeObstacles() {
		obstacles = new ArrayList<>();

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
