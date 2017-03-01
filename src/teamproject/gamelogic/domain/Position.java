package teamproject.gamelogic.domain;

/**
 * Represent a position given by a row and a column
 * 
 * @author aml
 *
 */
public class Position {
	private int row;
	private int column;

	public Position(final int row, final int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Fetch the row
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Fetch the column
	 * 
	 * @return column
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof Position) {
			final Position p = (Position) obj;

			return p.row == row && p.column == column;
		} else {
			return false;
		}
	}
}
