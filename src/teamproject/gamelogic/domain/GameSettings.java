package teamproject.gamelogic.domain;

public class GameSettings {

	private boolean musicOn;
	private boolean soundsOn;

	public GameSettings(final boolean musicOn, final boolean soundsOn) {
		this.musicOn = musicOn;
		this.soundsOn = soundsOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public boolean isSoundsOn() {
		return soundsOn;
	}

	public void setMusicOn(final boolean musicOn) {
		this.musicOn = musicOn;
	}

	public void setSoundsOn(final boolean soundsOn) {
		this.soundsOn = soundsOn;
	}

}
