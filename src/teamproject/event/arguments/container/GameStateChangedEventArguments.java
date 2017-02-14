package teamproject.event.arguments.container;

import teamproject.constants.GameStateType;

public class GameStateChangedEventArguments {
	
	private String username;
	private GameStateType type;
	
	public GameStateChangedEventArguments(String username, GameStateType type){
		this.username = username;
		this.type = type;
	}
	
	public String getUsername(){
		return username;
	}
	
	public GameStateType getType(){
		return type;
	}

}
