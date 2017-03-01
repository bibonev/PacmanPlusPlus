package teamproject.event.arguments;

import teamproject.constants.GameStateType;

public class GameStateChangedEventArguments {
	private int playerID;
	private GameStateType type;
	
	public GameStateChangedEventArguments(int playerID, GameStateType type){
		this.playerID = playerID;
		this.type = type;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public GameStateType getType() {
		return type;
	}

}
