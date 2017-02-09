package teamproject.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Screen for logging in
 * 
 * @author Rose Kirtley
 *
 */
public class LogInScreen extends Screen {
	
	private Button login;
	private Label label;
	private Label title;
	private TextField text;

	public LogInScreen(GameUI game){
		super(game);
		
        title = new Label("Welcome to PacMan");
        title.setStyle(titleStyle);
        
        label = new Label("Enter Username:");
        label.setStyle(labelStyle);
        
        text = new TextField ();
        text.setStyle(labelStyle);
        
        login = new Button("Log in");
        setUpButton(login);
        login.setOnAction(e-> switchScreen(text.getText()));
        
	    pane.getChildren().addAll(title, label, text, login);
	}
	
	private void switchScreen(String name){
		if(!name.isEmpty()){
			//if(checkname(name)){
				game.switchToMenu();
//			}else{
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Username Issue");
//				alert.setHeaderText("The username you have entered is already being used");
//				alert.setContentText("Please enter a new username");
//
//				alert.showAndWait();
//			}
		}
	}
}
