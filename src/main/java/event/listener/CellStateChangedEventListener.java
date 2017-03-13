package main.java.event.listener;

import main.java.event.arguments.CellStateChangedEventArgs;

/**
 * Created by boyanbonev on 01/03/2017.
 */
public interface CellStateChangedEventListener {
	public void onCellStateChanged(CellStateChangedEventArgs args);
}
