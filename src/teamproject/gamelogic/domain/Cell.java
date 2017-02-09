package teamproject.gamelogic.domain;

import teamproject.constants.CellState;
import teamproject.constants.CellType;

public abstract class Cell {

	private CellType type;
	private CellState state;
	private Position position;

	public Cell(final CellType type, final CellState state, final Position position) {
		this.type = type;
		this.state = state;
		this.position = position;
	}

	public Cell(final CellType type, final Position position) {
		this(type, CellState.EMPTY, position);
	}

	public Cell(final Position position) {
		this(CellType.NORMAL, CellState.EMPTY, position);
	}

	public CellType getType() {
		return type;
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
