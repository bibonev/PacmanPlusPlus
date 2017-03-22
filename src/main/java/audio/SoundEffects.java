package main.java.audio;

import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.GameEndedListener;

/**
 * Class for playing sound effects
 *
 * @author Rose Kirtley
 *
 */
public interface SoundEffects extends GameEndedListener, GameCreatedListener {

	/**
	 * Set whether the sound effects play or not
	 *
	 * @param bool
	 */
	public void setOn(final boolean bool);
	public void fireLasers();
	public void playShield();

}
