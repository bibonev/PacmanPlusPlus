package main.java.ui;

/**
 * Setting screen from menu
 * @author Rose Kirtley
 *
 */
public class SettingsScreenMenu extends SettingsScreen {

	public SettingsScreenMenu(GameUI game) {
		super(game);
		returnButton.setOnAction(e -> game.returnBackFromMenu());
	}

	@Override
	public void makeSelection() {
		game.returnBackFromMenu();	
	}

}
