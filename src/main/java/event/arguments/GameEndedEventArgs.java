package main.java.event.arguments;

import main.java.constants.GameOutcome;
import main.java.gamelogic.core.GameLogic;

public class GameEndedEventArgs {
	private GameLogic logic;
	private GameOutcome outcome;

	public GameEndedEventArgs(final GameLogic logic, final GameOutcome outcome) {
		this.logic = logic;
		this.outcome = outcome;
	}

	public GameLogic getLogic() {
		return logic;
	}

	public GameOutcome getOutcome() {
		return outcome;
	}
}
