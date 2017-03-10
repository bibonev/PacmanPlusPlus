package teamproject.constants;

import teamproject.gamelogic.domain.Player;

/*
 * Single player or multiplayer game outcomes
 */
public class GameOutcome {
	private GameOutcomeType type;
	private Player winner;
	
	public GameOutcome(GameOutcomeType type) {
		if(type != GameOutcomeType.PLAYER_WON) {
			this.type = type;
			winner = null;
		} else {
			throw new IllegalArgumentException("Must provide winning player with PLAYER_WON outcome type.");
		}
	}
	
	public GameOutcome(GameOutcomeType type, Player winner) {
		if(type == GameOutcomeType.PLAYER_WON) {
			this.type = type;
			this.winner = winner;
		} else {
			throw new IllegalArgumentException("Can only provide winning player with PLAYER_WON outcome type.");
		}
	}
	
	
	public GameOutcomeType getOutcomeType() {
		return type;
	}
	
	public Player getWinner() {
		return winner;
	}
}
