package main.java.event.arguments;

import main.java.constants.GameStateType;

public class GameStateChangedEventArgs {
	private int playerID;
	private GameStateType type;

	public GameStateChangedEventArgs(final int playerID, final GameStateType type) {
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
