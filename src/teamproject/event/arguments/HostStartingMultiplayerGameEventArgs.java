package teamproject.event.arguments;

import teamproject.gamelogic.domain.GameSettings;

public class HostStartingMultiplayerGameEventArgs {
	private GameSettings settings;
	
	public GameSettings getSettings() {
		return settings;
	}

	public HostStartingMultiplayerGameEventArgs(GameSettings settings) {
		this.settings = settings;
	}
}
