package main.java.event.listener;

import main.java.event.arguments.ReadyToStartEventArgs;

/**
 * Represents objects which receive messages when the current player
 * is ready to start playing the game. Normally this is fired immediately
 * once the game starts, but is used to give game clients the chance to
 * initialise any graphics resources before the server starts sending
 * data to them.
 * 
 * @author Tom Galvin
 *
 */
public interface ReadyToStartListener {
	/**
	 * Called when the local player is ready to start playing.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onReadyToStart(ReadyToStartEventArgs args);
}
