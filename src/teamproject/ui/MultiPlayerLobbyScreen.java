package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Screen for the lobby of a multiplayer game
 * 
 * @author Rose Kirtley
 *
 */
public class MultiPlayerLobbyScreen extends AbstractScreen {
	
	private Button play;
	private Button leaveGame;
	private Label label;	
	private Pane names;

	public MultiPlayerLobbyScreen(GameUI game){
		super(game);
		
        play = new Button("Play!");
        setUpButton(play);
        play.setOnAction(e-> play());
        
        leaveGame = new Button("Leave game");
        setUpButton(leaveGame);
        leaveGame.setOnAction(e-> leaveGame());
                
        label = new Label("Multiplayer");
        label.setStyle(labelStyle);
		
	    pane.getChildren().addAll(label, play, leaveGame);
	}
	
	private void play(){
		//fire event to start playing a game
		pane.getChildren().remove(names);
		game.switchToGame();
	}
	
	private void leaveGame(){
		//fire event for leaving a game
		pane.getChildren().remove(names);
		game.switchToMenu();
	}
	
	public void addNames(){
		names = new PlayersList(game).getPane();
		pane.getChildren().add(names);
	}
}
