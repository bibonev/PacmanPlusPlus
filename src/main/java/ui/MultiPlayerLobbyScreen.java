package main.java.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import main.java.event.Event;
import main.java.event.arguments.HostStartingMultiplayerGameEventArgs;
import main.java.event.listener.CountDownStartingListener;
import main.java.event.listener.HostStartingMultiplayerGameListener;
import main.java.event.listener.UserLeavingLobbyListener;
import main.java.gamelogic.domain.GameSettings;

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
	private ObservableList<Node> rulesList;

	public boolean thisClient;
	public PlayersList list;
	private GameSettings gameSettings;

	private Event<UserLeavingLobbyListener, Object> userLeavingLobbyEvent;
	private Event<HostStartingMultiplayerGameListener, Object> hostStartingGameListener;
	private Event<CountDownStartingListener, Object> countDownStartingListener;

	public MultiPlayerLobbyScreen(final GameUI game) {
		super(game);

		gameSettings = new GameSettings();

		play = new Button("Start Game");
		play.getStyleClass().add("buttonStyle");
		play.setOnAction(e -> play());
		setUpHover(play);

		leaveGame = new Button("Leave Game");
		leaveGame.getStyleClass().add("buttonStyle");
		leaveGame.setOnAction(e -> leaveGame());
		setUpHover(leaveGame);


		
		settings = new Button("Change Game Settings");
		settings.getStyleClass().add("buttonStyle");
		settings.setOnAction(e -> showSettings());
		setUpHover(settings);

		final HBox buttons = new HBox();
		buttons.setAlignment(Pos.TOP_CENTER);
		buttons.getStyleClass().add("paneStyle");
		buttons.getChildren().addAll(play, leaveGame, settings);

		label = new Label("Multiplayer");
		label.getStyleClass().add("miniTitleStyle");
		
		final FlowPane rules = new FlowPane();
		rules.setPadding(new Insets(5, 5, 5, 5));
		rules.setVgap(4);
		rules.setHgap(4);
		rules.setOrientation(Orientation.VERTICAL);
		rules.setAlignment(Pos.TOP_CENTER);
		rules.getStyleClass().add("paneStyle");
		rulesList = rules.getChildren();

		final BorderPane labelPane = new BorderPane();
		labelPane.setTop(label);
		labelPane.setAlignment(label, Pos.CENTER);
		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");
		labelPane.setBottom(separator);

		bPane = new BorderPane();
		bPane.setPadding(new Insets(-25, 0, 0, 0));
		bPane.setTop(labelPane);
		bPane.setLeft(rules);
		bPane.setBottom(buttons);
		BorderPane.setAlignment(rules, Pos.TOP_CENTER);
		pane.getChildren().add(bPane);

		userLeavingLobbyEvent = new Event<>((l, a) -> l.onUserLeavingLobby());
		userLeavingLobbyEvent.addListener(this);

		hostStartingGameListener = new Event<>(
				(l, a) -> l.onHostStartingGame(new HostStartingMultiplayerGameEventArgs(getMultiplayerSettings())));

		countDownStartingListener = new Event<>((l, a) -> l.onCountDownStarted());

		
		addNames();
	}
	
	public void setRuleStrings(String[] rules) {
		Platform.runLater(() -> {
			rulesList.clear();
			
			for(String rule : rules) {
				Label ruleLabel = new Label(rule);
				ruleLabel.getStyleClass().addAll("labelStyle");
				
				rulesList.add(ruleLabel);
			}
		});
	}

	public void showSettings() {
		game.showGameSettingsScreen();
	}

	public void setMultiplayerSettings(final GameSettings newSettings) {
		gameSettings = newSettings;
	}

	public GameSettings getMultiplayerSettings() {
		return gameSettings;
	}

	private void play() {
		thisClient = true;
		getCountDownStartingListener().fire(null);
	}

	public void setStartGameEnabled(final boolean enabled) {
		play.setDisable(!enabled);
	}

	public void setGameSettingsEnabled(final boolean enabled) {
		settings.setDisable(!enabled);
	}

	private void leaveGame() {
		// fire event for leaving a multiplayer game
		game.switchToMenu();
		userLeavingLobbyEvent.fire(null);
	}

	public void addNames() {
		list = new PlayersList(game);
		bPane.setRight(list.getPane());
	}

	public Event<UserLeavingLobbyListener, Object> getUserLeavingLobbyEvent() {
		return userLeavingLobbyEvent;
	}

	public Event<HostStartingMultiplayerGameListener, Object> getHostStartingGameListener() {
		return hostStartingGameListener;
	}
	
	public Event<CountDownStartingListener, Object> getCountDownStartingListener(){
		return countDownStartingListener;
	}

	@Override
	public void onUserLeavingLobby() {
		list.clear();
	}

	@Override
	public void changeSelection(final boolean up) {
		if (up) {
			if (settings.isDefaultButton()) {
				if (!play.isDisabled()) {
					unselect(play);
				}
				select(leaveGame);
				unselect(settings);
			} else if (leaveGame.isDefaultButton()) {
				if (play.isDisabled()) {
					select(settings);
				} else {
					select(play);
					unselect(settings);
				}
				unselect(leaveGame);
			} else {
				select(settings);
				unselect(play);
				unselect(leaveGame);
			}
		} else {
			if (settings.isDefaultButton()) {
				if (play.isDisabled()) {
					select(leaveGame);
				} else {
					select(play);
					unselect(leaveGame);
				}
				unselect(settings);
			} else if (leaveGame.isDefaultButton()) {
				if (!play.isDisabled()) {
					unselect(play);
				}
				select(settings);
				unselect(leaveGame);
			} else {
				select(leaveGame);
				unselect(play);
				unselect(settings);
			}
		}

	}

	@Override
	public void makeSelection() {
		if (settings.isDefaultButton()) {
			showSettings();
		} else if (leaveGame.isDefaultButton()) {
			leaveGame();
		} else {
			play();
		}
	}

	@Override
	public void unselectAll() {
		reset(play);
		reset(leaveGame);
		reset(settings);
	}
}
