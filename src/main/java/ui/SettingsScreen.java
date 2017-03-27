package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

/**
 * Abstract screen 'overlay' for the client settings, always able to go back to the
 * previous screen
 *
 * @author Rose Kirtley
 *
 */
public abstract class SettingsScreen extends Screen {

	public Button returnButton;
	private Label label;
	private CheckBox music;
	private CheckBox sounds;

	public SettingsScreen(final GameUI game) {
		super(game);

		returnButton = new Button("Return");
		returnButton.getStyleClass().add("backButtonStyle");

		music = new CheckBox("Music");
		music.getStyleClass().add("check-box");
		music.setSelected(true);
		music.selectedProperty().addListener(e -> game.muteMusic(music.isSelected()));

		sounds = new CheckBox("Sound effects");
		sounds.getStyleClass().add("check-box");
		sounds.setSelected(true);
		sounds.selectedProperty().addListener(e -> game.muteSounds(sounds.isSelected()));

		label = new Label("Settings");
		label.getStyleClass().add("miniTitleStyle");

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");

		pane.getChildren().addAll(label, separator, music, sounds, returnButton);
	}
	
	public void selectMusic(boolean b){
		music.setSelected(b);
	}
	
	public void selectSounds(boolean b){
		sounds.setSelected(b);
	}

	@Override
	public void changeSelection(final boolean up) {
		// Only one button
	}

	@Override
	public void unselectAll() {
		// only one button
	}
}
