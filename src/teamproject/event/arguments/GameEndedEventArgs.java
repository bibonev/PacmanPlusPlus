package teamproject.event.arguments;

import teamproject.constants.GameOutcome;
import teamproject.gamelogic.core.GameLogic;

public class GameEndedEventArgs {
	private GameLogic logic;
	private GameOutcome outcome;
	
	public GameEndedEventArgs(GameLogic logic, GameOutcome outcome) {
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
