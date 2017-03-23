package main.java.event.listener;

import main.java.event.arguments.GameEndedEventArgs;

/**
 * Represents objects that receive events when the game currently
 * being played comes to an end (due to all of the players dying,
 * or the dots all being collected, or something else.)
 * 
 * @author Tom Galvin
 *
 */
public interface GameEndedListener {
	/**
	 * Called when the game comes to an end.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onGameEnded(GameEndedEventArgs args);
}
