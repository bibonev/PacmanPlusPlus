package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import teamproject.event.Event;
import teamproject.event.arguments.SingleplayerGameStartingEventArgs;
import teamproject.event.listener.SingleplayerGameStartingListener;
import teamproject.gamelogic.domain.GameSettings;

/**
 * Screen for single player lobby
 * 
 * @author Rose Kirtley
 *
 */
public class SinglePlayerLobbyScreen extends Screen {
	
	private Button play;
	private Label label;
	private Button back;
	private Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> onStartingSingleplayerGame;

	public SinglePlayerLobbyScreen(GameUI game){
		super(game);
		
		play = new Button("Play!");
        play.getStyleClass().add("buttonStyle");
        play.setOnAction(e-> getOnStartingSingleplayerGame().fire(
        		new SingleplayerGameStartingEventArgs(getSinglePlayerSettings(), game.getName())));
        
        label = new Label("Single Player");
        label.getStyleClass().add("labelStyle");
		
        back = new Button("Back");
        back.getStyleClass().add("buttonStyle");
		back.setOnAction(e -> game.switchToMenu());
        
	    pane.getChildren().addAll(label, play, back);
	    
	    onStartingSingleplayerGame = new Event<>((l, s) -> l.onSingleplayerGameStarting(s));
	}
	
	public GameSettings getSinglePlayerSettings() {
		// TODO Rose: when we get round to adding game settings that we need
		// to change, add a screen to let you edit game settings for single
		// player games from this lobby screen - then make this method look
		// at the choices the player has made and return a GameSettings
		// object from it
		
		// temporary implementation
		return new GameSettings();
	}
	
	public Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> getOnStartingSingleplayerGame() {
		return onStartingSingleplayerGame;
	}
}
