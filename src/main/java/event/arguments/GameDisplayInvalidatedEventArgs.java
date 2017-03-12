package teamproject.event.arguments;

import teamproject.gamelogic.core.GameLogic;

public class GameDisplayInvalidatedEventArgs {
	private GameLogic logic;
	
	public GameDisplayInvalidatedEventArgs(GameLogic logic) {
		this.logic = logic;
	}
	
	public GameLogic getLogic() {
		return logic;
	}
}
