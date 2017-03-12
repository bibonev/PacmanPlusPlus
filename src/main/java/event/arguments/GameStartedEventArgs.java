package teamproject.event.arguments;

import teamproject.gamelogic.core.GameLogic;
import teamproject.gamelogic.domain.Game;

public class GameStartedEventArgs {
	private Game game;
	private GameLogic gameLogic;
	
	public GameStartedEventArgs(Game game, GameLogic gameLogic) {
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
