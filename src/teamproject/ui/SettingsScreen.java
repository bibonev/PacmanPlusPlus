package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

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
        returnButton.getStyleClass().add("buttonStyle");
   		returnButton.setOnAction(e-> game.returnBack());
   		
   		music = new CheckBox("Music");
   		music.getStyleClass().add("labelStyle");
   		music.setSelected(true);
   		
   		sounds = new CheckBox("Sound effects");
   		sounds.getStyleClass().add("labelStyle");
   		sounds.setSelected(true);
        
   		label = new Label("Settings");
   		label.getStyleClass().add("titleStyle");
		
	    pane.getChildren().addAll(label, returnButton, music, sounds);
	}	
}
