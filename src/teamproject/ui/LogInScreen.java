package teamproject.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import teamproject.event.Event;
import teamproject.event.arguments.container.UserLoggedInEventArguments;
import teamproject.event.listener.UserLoggedInEventListener;

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

	public LogInScreen(final GameUI game) {
		super(game);

		title = new Label("Welcome to PacMan");
		title.getStyleClass().add("titleStyle");

		label = new Label("Enter Username:");
		label.getStyleClass().add("labelStyle");

		text = new TextField();
		text.getStyleClass().add("labelStyle");

		login = new Button("Log in");
		login.getStyleClass().add("buttonStyle");
		login.setOnAction(e -> switchScreen(text.getText()));

		pane.getChildren().addAll(title, label, text, login);
	}

	private void switchScreen(final String username) {
		if (!username.isEmpty()) {
			// if(checkname(username)){
			game.setName(username);
			final Event<UserLoggedInEventListener, UserLoggedInEventArguments> event = new Event<>(
					(listener, arg) -> listener.onUserLoggedIn(arg));
			event.addListener(new teamproject.gamelogic.event.listener.UserLoggedInEventListener());
			event.fire(new UserLoggedInEventArguments(username));
			game.switchToMenu();
			// }else{
			// Alert alert = new Alert(AlertType.ERROR);
			// alert.setTitle("Username Issue");
			// alert.setHeaderText("The username you have entered is already
			// being used");
			// alert.setContentText("Please enter a new username");
			//
			// alert.showAndWait();
			// }
		}
	}
}
