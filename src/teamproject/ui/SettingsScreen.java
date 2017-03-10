package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

/**
 * Screen 'overlay' for the client settings, always able to go back to the previous screen
 * 
 * @author Rose Kirtley
 *
 */
public class SettingsScreen extends Screen {

	private Button returnButton; 
	private Label label;
	private CheckBox music;
	private CheckBox sounds;

	public SettingsScreen(GameUI game){
		super(game);
		
        returnButton = new Button("Return");
        returnButton.getStyleClass().add("backButtonStyle");
   		returnButton.setOnAction(e-> game.returnBack());
   		
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
   		
   		Separator separator = new Separator();
        separator.getStyleClass().add("separator");
		
	    pane.getChildren().addAll(label, separator , music, sounds, returnButton);
	}	
}
