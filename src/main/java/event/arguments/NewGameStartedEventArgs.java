package main.java.event.arguments;

import javafx.stage.Stage;
import main.java.gamelogic.domain.Game;

public class NewGameStartedEventArgs {

	private Game game;
	private Stage stage;

	public NewGameStartedEventArgs(final Game game, final Stage stage) {
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
