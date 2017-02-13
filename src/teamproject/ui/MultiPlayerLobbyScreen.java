package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import teamproject.event.Event;

/**
 * Screen for the lobby of a multiplayer game
 * 
 * @author Rose Kirtley
 *
 */
public class MultiPlayerLobbyScreen extends Screen {
	
	private Button play;
	private Button leaveGame;
	private Label label;	
	private Pane names;

	public MultiPlayerLobbyScreen(GameUI game){
		super(game);
		
        play = new Button("Play!");
        play.getStyleClass().add("buttonStyle");
        play.setOnAction(e-> play());
        
        leaveGame = new Button("Leave game");
        leaveGame.getStyleClass().add("buttonStyle");
        leaveGame.setOnAction(e-> leaveGame());
                
        label = new Label("Multiplayer");
    	label.getStyleClass().add("labelStyle");
		
	    pane.getChildren().addAll(label, play, leaveGame);
	}
	
	private void play(){
		pane.getChildren().remove(names);
		game.startNewMultiPlayerGame();
	}
	
	private void leaveGame(){
		//fire event for leaving a multiplayer game
		pane.getChildren().remove(names);
		game.switchToMenu();
	}
	
	public void addNames(){
		names = new PlayersList(game).getPane();
		pane.getChildren().add(names);
	}
}
