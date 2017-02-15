package teamproject.event.arguments.container;

import teamproject.gamelogic.domain.GameSettings;

public class NewMultiplayerGameRequestedEventArguments {

	private String userName;
	private GameSettings settings;

	public NewMultiplayerGameRequestedEventArguments(final String userName, final GameSettings settings) {
		this.userName = userName;
		this.settings = settings;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public String getUserName() {
		return userName;
	}

}
