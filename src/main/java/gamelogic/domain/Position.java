package main.java.gamelogic.domain;

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

	/**
	 * Creates aa new Position object with this position and the position
	 * {@code p} added together like vectors.
	 *
	 * @param p
	 * @return
	 */
	public Position add(final Position p) {
		return new Position(row + p.row, column + p.column);
	}

	/**
	 * Creates a new Position object with this position and the position
	 * {@code (row, column)} added like vectors.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Position add(final int row, final int column) {
		return new Position(this.row + row, this.column + column);
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

	@Override
	public String toString() {
		return String.format("(%d, %d)", getRow(), getColumn());
	}
}
