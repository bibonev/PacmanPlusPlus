package main.java.event.listener;

import main.java.event.arguments.RemoteGameEndedEventArgs;

/**
 * Represents objects which receive events when a networked game
 * ends server-side.
 * 
 * @author Tom Galvin
 *
 */
public interface RemoteGameEndedListener {
	/**
	 * Called when the server side game ends.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onRemoteGameEnded(RemoteGameEndedEventArgs args);
}
