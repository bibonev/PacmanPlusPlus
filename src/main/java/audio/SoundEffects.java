package main.java.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.java.constants.GameOutcomeType;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameStartedEventArgs;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.GameStartedListener;
import main.java.ui.GameUI;

/**
 * Class for playing sound effects
 *
 * @author Rose Kirtley
 *
 */
public class SoundEffects implements GameEndedListener, GameStartedListener {
	
	private GameUI game;

	private boolean isOn;
	private MediaPlayer winPlayer;
	private MediaPlayer lostPlayer;
	private MediaPlayer startPlayer;
//	private MediaPlayer expPlayer;
//	private MediaPlayer shootPlayer;
//	private MediaPlayer powerPlayer;

	public SoundEffects(GameUI game) {
		this.game = game;
		isOn = true;
//		final String explosionFile = ""; // TODO
//		final Media explosion = new Media(new File(explosionFile).toURI().toString());
//		expPlayer = new MediaPlayer(explosion);

//		final String shootFile = ""; // TODO
//		final Media shoot = new Media(new File(shootFile).toURI().toString());
//		shootPlayer = new MediaPlayer(shoot);

//		final String powerFile = ""; // TODO
//		final Media power = new Media(new File(powerFile).toURI().toString());
//		powerPlayer = new MediaPlayer(power);
		
		final String winFile = "src/main/java/audio/win.mp3";
		final Media win = new Media(new File(winFile).toURI().toString());
		winPlayer = new MediaPlayer(win);
		
		final String lostFile = "src/main/java/audio/lost.mp3";
		final Media lost = new Media(new File(lostFile).toURI().toString());
		lostPlayer = new MediaPlayer(lost);
		
		final String startFile = "src/main/java/audio/start.mp3";
		final Media start = new Media(new File(startFile).toURI().toString());
		startPlayer = new MediaPlayer(start);
	}

	/**
	 * Set whether the sound effects play or not
	 *
	 * @param bool
	 */
	public void setOn(final boolean bool) {
		isOn = bool;
	}

//	public void playExplosion() {
//		if (isOn) {
//			expPlayer.play();
//		}
//	}

//	public void playShoot() {
//		if (isOn) {
//			shootPlayer.play();
//		}
//	}

//	public void playPowerUp() {
//		if (isOn) {
//			powerPlayer.play();
//		}
//	}

	@Override
	public void onGameEnded(GameEndedEventArgs args) {
		System.out.println("game eneded");
		if (isOn) {
			if(args.getOutcome().getOutcomeType() == GameOutcomeType.PLAYER_WON &&
					args.getOutcome().getWinner().getName()==game.getName()){
				winPlayer.play();
			}else{
				lostPlayer.play();
			}
		}		
	}

	@Override
	public void onGameStarted(GameStartedEventArgs args) {
		System.out.println("game started");
		if (isOn) {
			startPlayer.play();
		}
	}

}
