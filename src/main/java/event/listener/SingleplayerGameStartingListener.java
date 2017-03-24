package main.java.event.listener;

import main.java.event.arguments.SingleplayerGameStartingEventArgs;

/**
 * Represents objects which receive messages when a single player
 * game is created and about to start.
 * 
 * @author Tom Galvin
 *
 */
public interface SingleplayerGameStartingListener {
	/**
	 * Called when a singleplayer game is about to start.
	 * 
	 * @param eventArgs Information or parameters regarding/describing the event.
	 */
	public void onSingleplayerGameStarting(SingleplayerGameStartingEventArgs eventArgs);
}
