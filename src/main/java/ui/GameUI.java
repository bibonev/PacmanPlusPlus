package main.java.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.audio.Music;
import main.java.audio.SoundEffects;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.GameStartedEventArgs;
import main.java.event.arguments.LobbyChangedEventArgs;
import main.java.event.listener.GameClosingListener;
import main.java.event.listener.GameStartedListener;
import main.java.event.listener.LobbyStateChangedListener;
import main.java.gamelogic.core.GameCommandService;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.domain.Game;
import main.java.graphics.PositionVisualisation;
import main.java.graphics.Render;
import main.java.networking.integration.ClientInstance;
import main.java.networking.integration.ServerInstance;

/**
 * UI to be run, contains all screens
 *
 * @author Rose Kirtley
 *
 */
public class GameUI extends Application implements LobbyStateChangedListener, GameStartedListener {
	private Lobby lobby;
	private Game game;
	private Music music;
	private SoundEffects sounds;

	private Stage thisStage;
	private Scene uiScene;
	private BorderPane pane;
	private BorderPane banner;
	public Button settings;
	private StackPane centerPane;

	private boolean isPlaying;
	private String name;

	public Screen currentScreen;
	public LogInScreen logInScreen;
	public MenuScreen menuScreen;
	// private GameScreen gameScreen;
	private SettingsScreen settingsScreen;
	private SinglePlayerLobbyScreen singlePlayerLobbyScreen;
	public MultiPlayerLobbyScreen multiPlayerLobbyScreen;
	private MultiPlayerOptionScreen multiPlayerOptionScreen;
	private MultiPlayerJoinScreen multiPlayerJoinScreen;

	private Event<GameClosingListener, Object> onGameClosing = new Event<>((l, a) -> l.onGameClosing());
	private GameCommandService gameCommandService;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		gameCommandService = new GameCommandService();
		gameCommandService.getGameStartedEvent().addListener(this);
		setup();

		thisStage = primaryStage;
		primaryStage.setTitle("PacMac");

		pane = new BorderPane();

		centerPane = new StackPane();
		pane.setCenter(centerPane);
		pane.getStyleClass().add("paneStyle");
		uiScene = new Scene(pane, 500, 500);
		uiScene.setOnKeyPressed(e -> sendMoveEvent(e.getCode()));

		final String css = this.getClass().getResource("style.css").toExternalForm();
		uiScene.getStylesheets().add(css);
		pane.getStyleClass().add("paneStyle");

		setUpSettingsButton();

