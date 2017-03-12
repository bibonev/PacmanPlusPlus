package main.java.constants;

import main.java.gamelogic.domain.Player;

/*
 * Single player or multiplayer game outcomes
 */
public class GameOutcome {
	private GameOutcomeType type;
	private Player winner;

	public GameOutcome(final GameOutcomeType type) {
		if (type != GameOutcomeType.PLAYER_WON) {
			this.type = type;
			winner = null;
		} else {
			throw new IllegalArgumentException("Must provide winning player with PLAYER_WON outcome type.");
		}
	}

	public GameOutcome(final GameOutcomeType type, final Player winner) {
		if (type == GameOutcomeType.PLAYER_WON) {
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
