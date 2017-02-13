package teamproject.gamelogic.domain;

import teamproject.constants.CellState;
import teamproject.constants.CellType;

public class RuleEnforcer {

	/**
	 * Check cell validity.
	 *
	 * @param cell
	 *            the cell
	 * @return true, if successful
	 */
	public static boolean checkCellValidity(final Cell cell) {
		return cell.getPosition().getRow() >= 0 && cell.getPosition().getColumn() >= 0
				&& cell.getState() != CellState.OBSTACLE && cell.getType() == CellType.NORMAL;

	}
}
