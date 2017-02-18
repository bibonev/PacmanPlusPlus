package teamproject.gamelogic.domain;

public class Position {
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Position) {
			Position p = (Position)obj;
			
			return p.row == this.row && p.column == this.column;
		} else {
			return false;
		}
	}
}
