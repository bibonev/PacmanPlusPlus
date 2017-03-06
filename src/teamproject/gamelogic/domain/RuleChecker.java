package teamproject.gamelogic.domain;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.GameOutcomeType;
import teamproject.constants.GameType;

/**
 * Check different rules, constraints and states
 *
 * @author aml
 *
 */
public class RuleChecker {

	/**
	 * Check cell validity.
	 *
	 * @param cell
	 *            the cell
	 * @return true, if successful
	 */
	public static boolean checkCellValidity(final Cell cell) {
		return cell.getPosition().getRow() >= 0 && cell.getPosition().getColumn() >= 0
				&& cell.getState() != CellState.OBSTACLE;

	}

	/**
	 * Check whether the player is trying to go outside of the map
	 *
	 * @param row
	 * @param column
	 *
	 * @return
	 */
	public static boolean isOutOfBounds(final int row, final int column) {
		return row > CellSize.Rows - 1 || column > CellSize.Columns - 1 || row < 0 || column < 0;
	}
}
