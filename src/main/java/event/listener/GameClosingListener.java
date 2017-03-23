package main.java.event.listener;

/**
 * Represents an object which receives messages when the game window is
 * closing.
 * @author Tom Galvin
 *
 */
public interface GameClosingListener {
	/**
	 * Called when the game window is about to close.
	 * 
	 */
	public void onGameClosing();
}
