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
public class DisabledMusic implements Music {
	@Override
	public void setOn(boolean bool) {}

	@Override
	public void playMusic() {}

	@Override
	public void stopMusic() {}
}
