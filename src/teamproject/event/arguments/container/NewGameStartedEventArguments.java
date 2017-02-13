package teamproject.event.arguments.container;

import javafx.stage.Stage;
import teamproject.gamelogic.domain.Game;

public class NewGameStartedEventArguments {

	private Game game;
	private Stage stage;

	public NewGameStartedEventArguments(final Game game, final Stage stage) {
		this.game = game;
		this.stage = stage;
	}

	public Game getGame() {
		return game;
	}

	public Stage getStage() {
		return stage;
	}

}
