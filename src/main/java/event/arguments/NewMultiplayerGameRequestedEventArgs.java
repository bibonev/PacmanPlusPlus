package main.java.event.arguments;

import javafx.stage.Stage;
import main.java.gamelogic.domain.GameSettings;

public class NewMultiplayerGameRequestedEventArgs {

	private String userName;
	private GameSettings settings;
	private Stage stage;

	public NewMultiplayerGameRequestedEventArgs(final String userName, final GameSettings settings, final Stage stage) {
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
