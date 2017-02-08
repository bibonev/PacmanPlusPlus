package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Screen for single player lobby
 * 
 * @author Rose Kirtley
 *
 */
public class SinglePlayerLobbyScreen extends AbstractScreen {
	
	private Button play;
	private Label label;

	public SinglePlayerLobbyScreen(GameUI game){
		super(game);
		
		play = new Button("Play!");
        setUpButton(play);
        play.setOnAction(e-> game.switchToGame());
        
        label = new Label("Single Player");
        label.setStyle(labelStyle);
		
	    pane.getChildren().addAll(label, play);
	}
}
