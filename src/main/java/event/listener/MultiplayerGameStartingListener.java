package main.java.event.listener;

import main.java.event.arguments.MultiplayerGameStartingEventArgs;

/**
 * Represents objects which receive messages when a multiplayer
 * game on a server (local for hosts, remote for guests) is about
 * to start.
 * 
 * @author Tom Galvin
 *
 */
public interface MultiplayerGameStartingListener {
	/**
	 * Called when a game is starting over the network.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onMultiplayerGameStarting(MultiplayerGameStartingEventArgs args);
}
