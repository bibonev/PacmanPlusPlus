package main.java.event.listener;

/**
 * Represents objects which receive events when the player attempts to
 * leave the game while it is in progress (without straight-up
 * closing the game window).
 * 
 * @author Tom Galvin
 *
 */
public interface PlayerLeavingGameListener {
	/**
	 * Called when the player is leaving the current game in progress.
	 */
	public void onPlayerLeavingGame();
}
