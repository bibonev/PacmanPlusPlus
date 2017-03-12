package main.java.event.arguments;

import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.Game;

public class GameStartedEventArgs {
	private Game game;
	private GameLogic gameLogic;

	public GameStartedEventArgs(final Game game, final GameLogic gameLogic) {
		this.game = game;
		this.gameLogic = gameLogic;

	}

	public Game getGame() {
		return game;
	}

	public GameLogic getGameLogic() {
		return gameLogic;
	}
}
