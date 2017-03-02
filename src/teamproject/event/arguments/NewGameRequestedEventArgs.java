package teamproject.event.arguments;

import javafx.stage.Stage;
import teamproject.constants.GameType;
import teamproject.gamelogic.domain.GameSettings;

public class NewGameRequestedEventArgs {

	private GameType type;
	private GameSettings settings;
	private String userName;
	private Stage stage;

	public NewGameRequestedEventArgs(final GameType type, final GameSettings settings, final String userName, final Stage stage) {
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
