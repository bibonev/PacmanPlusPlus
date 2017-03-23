package main.java.event.listener;

/**
 * Represents an object that receives messages when the multiplayer
 * lobby countdown starts.
 * 
 * @author Rose Kirtley
 */
public interface CountDownStartingListener {
	/**
	 * Called when the multiplayer lobby countdown starts.
	 */
	public void onCountDownStarted();
}