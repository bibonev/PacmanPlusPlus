package main.java.audio;

import java.io.File;

import javafx.animation.Transition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.GameCreatedListener;

/**
 * Class for playing background music
 *
 * @author Rose Kirtley
 *
 */
public class DefaultMusic implements Music, GameCreatedListener, GameEndedListener  {
	
	private boolean isOn;
	private MediaPlayer mediaPlayer;
	private String musicFile = "src/main/java/audio/musicShort.mp3";

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
			new Transition() {
				{
					setCycleDuration(Duration.millis(1500));
				}
				
				@Override
				protected void interpolate(double frac) {
					mediaPlayer.setVolume(frac);
				}
			}.play();
		}
	}

	@Override
	public void stopMusic() {
		if (isOn) {
			mediaPlayer.stop();
		}
	}

	@Override
	public void onGameCreated(GameCreatedEventArgs args) {
		playMusic();
	}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {
		stopMusic();
	}
	
	//Add another listener for game paused
}
