package main.java.event.listener;

import main.java.event.arguments.EntityMovedEventArgs;

/**
 * Represents an object that receives messages when an entity 
 * moves in the world.
 * 
 * @author Tom Galvin
 *
 */
public interface EntityMovedListener {
	/**
	 * Called when an entity moves position in the world.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onEntityMoved(EntityMovedEventArgs args);

}
