package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Screen for single player lobby
 * 
 * @author Rose Kirtley
 *
 */
public class SinglePlayerLobbyScreen extends Screen {
	
	private Button play;
	private Label label;

	public SinglePlayerLobbyScreen(GameUI game){
		super(game);
		
		play = new Button("Play!");
        play.getStyleClass().add("buttonStyle");
        play.setOnAction(e-> game.switchToGame());
        
        label = new Label("Single Player");
        label.getStyleClass().add("labelStyle");
		
	    pane.getChildren().addAll(label, play);
	}
}
