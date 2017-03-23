package main.java.event.arguments;

import java.util.Map;

import main.java.gamelogic.domain.GameSettings;

public class GameSettingsChangedEventArgs {
	private GameSettings settings;
	
	public GameSettingsChangedEventArgs(GameSettings settings){
		this.settings = settings;
	}
	
	public GameSettings getSettings(){
		return settings;
	}
}
