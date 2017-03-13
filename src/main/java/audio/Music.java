package main.java.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class for playing background music
 *
 * @author Rose Kirtley
 *
 */
public interface Music {

	/**
	 * Set whether the background music is playing or not
	 * 
	 * @param bool,
	 *            true for music to play, false to turn music off
	 */
	public void setOn(final boolean bool);

	public void playMusic();

	public void stopMusic();
}
