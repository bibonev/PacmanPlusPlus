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
public class DefaultMusic implements Music {
	
	private boolean isOn;
	private MediaPlayer mediaPlayer;
	private String musicFile = "src/main/java/audio/musicShort.mp3"; // could
																		// change
																		// to
																		// longer
																		// version

	public DefaultMusic() {
		try{
		isOn = true;
		String path = new File(musicFile).toURI().toString();
		final Media sound = new Media(path);
		mediaPlayer = new MediaPlayer(sound);

		mediaPlayer.setOnEndOfMedia(() -> {
			if (isOn) {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		}catch(MediaException e) {
			throw e;
			//e.printStackTrace();
		}
	}

	/**
	 * Set whether the background music is playing or not
	 * 
	 * @param bool,
	 *            true for music to play, false to turn music off
	 */
	@Override
	public void setOn(final boolean bool) {
		isOn = bool;
	}

	@Override
	public void playMusic() {
		if (isOn) {
			mediaPlayer.play();
		}
	}

	@Override
	public void stopMusic() {
		if (isOn) {
			mediaPlayer.stop();
		}
	}

}
