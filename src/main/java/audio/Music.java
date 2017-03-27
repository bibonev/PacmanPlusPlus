package main.java.audio;

import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.GameEndedListener;

/**
 * Class for playing background music
 *
 * @author Rose Kirtley
 *
 */
public interface Music extends GameCreatedListener, GameEndedListener {
	/**
	 * Set whether the background music is playing or not
	 * 
	 * @param bool,
	 *            true for music to play, false to turn music off
	 */
	public void setOn(final boolean bool);

	public void playMusic();

	public void stopMusic();
	
	/**
	 * Pauses or plays the music depending on the current state
	 */
	public void pausePlay();
}
