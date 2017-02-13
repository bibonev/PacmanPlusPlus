package teamproject.graphics;

import teamproject.constants.CellSize;
import teamproject.gamelogic.domain.Map;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class MapVisualisation extends Map {
	private static CellVisualisation[][] grid;

	/**
	 * Initialize the grid
	 */
	public MapVisualisation() {
		super();
		grid = new CellVisualisation[CellSize.Rows][CellSize.Columns];
	}

	/**
	 * Add cell on the grid
	 *
	 * @param cell
	 */
	public void addVisualCell(final CellVisualisation cell) {
		grid[cell.positionVisualisation.getRow()][cell.positionVisualisation.getColumn()] = cell;
	}

	/**
	 * Get particular cell by searching it on row and column
	 *
	 * @param row
	 * @param column
	 * @return CellVisualisation
	 */
	@Override
	public CellVisualisation getCell(final int row, final int column) {
		return grid[row][column];
	}
}
