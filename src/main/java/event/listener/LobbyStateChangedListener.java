package main.java.event.listener;

import main.java.event.arguments.LobbyChangedEventArgs;

/**
 * Represents objects that receive messages when a player joins or
 * leaves a lobby, or the game rules display change.
 * 
 * @author Tom Galvin
 *
 */
public interface LobbyStateChangedListener {
	/**
	 * Called when the state of the pre-game lobby changes in some way.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onLobbyStateChanged(LobbyChangedEventArgs args);
}
