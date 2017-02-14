package teamproject.event.arguments.container;

import java.util.Map;

import teamproject.constants.GameSetting;

public class GameSettingsChangedEventArguments {

	private Map<GameSetting, Boolean> newGameSettings;

	// TODO: replace Boolean with something more abstract
	public GameSettingsChangedEventArguments(final Map<GameSetting, Boolean> newGameSettings) {
		this.newGameSettings = newGameSettings;
	}

	public Map<GameSetting, Boolean> getNewGameSettings() {
		return newGameSettings;
	}
}
