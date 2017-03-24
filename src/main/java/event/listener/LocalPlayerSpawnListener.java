package main.java.event.listener;

import main.java.event.arguments.LocalPlayerSpawnEventArgs;

/**
 * Represents objects which receive messages when a player which is
 * added to the game world is one which the local user is controlling
 * (ie. the local player respawns).
 * 
 * @author Tom Galvin
 *
 */
public interface LocalPlayerSpawnListener {
	/**
	 * Called when the local player is readded to the world.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onLocalPlayerSpawn(LocalPlayerSpawnEventArgs args);
}
