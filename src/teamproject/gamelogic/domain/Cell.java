package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

public class Cell {
	private CellState state;
	private Position position;

	public Cell(final CellState state, final Position position) {
		this.state = state;
		this.position = position;
	}

	public Cell(final Position position) {
		this(CellState.EMPTY, position);
	}

	public CellState getState() {
		return state;
	}

	public Position getPosition() {
		return position;
	}

	public void setState(final CellState state) {
		this.state = state;
	}

}
