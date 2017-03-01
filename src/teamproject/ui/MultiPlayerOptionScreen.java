package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * Screen for choosing whether to join a multiplayer game or start a new multiplayer game
 * 
 * @author Rose Kirtley
 *
 */
public class MultiPlayerOptionScreen extends Screen {
	
	private Button joinGame;
	private Button startGame;

	public MultiPlayerOptionScreen(GameUI game){
		super(game);
		
        joinGame = new Button("Join game");
        joinGame.getStyleClass().add("buttonStyle");
        joinGame.setTooltip(new Tooltip("Join a game"));
        joinGame.setOnAction(e-> game.switchToMultiPlayerJoin());
		
        startGame = new Button("Start a new game");
        startGame.getStyleClass().add("buttonStyle");
        startGame.setTooltip(new Tooltip("Initiate a new game"));
        startGame.setOnAction(e-> startNewGame());
        
        pane.getChildren().addAll(joinGame, startGame);
	}
	
	private void startNewGame(){
		//fire event to start new game
		//give game game.logInScreen.getName(); for the user name
		game.createNewPendingMultiPlayerGame();
		game.switchToMultiPlayerLobby();
	}
}
