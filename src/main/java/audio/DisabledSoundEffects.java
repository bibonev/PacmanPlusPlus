package main.java.audio;

import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameCreatedEventArgs;

/**
 * Class for playing sound effects
 *
 * @author Rose Kirtley
 *
 */
public class DisabledSoundEffects implements SoundEffects {

	/**
	 * Set whether the sound effects play or not
	 *
	 * @param bool
	 */
	@Override
	public void setOn(final boolean bool) {}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {}
	@Override
	public void onGameCreated(GameCreatedEventArgs args) {}
	public void fireLasers() {};
	public void playShield() {};

}
