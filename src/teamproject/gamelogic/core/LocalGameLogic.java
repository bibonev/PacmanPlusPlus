package teamproject.gamelogic.core;

import teamproject.event.Event;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.gamelogic.domain.Game;

public class LocalGameLogic implements GameLogic {
	private Game game;
	private Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> onGameDisplayInvalidated;
	
	public LocalGameLogic(Game game) {
		this.game = game;
		this.onGameDisplayInvalidated = new Event<>((l, a) -> l.onGameDisplayInvalidated(a));
	}
	
	public Game getGame() {
		return game;
	}
	
	public void gameStep(int period) {
		game.getWorld().gameStep(game);
		checkGameEnding();
		invalidateDisplay();
	}

	private void checkGameEnding() {
		/* if(RuleChecker.getGameOutcome(game)
				== GameOutcome.LOCAL_PLAYER_LOST){
			gameEnded();
		} else if(RuleChecker.getGameOutcome(game)
				== GameOutcome.LOCAL_PLAYER_WON){
			gameEnded();
		} */
	}
	
	private void invalidateDisplay() {
		this.onGameDisplayInvalidated.fire(new GameDisplayInvalidatedEventArgs(this));
	}
	
	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated() {
		return onGameDisplayInvalidated;
	}
}
