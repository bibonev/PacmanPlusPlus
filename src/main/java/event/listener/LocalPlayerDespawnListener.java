package main.java.event.listener;

import main.java.event.arguments.LocalPlayerDespawnEventArgs;

/**
 * Represents objects that receive messages when the locally controlled 
 * player (in single or multiplayer) despawns, ie. is killed.
 * 
 * @author Tom Galvin
 *
 */
public interface LocalPlayerDespawnListener {
	/**
	 * Called when the local player dies, is killed or otherwise is 
	 * removed from the world.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onLocalPlayerDespawn(LocalPlayerDespawnEventArgs args);
}
