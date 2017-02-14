package teamproject.event.arguments.container;

import javafx.stage.Stage;
import teamproject.constants.GameType;
import teamproject.gamelogic.domain.GameSettings;

public class NewGameRequestedEventArguments {

	private GameType type;
	private GameSettings settings;
	private String userName;
	private Stage stage;

	public NewGameRequestedEventArguments(final GameType type, final GameSettings settings, final String userName, final Stage stage) {
		this.type = type;
		this.settings = settings;
		this.userName = userName;
		this.stage = stage;
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

	public Stage getStage(){
		return stage;
	}
}
