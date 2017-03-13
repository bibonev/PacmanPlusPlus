package main.java.event.arguments;

import main.java.constants.GameOutcome;

public class RemoteGameEndedEventArgs {
	private GameOutcome outcome;

	public RemoteGameEndedEventArgs(final GameOutcome outcome) {
		this.outcome = outcome;
	}

	public GameOutcome getOutcome() {
		return outcome;
	}
}
