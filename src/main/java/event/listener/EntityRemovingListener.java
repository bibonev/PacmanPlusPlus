package main.java.event.listener;

import main.java.event.arguments.EntityChangedEventArgs;

/**
 * Represents an object which receives messages when an entity
 * is removed from a world.
 * 
 * @author Tom Galvin
 *
 */
public interface EntityRemovingListener {
	/**
	 * Called when an entity is removed from the world.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onEntityRemoving(EntityChangedEventArgs args);
}
