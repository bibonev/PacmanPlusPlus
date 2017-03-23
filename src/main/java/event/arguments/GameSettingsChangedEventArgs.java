package main.java.event.arguments;

import java.util.Map;

import main.java.constants.GameSetting;
import main.java.gamelogic.domain.GameSettings;

public class GameSettingsChangedEventArgs {

	/*private Map<GameSetting, Boolean> newGameSettings;

	// TODO: replace Boolean with something more abstract
	public GameSettingsChangedEventArgs(final Map<GameSetting, Boolean> newGameSettings) {
		this.newGameSettings = newGameSettings;
	}

	public Map<GameSetting, Boolean> getNewGameSettings() {
		return newGameSettings;
	}*/
	
	private GameSettings settings;
	
	public GameSettingsChangedEventArgs(GameSettings settings){
		this.settings = settings;
	}
	
	public GameSettings getSettings(){
		return settings;
	}
}
