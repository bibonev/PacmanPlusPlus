package main.java.event.arguments;

import main.java.gamelogic.core.GameLogic;

public class GameDisplayInvalidatedEventArgs {
	private GameLogic logic;

	public GameDisplayInvalidatedEventArgs(final GameLogic logic) {
		this.logic = logic;
	}

	public GameLogic getLogic() {
		return logic;
	}
}
