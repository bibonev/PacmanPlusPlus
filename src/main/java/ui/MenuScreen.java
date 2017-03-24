package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;

/**
 * Screen for the main menu
 *
 * @author Rose Kirtley
 *
 */
public class MenuScreen extends Screen {

	private Button login;
	private boolean loginSelected;
	private Button singlePlayer;
	private boolean singlePlayerelected;
	private Button multiPlayer;
	private boolean multiPlayerSelected;
	private Button close;
	private boolean closeSelected;
	private Button help;
	private boolean helpSelected;
	private Label title;

	public MenuScreen(final GameUI game) {
		super(game);

		login = new Button("Log in again");
		login.getStyleClass().add("buttonStyle");
		login.setOnAction(e -> game.switchToLogIn());
		setUpHover(login);

		singlePlayer = new Button("Single player");
		singlePlayer.getStyleClass().add("buttonStyle");
		singlePlayer.setTooltip(new Tooltip("Play in single player mode"));
		singlePlayer.setOnAction(e -> game.switchToSinglePlayerLobby());
		setUpHover(singlePlayer);

		multiPlayer = new Button("Multiplayer");
		multiPlayer.getStyleClass().add("buttonStyle");
		multiPlayer.setTooltip(new Tooltip("Play in multiplayer mode with others"));
		multiPlayer.setOnAction(e -> game.switchToMultiPlayerOption());
		setUpHover(multiPlayer);
		
		help = new Button("Help");
		help.getStyleClass().add("buttonStyle");
		help.setOnAction(e -> game.switchToHelp());
		setUpHover(help);

		close = new Button("Close");
		close.getStyleClass().add("buttonStyle");
		close.setOnAction(e -> game.close());
		setUpHover(close);

		title = new Label("Main Menu");
		title.getStyleClass().add("miniTitleStyle");

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");

		pane.getChildren().addAll(title, separator, singlePlayer, multiPlayer, login, help, close);
	}

	@Override
	public void changeSelection(final boolean up) {
		if (up) {
			if (singlePlayer.isDefaultButton()) {
				select(close);
				unselect(singlePlayer);
				unselect(multiPlayer);
				unselect(login);
				unselect(help);
			} else if (multiPlayer.isDefaultButton()) {
				select(singlePlayer);
				unselect(multiPlayer);
				unselect(login);
				unselect(close);
				unselect(help);
			} else if (login.isDefaultButton()) {
				select(multiPlayer);
				unselect(login);
				unselect(close);
				unselect(singlePlayer);
				unselect(help);
			} else if (help.isDefaultButton()) {
				select(multiPlayer);
				unselect(login);
				unselect(close);
				unselect(singlePlayer);
				unselect(help);
			} else {
				select(help);
				unselect(login);
				unselect(multiPlayer);
				unselect(singlePlayer);
				unselect(close);
			}
		} else {
			if (singlePlayer.isDefaultButton()) {
				select(multiPlayer);
				unselect(singlePlayer);
				unselect(login);
				unselect(close);
				unselect(help);
			} else if (multiPlayer.isDefaultButton()) {
				select(login);
				unselect(multiPlayer);
				unselect(singlePlayer);
				unselect(close);
				unselect(help);
			} else if (login.isDefaultButton()) {
				select(help);
				unselect(login);
				unselect(singlePlayer);
				unselect(multiPlayer);
				unselect(close);
			} else if (help.isDefaultButton()) {
				select(close);
				unselect(login);
				unselect(multiPlayer);
				unselect(singlePlayer);
				unselect(help);
				unselect(help);
			}else {
				select(singlePlayer);
				unselect(close);
				unselect(login);
				unselect(multiPlayer);
				unselect(help);
			}
		}
	}

	@Override
	public void makeSelection() {
		if (singlePlayer.isDefaultButton()) {
			game.switchToSinglePlayerLobby();
		} else if (multiPlayer.isDefaultButton()) {
			game.switchToMultiPlayerOption();
		} else if (login.isDefaultButton()) {
			game.switchToLogIn();
		} else if (help.isDefaultButton()){
			game.switchToHelp();
		} else {
			game.close();
		}
	}

	@Override
	public void unselectAll() {
		reset(singlePlayer);
		reset(multiPlayer);
		reset(close);
		reset(login);
		reset(help);
	}
}
