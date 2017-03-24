package main.java.event.listener;

import main.java.event.arguments.GameDisplayInvalidatedEventArgs;

/**
 * Represents objects that receive messages when the game state has changed
 * in such a way that its visible representation is now invalid (ie. the
 * game looks different) and the display must now be updated.
 * 
 * @author Tom Galvin
 *
 */
public interface GameDisplayInvalidatedListener {
	/**
	 * Called when any display of the game is invalidated due to game
	 * state changes.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onGameDisplayInvalidated(GameDisplayInvalidatedEventArgs args);
}
