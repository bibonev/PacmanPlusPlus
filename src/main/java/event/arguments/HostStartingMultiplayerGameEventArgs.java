package main.java.event.arguments;

import main.java.gamelogic.domain.GameSettings;

public class HostStartingMultiplayerGameEventArgs {
	private GameSettings settings;

	public GameSettings getSettings() {
		return settings;
	}

	public HostStartingMultiplayerGameEventArgs(final GameSettings settings) {
		this.settings = settings;
	}
}
