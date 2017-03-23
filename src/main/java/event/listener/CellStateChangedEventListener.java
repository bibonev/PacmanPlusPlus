package main.java.event.listener;

import main.java.event.arguments.CellStateChangedEventArgs;

/**
 * Represents an object that receives messages when the state
 * of a cell in the world changes.
 * 
 * @author Boyan Bonev
 */
public interface CellStateChangedEventListener {
	/**
	 * Called when the state of a cell in the world changes.
	 * 
	 * @param args Information regarding the event.
	 */
	public void onCellStateChanged(CellStateChangedEventArgs args);
}
