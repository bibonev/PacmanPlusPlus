package teamproject.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import teamproject.audio.Music;
import teamproject.audio.SoundEffects;
import teamproject.event.Event;
import teamproject.event.arguments.GameStartedEventArgs;
import teamproject.event.arguments.LobbyChangedEventArgs;
import teamproject.event.listener.GameClosingListener;
import teamproject.event.listener.GameStartedListener;
import teamproject.event.listener.LobbyStateChangedListener;
import teamproject.gamelogic.core.GameCommandService;
import teamproject.gamelogic.core.Lobby;
import teamproject.gamelogic.domain.Game;
import teamproject.constants.GameType;
import teamproject.graphics.PositionVisualisation;
import teamproject.graphics.Render;
import teamproject.networking.integration.ClientInstance;
import teamproject.networking.integration.ServerInstance;

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
	private BorderPane pane;
	private BorderPane banner;
	public Button settings;
	private StackPane centerPane;

	private boolean isPlaying;
	private String name;

	public LogInScreen logInScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
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
		final Scene scene = new Scene(pane, 500, 500);
		scene.setOnKeyPressed(e-> sendMoveEvent(e.getCode()));

		final String css = this.getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().add(css);
		pane.getStyleClass().add("paneStyle");

		setUpSettingsButton();

		primaryStage.setScene(scene);
		switchToLogIn();
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		this.onGameClosing.fire(null);
	}
	
	public Event<GameClosingListener,Object> getOnGameClosingEvent() {
		return this.onGameClosing;
	}

	private void setup() {
		music = new Music();
		//sounds = new SoundEffects();
		logInScreen = new LogInScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this, music);
		settingsScreen = new SettingsScreen(this);
		singlePlayerLobbyScreen = new SinglePlayerLobbyScreen(this);
		singlePlayerLobbyScreen.getOnStartingSingleplayerGame().addListener(gameCommandService);
		multiPlayerLobbyScreen = new MultiPlayerLobbyScreen(this);
		multiPlayerOptionScreen = new MultiPlayerOptionScreen(this);
		multiPlayerJoinScreen = new MultiPlayerJoinScreen(this);
	}
	
	private void sendMoveEvent(KeyCode key){
		if(key == KeyCode.R){
			if(isPlaying){
				music.stopMusic();
				isPlaying = false;
			}else{
				music.playMusic();
				isPlaying = true;
			}
		}
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

	private void setScreen(final Pane screen) {
		centerPane.getChildren().remove(0, centerPane.getChildren().size());
		centerPane.getChildren().add(screen);
	}

	public void switchToMenu() {
		System.out.println("Here");
		setScreen(menuScreen.getPane());
		final Label label = new Label("PacMan " + getName());
		banner.setLeft(label);
		settings.setDisable(false);
		isPlaying = false;
	}

	public void switchToLogIn() {
		final Label label = new Label("PacMan");
		banner.setLeft(label);
		setScreen(logInScreen.getPane());
	}

	public void switchToGame() {
		settings.setDisable(true);
		music.playMusic();
		isPlaying = true;
		setScreen(gameScreen.getPane());
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
		setScreen(singlePlayerLobbyScreen.getPane());
	}

	public void switchToMultiPlayerLobby() {
		setScreen(multiPlayerLobbyScreen.getPane());

	}

	public void switchToMultiPlayerOption() {
		setScreen(multiPlayerOptionScreen.getPane());
	}

	public void switchToMultiPlayerJoin() {
		setScreen(multiPlayerJoinScreen.getPane());
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
		
		this.lobby = new Lobby();
		ServerInstance server = new ServerInstance(this, lobby);
		ClientInstance client = new ClientInstance(this, name, "localhost");
		
		this.onGameClosing.addListener(() -> {
			server.stop();
			client.stop();
		});
		
		this.multiPlayerLobbyScreen.getUserLeavingLobbyEvent().addListener(
				() -> {
					client.stop();
					server.stop();
				});
		this.multiPlayerLobbyScreen.getHostStartingGameListener().addListener(server);
		this.multiPlayerLobbyScreen.setStartGameEnabled(true);
		this.gameCommandService.getGameStartedEvent().addListener(client);
		this.gameCommandService.getGameStartedEvent().addListener(server);
		
		server.getMultiplayerGameStartingEvent().addListener(gameCommandService);
		client.getMultiplayerGameStartingEvent().addListener(gameCommandService);
		
		server.run();
		client.run();
		// create new lobby for a multiplayer game
	}

	public boolean checkGame(final String ip) {
		// check game
		return true;
	}

	// TODO move creation of client instance into GameCommandService at some point
	public void joinGame(final String gameIp) {
		ClientInstance client = new ClientInstance(this, name, gameIp);
		
		this.onGameClosing.addListener(() -> {
			client.stop();
		});
		
		this.multiPlayerLobbyScreen.getUserLeavingLobbyEvent().addListener(
				() -> client.stop());
		this.multiPlayerLobbyScreen.setStartGameEnabled(false);
		this.gameCommandService.getGameStartedEvent().addListener(client);
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
	public void onLobbyStateChanged(LobbyChangedEventArgs args) {
		if(args instanceof LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) {
			multiPlayerLobbyScreen.list.removePlayer(
					((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID());
		} else if(args instanceof LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) {
			multiPlayerLobbyScreen.list.addPlayer(
					((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerInfo());
		} else {
			// TODO: update rules display
		}
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
		lobby.getLobbyStateChangedEvent().addListener(this);
	}

	@Override
	public void onGameStarted(GameStartedEventArgs args) {
		if(args.getGame().getGameType() != GameType.MULTIPLAYER_SERVER) {
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
