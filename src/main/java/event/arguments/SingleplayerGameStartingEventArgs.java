package main.java.event.arguments;

import main.java.gamelogic.domain.GameSettings;

public class SingleplayerGameStartingEventArgs {
	private GameSettings settings;
	private String username;

	public GameSettings getSettings() {
		return settings;
	}

	public String getUsername() {
		return username;
	}

	public SingleplayerGameStartingEventArgs(final GameSettings settings, final String username) {
		this.settings = settings;
		this.username = username;
	}
}
