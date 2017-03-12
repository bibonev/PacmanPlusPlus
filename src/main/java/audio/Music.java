package main.java.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class for playing background music
 *
 * @author Rose Kirtley
 *
 */
public class Music {

	private boolean isOn;
	private MediaPlayer mediaPlayer;
	private String musicFile = "src/teamproject/audio/musicShort.mp3"; // could
																		// change
																		// to
																		// longer
																		// version

	public Music() {
		isOn = true;
		final Media sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);

		mediaPlayer.setOnEndOfMedia(() -> {
			if (isOn) {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
	}

	/**
	 * Set whether the background music is playing or not
	 * 
	 * @param bool,
	 *            true for music to play, false to turn music off
	 */
	public void setOn(final boolean bool) {
		isOn = bool;
	}

	public void playMusic() {
		if (isOn) {
			mediaPlayer.play();
		}
	}

	public void stopMusic() {
		if (isOn) {
			mediaPlayer.stop();
		}
	}

}
