package main.java.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameStartedEventArgs;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.GameStartedListener;

/**
 * Class for playing background music
 *
 * @author Rose Kirtley
 *
 */
public class DisabledMusic implements Music, GameStartedListener, GameEndedListener {
	@Override
	public void setOn(boolean bool) {}

	@Override
	public void playMusic() {}

	@Override
	public void stopMusic() {}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {}

	@Override
	public void onGameStarted(GameStartedEventArgs args) {}
}
