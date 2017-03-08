package teamproject.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
	
	private BorderPane bPane;
	private Button play;
	private Button leaveGame;
	private Button settings;
	private Label label;	
	
	public PlayersList list;
	private GameSettings gameSettings;
	
	private Event<UserLeavingLobbyListener, Object> userLeavingLobbyEvent;
	private Event<HostStartingMultiplayerGameListener, Object> hostStartingGameListener;

	public MultiPlayerLobbyScreen(GameUI game){
		super(game);
		
		gameSettings = new GameSettings();
		
        play = new Button("Start Game");
        play.getStyleClass().add("buttonStyle");
        play.setOnAction(e-> play());
        
        leaveGame = new Button("Leave game");
        leaveGame.getStyleClass().add("buttonStyle");
        leaveGame.setOnAction(e-> leaveGame());
        
        settings = new Button("Change Game Settings");
        settings.getStyleClass().add("buttonStyle");
        settings.setOnAction(e -> showSettings());
        
        FlowPane buttons = new FlowPane();

        buttons.setPadding(new Insets(5, 5, 5, 5));
        buttons.setVgap(4);
        buttons.setHgap(4);
        buttons.setOrientation(Orientation.VERTICAL);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.getStyleClass().add("paneStyle");
        buttons.getChildren().addAll(play, leaveGame, settings);
        
        label = new Label("Multiplayer");
    	label.getStyleClass().add("labelStyle");
		
    	bPane = new BorderPane();
    	bPane.setTop(label);
    	bPane.setLeft(buttons);
   	    pane.getChildren().add(bPane);
	    
	    userLeavingLobbyEvent = new Event<>((l, a) -> l.onUserLeavingLobby());
	    userLeavingLobbyEvent.addListener(this);
	    
	    hostStartingGameListener = new Event<>((l, a) -> l.onHostStartingGame(
	    		new HostStartingMultiplayerGameEventArgs(getMultiplayerSettings())));
	    
	    addNames();
	}

	public void showSettings(){
		System.out.println("settings"); //TODO 
	}
	
	public void setMultiplayerSettings(GameSettings newSettings){
		gameSettings = newSettings;
	}
	
	public GameSettings getMultiplayerSettings() {
		// TODO Rose: when we get round to adding game settings that we need
		// to change, add a screen to let you edit game settings for multi
		// player games from this lobby screen - then make this method look
		// at the choices the player has made and return a GameSettings
		// object from it
		
		// temporary implementation
		return gameSettings;
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
		bPane.setCenter(list.getPane());
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