		primaryStage.setScene(uiScene);
		switchToLogIn();
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		onGameClosing.fire(null);
	}

	public Event<GameClosingListener, Object> getOnGameClosingEvent() {
		return onGameClosing;
	}

	private void setup() {
		music = new Music();
		// sounds = new SoundEffects();
		logInScreen = new LogInScreen(this);
		menuScreen = new MenuScreen(this);
		settingsScreen = new SettingsScreen(this);
		singlePlayerLobbyScreen = new SinglePlayerLobbyScreen(this);
		singlePlayerLobbyScreen.getOnStartingSingleplayerGame().addListener(gameCommandService);
		multiPlayerLobbyScreen = new MultiPlayerLobbyScreen(this);
		multiPlayerOptionScreen = new MultiPlayerOptionScreen(this);
		multiPlayerJoinScreen = new MultiPlayerJoinScreen(this);
	}

	private void sendMoveEvent(final KeyCode key) {
		if (key == KeyCode.UP) {
			currentScreen.changeSelection(true);
		} else if (key == KeyCode.DOWN) {
			currentScreen.changeSelection(false);
		} else if (key == KeyCode.ENTER) {
			currentScreen.makeSelection();
		}

		//
		// if(key == KeyCode.R){
		// if(isPlaying){
		// music.stopMusic();
		// isPlaying = false;
		// }else{
		// music.playMusic();
		// isPlaying = true;
		// }
		// }
	}

	private void setUpSettingsButton() {

		settings = new Button("Settings");
		settings.setOnAction(e -> switchToSettings());
		settings.getStyleClass().add("buttonStyle");

		banner = new BorderPane();

		banner.setRight(settings);

		pane.setTop(banner);
	}

	public static void main(final String[] args) {
		launch(args);
	}

	private void setScreen(final Screen screen) {
		currentScreen = screen;
		centerPane.getChildren().remove(0, centerPane.getChildren().size());
		centerPane.getChildren().add(screen.getPane());
	}

	public void switchToMenu() {
		thisStage.setScene(uiScene);
		music.stopMusic(); // TODO move to when game ends
		setScreen(menuScreen);
		final Label label = new Label("PacMan " + getName());
		label.getStyleClass().add("labelStyle");
		banner.setLeft(label);
		settings.setDisable(false);
		isPlaying = false;
	}

	public void switchToLogIn() {
		final Label label = new Label("PacMan");
		label.getStyleClass().add("labelStyle");
		banner.setLeft(label);
		setScreen(logInScreen);
	}

	public void switchToGame() {
		settings.setDisable(true);
		music.playMusic();
		isPlaying = true;
		// setScreen(gameScreen);
	}

	public void switchToSettings() {
		settings.setDisable(true);
		centerPane.getChildren().add(settingsScreen.getPane());
	}

	public void returnBack() {
		settings.setDisable(false);
		centerPane.getChildren().remove(settingsScreen.getPane());
	}

	public void switchToSinglePlayerLobby() {
		setScreen(singlePlayerLobbyScreen);
	}

	public void switchToMultiPlayerLobby() {
		setScreen(multiPlayerLobbyScreen);

	}

	public void switchToMultiPlayerOption() {
		setScreen(multiPlayerOptionScreen);
	}

	public void switchToMultiPlayerJoin() {
		setScreen(multiPlayerJoinScreen);
	}

	public void close() {
		thisStage.close();
	}

	public void setIsPlaying(final boolean bool) {
		isPlaying = bool;
	}

	public Game getGame() {
		return game;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void createNewPendingMultiPlayerGame() {
		multiPlayerLobbyScreen.addNames();

		lobby = new Lobby();
		final ServerInstance server = new ServerInstance(this, lobby);
		final ClientInstance client = new ClientInstance(this, name, "localhost");

		onGameClosing.addListener(() -> {
			server.stop();
			client.stop();
		});

		multiPlayerLobbyScreen.getUserLeavingLobbyEvent().addListener(() -> {
			client.stop();
			server.stop();
		});
		multiPlayerLobbyScreen.getHostStartingGameListener().addListener(server);
		multiPlayerLobbyScreen.setStartGameEnabled(true);
		gameCommandService.getGameStartedEvent().addListener(client);
		gameCommandService.getGameStartedEvent().addListener(server);

		server.getMultiplayerGameStartingEvent().addListener(gameCommandService);
		client.getMultiplayerGameStartingEvent().addListener(gameCommandService);

		server.run();
		client.run();
		// create new lobby for a multiplayer game
	}

	// TODO move creation of client instance into GameCommandService at some
	// point
	public void joinGame(final String gameIp) {
		final ClientInstance client = new ClientInstance(this, name, gameIp);

		onGameClosing.addListener(() -> {
			client.stop();
		});

		multiPlayerLobbyScreen.getUserLeavingLobbyEvent().addListener(() -> client.stop());
		multiPlayerLobbyScreen.setStartGameEnabled(false);
		gameCommandService.getGameStartedEvent().addListener(client);
		client.getMultiplayerGameStartingEvent().addListener(gameCommandService);

		client.run();
		// join game with ip
	}

	public void muteMusic(final boolean bool) {
		music.setOn(bool);
	}

	public void muteSounds(final boolean bool) {
		sounds.setOn(bool);
	}

	@Override
	public void onLobbyStateChanged(final LobbyChangedEventArgs args) {
		if (args instanceof LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) {
			multiPlayerLobbyScreen.list
					.removePlayer(((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID());
		} else if (args instanceof LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) {
			multiPlayerLobbyScreen.list
					.addPlayer(((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerInfo());
		} else {
			// TODO: update rules display
		}
	}

	public void setLobby(final Lobby lobby) {
		this.lobby = lobby;
		lobby.getLobbyStateChangedEvent().addListener(this);
	}

	@Override
	public void onGameStarted(final GameStartedEventArgs args) {
		if (args.getGame().getGameType() != GameType.MULTIPLAYER_SERVER) {
			Platform.runLater(() -> {
				switchToGame();

				switchToMultiPlayerLobby();

				final Render mapV = new Render(this, args.getGame(), args.getGameLogic());

				// Initialize Screen dimensions
				PositionVisualisation.initScreenDimensions();

				// Draw Map
				thisStage.setScene(mapV.drawWorld());
				thisStage.show();

				// Add CLick Listener
				mapV.addClickListener();

				// Redraw Map
				mapV.redrawWorld();

				// Start Timeline
				mapV.startTimeline();
			});
		}
	}
}