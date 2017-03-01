package teamproject.event.arguments;

import javafx.stage.Stage;
import teamproject.gamelogic.domain.GameSettings;

public class NewMultiplayerGameRequestedEventArguments {

	private String userName;
	private GameSettings settings;
	private Stage stage;

	public NewMultiplayerGameRequestedEventArguments(final String userName, final GameSettings settings,
			final Stage stage) {
		this.userName = userName;
		this.settings = settings;
		this.stage = stage;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public String getUserName() {
		return userName;
	}

	public Stage getStage() {
		return stage;
	}

}
