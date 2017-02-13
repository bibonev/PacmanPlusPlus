package teamproject.event.arguments.container;

import teamproject.constants.PauseGameType;

public class GamePausedEventArguments {
	
	private String username;
	private PauseGameType type;
	
	public GamePausedEventArguments(String username, PauseGameType type){
		this.username = username;
		this.type = type;
	}
	
	public String getUsername(){
		return username;
	}
	
	public PauseGameType getType(){
		return type;
	}

}
