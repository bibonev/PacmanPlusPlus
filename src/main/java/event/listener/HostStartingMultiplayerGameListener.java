package main.java.event.listener;

import main.java.event.arguments.HostStartingMultiplayerGameEventArgs;

/**
 * Represents objects that receive messages when the host of a multiplayer
 * game lobby has started the game.
 * 
 * @author Tom Galvin
 *
 */
public interface HostStartingMultiplayerGameListener {
	/**
	 * Called when the multiplayer host starts the game (ie. transition from
	 * lobby to game display).
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onHostStartingGame(HostStartingMultiplayerGameEventArgs args);
}
