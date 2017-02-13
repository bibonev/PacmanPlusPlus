package teamproject.event.arguments.container;

import java.util.Map;

import teamproject.constants.GameSetting;

public class GameSettingsChangedEventArguments {

	private Map<GameSetting, Boolean> newGameSettings;

	public GameSettingsChangedEventArguments(final Map<GameSetting, Boolean> newGameSettings) {
		this.newGameSettings = newGameSettings;
	}

	public Map<GameSetting, Boolean> getNewGameSettings() {
		return newGameSettings;
	}
}
