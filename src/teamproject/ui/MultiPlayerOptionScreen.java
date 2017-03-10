package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
	private Button back;
	private Label title;

	public MultiPlayerOptionScreen(GameUI game){
		super(game);
		
		title = new Label("Mutliplayer Game Options");
		title.getStyleClass().add("miniTitleStyle");
		
        joinGame = new Button("Join game");
        joinGame.getStyleClass().add("buttonStyle");
        joinGame.setTooltip(new Tooltip("Join a game"));
        joinGame.setOnAction(e-> game.switchToMultiPlayerJoin());
		
        startGame = new Button("Start a new game");
        startGame.getStyleClass().add("buttonStyle");
        startGame.setTooltip(new Tooltip("Initiate a new game"));
        startGame.setOnAction(e-> startNewGame());
        
        back = new Button("Back");
        back.getStyleClass().add("backButtonStyle");
		back.setOnAction(e -> game.switchToMenu());
		
		Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        
        pane.getChildren().addAll(title, separator, joinGame, startGame, back);
	}
	
	private void startNewGame(){
		//fire event to start new game
		//give game game.logInScreen.getName(); for the user name
		game.createNewPendingMultiPlayerGame();
		game.switchToMultiPlayerLobby();
	}
}
