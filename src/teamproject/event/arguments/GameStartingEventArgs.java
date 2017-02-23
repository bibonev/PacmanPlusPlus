package teamproject.event.arguments;

import teamproject.gamelogic.domain.GameSettings;

public class GameStartingEventArgs {
	private GameSettings settings;
	private String username;
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public String getUsername() {
		return username;
	}
	
	public GameStartingEventArgs(GameSettings settings, String username) {
		this.settings = settings;
		this.username = username;
	}
}
