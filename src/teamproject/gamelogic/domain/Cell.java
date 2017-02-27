package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

/**
 * Represent a cell on a map
 * 
 * @author aml
 *
 */
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

	/**
	 * Get the cell's position
	 * 
	 * @return a position object
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Update the state of the cell
	 * 
	 * @param state
	 *            the new cell state
	 */
	public void setState(final CellState state) {
		this.state = state;
	}

}
