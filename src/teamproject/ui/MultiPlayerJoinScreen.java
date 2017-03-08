package teamproject.ui;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Screen for entering an IP address to join a pre-existing game
 * 
 * @author Rose Kirtley
 *
 */
public class MultiPlayerJoinScreen extends Screen {
	
	private Button join;
	private Button back;
    private TextField ip;
    private Label label;


	public MultiPlayerJoinScreen(GameUI game) {
		super(game);
		
		join = new Button("Join game");
		join.getStyleClass().add("buttonStyle");
		join.setOnAction(e-> joinGame(ip.getText()));
		
		ip = new TextField();
        ip.getStyleClass().add("labelStyle");
        ip.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                	joinGame(ip.getText());
                }
            }
        });
		
		label = new Label("IP address: ");
        label.getStyleClass().add("labelStyle");
		
		back = new Button("Back");
        back.getStyleClass().add("buttonStyle");
		back.setOnAction(e -> game.switchToMenu());
		
		pane.getChildren().addAll(label, ip, join, back);
	}
	
	private void joinGame(String text){
		if(text.isEmpty()){
			return;
		}
			//fire event to join a game
		try{
			game.joinGame(text);
			game.switchToMultiPlayerLobby();
		}catch(RuntimeException ex){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("IP Address Issue");
			alert.setHeaderText("The IP Address you have entered is not valid");
			alert.setContentText("Please enter an IP Address which you know has started a game");

			alert.showAndWait();
		}
	}

}
