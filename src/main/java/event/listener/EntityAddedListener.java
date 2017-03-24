package main.java.event.listener;

import main.java.event.arguments.EntityChangedEventArgs;

/**
 * Represents an object which receives messages when an entity is added to 
 * a world.
 * 
 * @author Tom Galvin
 *
 */
public interface EntityAddedListener {
	/**
	 * Called when an entity is added to the world.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onEntityAdded(EntityChangedEventArgs args);
}
