package main.java.event.arguments;

import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.Game;

public class ReadyToStartEventArgs {
	private Game game;
	private GameLogic logic;
	public ReadyToStartEventArgs(Game game, GameLogic logic) {
		super();
		this.game = game;
		this.logic = logic;
	}
	
	public Game getGame() {
		return game;
	}
	
	public GameLogic getLogic() {
		return logic;
	}
}
