package main.java.ui;

/**
 * Setting screen when in game
 * @author Rose Kirtley
 *
 */
public class SettingsScreenGame extends SettingsScreen {

	public SettingsScreenGame(GameUI game) {
		super(game);
		returnButton.setOnAction(e -> game.returnBackFromGame());
	}

	@Override
	public void makeSelection() {
		game.returnBackFromGame();	
	}

}
