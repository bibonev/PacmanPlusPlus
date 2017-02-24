package teamproject.gamelogic.domain;

import teamproject.constants.CellState;
import teamproject.constants.CellType;

/**
 * Represent a cell on a map
 * 
 * @author aml
 *
 */
public class Cell {

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

	/**
	 * Get the type of the cell
	 * 
	 * @return a CellType enum value
	 */
	public CellType getType() {
		return type;
	}

	/**
	 * Get the state of the cell
	 * 
	 * @return a CellState enum value
	 */
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
