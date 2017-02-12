package teamproject.audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class for playing sound effects
 * 
 * @author Rose Kirtley
 *
 */
public class SoundEffects {
	
	private boolean isOn;
	private MediaPlayer explosionPlayer;
	private MediaPlayer shootPlayer;
	private MediaPlayer powerUpPlayer;
	
	public SoundEffects(){
		isOn = true;
		
		String explosionFile = "src/teamproject/audio/explosion.mp3";
		Media explosion = new Media(new File(explosionFile).toURI().toString());
		explosionPlayer = new MediaPlayer(explosion);
		
		String shootFile = "src/teamproject/audio/gun.mp3";
		Media shoot = new Media(new File(shootFile).toURI().toString());
		shootPlayer = new MediaPlayer(shoot);
		
		String powerUpFile = "src/teamproject/audio/powerup.mp3";
		Media powerUp = new Media(new File(powerUpFile).toURI().toString());
		powerUpPlayer = new MediaPlayer(powerUp);
	}
	
	/**
	 * Set whether the sound effects play or not
	 * @param bool
	 */
	public void setOn(boolean bool){
		isOn = bool;
	}
	
	public void playExplosion(){
		if(isOn){
			explosionPlayer.seek(Duration.ZERO);
			explosionPlayer.play();
		}
	}
	
	public void playShoot(){
		if(isOn){
			shootPlayer.seek(Duration.ZERO);
			shootPlayer.play();
		}
	}

	public void playPowerUp(){
		if(isOn){
			powerUpPlayer.seek(Duration.ZERO);
			powerUpPlayer.play();
		}
	}
	
}
