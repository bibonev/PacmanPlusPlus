package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import teamproject.event.Event;
import teamproject.event.arguments.HostStartingMultiplayerGameEventArgs;
import teamproject.event.listener.HostStartingMultiplayerGameListener;
import teamproject.event.listener.UserLeavingLobbyListener;
import teamproject.gamelogic.domain.GameSettings;

/**
 * Screen for the lobby of a multiplayer game
 * 
 * @author Rose Kirtley
 *
 */
public class MultiPlayerLobbyScreen extends Screen implements UserLeavingLobbyListener {
	
	private Button play;
	private Button leaveGame;
	private Label label;	
	public PlayersList list;
	private Event<UserLeavingLobbyListener, Object> userLeavingLobbyEvent;
	private Event<HostStartingMultiplayerGameListener, Object> hostStartingGameListener;

	public MultiPlayerLobbyScreen(GameUI game){
		super(game);
		
        play = new Button("Start Game");
        play.getStyleClass().add("buttonStyle");
        play.setOnAction(e-> play());
        
        leaveGame = new Button("Leave game");
        leaveGame.getStyleClass().add("buttonStyle");
        leaveGame.setOnAction(e-> leaveGame());
                
        label = new Label("Multiplayer");
    	label.getStyleClass().add("labelStyle");
		
	    pane.getChildren().addAll(label, play, leaveGame);
	    
	    userLeavingLobbyEvent = new Event<>((l, a) -> l.onUserLeavingLobby());
	    userLeavingLobbyEvent.addListener(this);
	    
	    hostStartingGameListener = new Event<>((l, a) -> l.onHostStartingGame(
	    		new HostStartingMultiplayerGameEventArgs(getMultiplayerSettings())));
	    
	    addNames();
	}

	
	public GameSettings getMultiplayerSettings() {
		// TODO Rose: when we get round to adding game settings that we need
		// to change, add a screen to let you edit game settings for multi
		// player games from this lobby screen - then make this method look
		// at the choices the player has made and return a GameSettings
		// object from it
		
		// temporary implementation
		return new GameSettings();
	}
	
	private void play(){
		// pane.getChildren().remove(list.getPane());
		getHostStartingGameListener().fire(null);
	}
	
	public void setStartGameEnabled(boolean enabled) {
		play.setDisable(!enabled);
	}
	
	private void leaveGame(){
		//fire event for leaving a multiplayer game
		// pane.getChildren().remove(list.getPane());
		game.switchToMenu();
		userLeavingLobbyEvent.fire(null);
	}
	
	public void addNames(){
		list = new PlayersList(game);
		pane.getChildren().add(list.getPane());
	}
	
	public Event<UserLeavingLobbyListener, Object> getUserLeavingLobbyEvent() {
		return userLeavingLobbyEvent;
	}
	
	public Event<HostStartingMultiplayerGameListener, Object> getHostStartingGameListener() {
		return hostStartingGameListener;
	}

	@Override
	public void onUserLeavingLobby() {
		list.clear();
	}
}
