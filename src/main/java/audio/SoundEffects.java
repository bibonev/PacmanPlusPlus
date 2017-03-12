package main.java.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class for playing sound effects
 *
 * @author Rose Kirtley
 *
 */
public class SoundEffects {

	private boolean isOn;
	private MediaPlayer expPlayer;
	private MediaPlayer shootPlayer;
	private MediaPlayer powerPlayer;

	public SoundEffects() {
		isOn = true;
		final String explosionFile = ""; // TODO
		final Media explosion = new Media(new File(explosionFile).toURI().toString());
		expPlayer = new MediaPlayer(explosion);

		final String shootFile = ""; // TODO
		final Media shoot = new Media(new File(shootFile).toURI().toString());
		shootPlayer = new MediaPlayer(shoot);

		final String powerFile = ""; // TODO
		final Media power = new Media(new File(powerFile).toURI().toString());
		powerPlayer = new MediaPlayer(power);
	}

	/**
	 * Set whether the sound effects play or not
	 *
	 * @param bool
	 */
	public void setOn(final boolean bool) {
		isOn = bool;
	}

	public void playExplosion() {
		if (isOn) {
			expPlayer.play();
		}
	}

	public void playShoot() {
		if (isOn) {
			shootPlayer.play();
		}
	}

	public void playPowerUp() {
		if (isOn) {
			powerPlayer.play();
		}
	}

}
