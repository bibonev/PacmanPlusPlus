package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * Screen for the main menu
 * 
 * @author Rose Kirtley
 *
 */
public class MenuScreen extends Screen {
	
	private Button login;
	private Button singlePlayer;
	private Button multiPlayer;
	private Button close;
	private Label title;

	public MenuScreen(GameUI game){
		super(game);
		
        login = new Button("Log in again");
        setUpButton(login);
        login.setOnAction(e-> game.switchToLogIn());
        
        singlePlayer = new Button("Single player");
        setUpButton(singlePlayer);
        singlePlayer.setTooltip(new Tooltip("Play in single player mode"));
        singlePlayer.setOnAction(e-> game.switchToSinglePlayerLobby());
        
        multiPlayer = new Button("Multiplayer");
        setUpButton(multiPlayer);
        multiPlayer.setTooltip(new Tooltip("Play in multiplayer mode with others"));
        multiPlayer.setOnAction(e-> game.switchToMultiPlayerOption());
        
        close = new Button("Close");
        setUpButton(close);
        close.setOnAction(e-> game.close());
        
        title = new Label("Main Menu");
        title.setStyle(titleStyle);
		
        pane.getChildren().addAll(title, singlePlayer, multiPlayer, login, close);
	}
}
