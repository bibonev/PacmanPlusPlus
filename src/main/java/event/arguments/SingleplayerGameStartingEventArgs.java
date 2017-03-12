package teamproject.event.arguments;

import teamproject.gamelogic.domain.GameSettings;

public class SingleplayerGameStartingEventArgs {
	private GameSettings settings;
	private String username;
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public String getUsername() {
		return username;
	}
	
	public SingleplayerGameStartingEventArgs(GameSettings settings, String username) {
		this.settings = settings;
		this.username = username;
	}
}
