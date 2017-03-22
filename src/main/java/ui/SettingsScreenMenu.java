package main.java.ui;

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
