package teamproject.gamelogic.domain;

public abstract class Position {

	private int row;
	private int column;

	public Position(final int row, final int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

}
