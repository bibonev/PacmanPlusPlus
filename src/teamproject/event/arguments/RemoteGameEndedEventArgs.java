package teamproject.event.arguments;

import teamproject.constants.GameOutcome;
import teamproject.gamelogic.core.GameLogic;

public class RemoteGameEndedEventArgs {
	private GameOutcome outcome;
	
	public RemoteGameEndedEventArgs(GameOutcome outcome) {
		this.outcome = outcome;
	}
	
	public GameOutcome getOutcome() {
		return outcome;
	}
}
