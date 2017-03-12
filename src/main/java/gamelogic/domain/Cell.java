package main.java.gamelogic.domain;

import main.java.constants.CellState;
import main.java.event.Event;
import main.java.event.arguments.CellStateChangedEventArgs;
import main.java.event.listener.CellStateChangedEventListener;

/**
 * Represent a cell on a map
 *
 * @author aml
 *
 */
public class Cell {
	private CellState state;
	private Position position;
	private Event<CellStateChangedEventListener, CellStateChangedEventArgs> onCellStateChanged;

	public Cell(final CellState state, final Position position) {
		this.state = state;
		this.position = position;
		onCellStateChanged = new Event<>((l, p) -> l.onCellStateChanged(p));
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
		onCellStateChanged.fire(new CellStateChangedEventArgs(this, state));
	}

	public Event<CellStateChangedEventListener, CellStateChangedEventArgs> getOnCellStateChanged() {
		return onCellStateChanged;
	}
}
