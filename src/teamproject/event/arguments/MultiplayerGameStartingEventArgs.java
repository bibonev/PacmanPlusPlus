package teamproject.event.arguments;

import teamproject.gamelogic.domain.GameSettings;

public class MultiplayerGameStartingEventArgs extends GameStartingEventArgs {
	public MultiplayerGameStartingEventArgs(GameSettings settings, String username) {
		super(settings, username);
	}
}
