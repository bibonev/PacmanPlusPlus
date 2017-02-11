package teamproject.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
		
		label = new Label("IP address: ");
        label.getStyleClass().add("labelStyle");
		
		back = new Button("Back");
        back.getStyleClass().add("butonStyle");
		back.setOnAction(e -> game.switchToMenu());
		
		pane.getChildren().addAll(label, ip, join, back);
	}
	
	private void joinGame(String text){
		if(text.isEmpty()){
			return;
		}
		if(game.checkGame(text)){
			//fire event to join a game
			game.joinGame(text);
			game.switchToMultiPlayerLobby();
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("IP Address Issue");
			alert.setHeaderText("The IP Address you have entered is not valid");
			alert.setContentText("Please enter an IP Address which you know has started a game");

			alert.showAndWait();
		}
	}

}
