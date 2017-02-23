package teamproject.gamelogic.domain;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.constants.GameType;

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

	/**
	 * Check whether game should end
	 *
	 * @param game
	 * @return
	 */
	// TODO: possibly add the type as a game field instead of passing them
	// separately
	public static boolean gameShouldEnd(final Game game, final GameType type) {
		return type.equals(GameType.SINGLEPLAYER) ? singleplayerGameShouldEnd(game) : multiplayerGameShouldEnd(game);
	}

	private static boolean multiplayerGameShouldEnd(final Game game) {
		// TODO More complicated so will be implemented once networking is
		// integrated fully
		return false;
	}

	private static boolean singleplayerGameShouldEnd(final Game game) {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		boolean ghostAtePlayer = false;
		boolean foodLeft = false;

		for (final Cell[] cellRow : cells) {
			for (int j = 0; j < cells[0].length; j++) {
				ghostAtePlayer = ghostAtePlayer || cellRow[j].getState().equals(CellState.PLAYER_AND_ENEMY);
				foodLeft = foodLeft || cellRow[j].getState().equals(CellState.FOOD);
			}
		}

		return ghostAtePlayer || !foodLeft;
	}
}
