package teamproject.event.arguments.container;

import teamproject.constants.GameType;
import teamproject.gamelogic.domain.GameSettings;

public class NewGameRequestedEventArguments {

	private GameType type;
	private GameSettings settings;
	private String userName;

	public NewGameRequestedEventArguments(final GameType type, final GameSettings settings, final String userName) {
		this.type = type;
		this.settings = settings;
		this.userName = userName;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public GameType getType() {
		return type;
	}

	public String getUserName() {
		return userName;
	}

}
