package teamproject.gamelogic.core;

import teamproject.constants.CellState;
import teamproject.constants.GameOutcome;
import teamproject.constants.GameOutcomeType;
import teamproject.constants.GameType;
import teamproject.event.Event;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.event.listener.GameEndedListener;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.Ghost;

public class LocalGameLogic implements GameLogic {
	private Game game;
	private Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> onGameDisplayInvalidated;
	private Event<GameEndedListener, GameEndedEventArgs> onGameEnded;
	
	public LocalGameLogic(Game game) {
		this.game = game;
		this.onGameDisplayInvalidated = new Event<>((l, a) -> l.onGameDisplayInvalidated(a));
		this.onGameEnded = new Event<>((l, a) -> l.onGameEnded(a));
	}
	
	public Game getGame() {
		return game;
	}
	
	public void gameStep(int period) {
		if(!game.hasEnded()) {
			checkEndingConditions();
			game.getWorld().getMap().gameStep(game);
			for(Entity entity : game.getWorld().getEntities()){
				entity.gameStep(game);
				checkEndingConditions();
			}
			checkEndingConditions();
			invalidateDisplay();
		}
	}
	
	private boolean ghostsEatenPlayers() {
		if(game.getGameType() == GameType.SINGLEPLAYER) {
			for(final Ghost ghost : game.getWorld().getGhosts()) {
				if(ghost.getPosition().equals(game.getPlayer().getPosition())) {
					return true;
				}
			}
			
			return false;
		} else if(game.getGameType() == GameType.MULTIPLAYER_SERVER) {
			// what to do for this for multiplayer?
			// game over when one player dies or when all players die?
			return false;
		} else {
			return false;
		}
	}
	
	private boolean allFoodEaten() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		for (final Cell[] cellRow : cells) {
			for (int j = 0; j < cells[0].length; j++) {
				if(cellRow[j].getState().equals(CellState.FOOD)) {
					return false;
				}
			}
		}
		
		return true;
	}

	public void checkEndingConditions() {
		if(ghostsEatenPlayers()) {
			GameOutcome outcome = new GameOutcome(GameOutcomeType.GHOSTS_WON);
			onGameEnded(outcome);
		}
		
		if(allFoodEaten()) {
			GameOutcome outcome = new GameOutcome(GameOutcomeType.PLAYER_WON, game.getPlayer());
			onGameEnded(outcome);
		}
	}
	
	private void invalidateDisplay() {
		this.onGameDisplayInvalidated.fire(new GameDisplayInvalidatedEventArgs(this));
	}
	
	private void onGameEnded(GameOutcome outcome) {
		if(!this.game.hasEnded()) {
			this.game.setEnded();
			this.onGameEnded.fire(new GameEndedEventArgs(this, outcome));
		}
	}
	
	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated() {
		return onGameDisplayInvalidated;
	}

	@Override
	public Event<GameEndedListener, GameEndedEventArgs> getOnGameEnded() {
		return onGameEnded;
	}
}
