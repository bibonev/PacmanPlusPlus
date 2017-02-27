package teamproject.ui;

import javafx.application.Application;
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
import teamproject.constants.GameType;
import teamproject.event.Event;
import teamproject.event.arguments.NewGameRequestedEventArguments;
import teamproject.event.listener.GameClosingListener;
import teamproject.event.listener.NewGameRequestedEventListener;
import teamproject.gamelogic.core.GameCommandService;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.event.listener.NewGameRequestedListener;
import teamproject.graphics.GhostVisualisation;
import teamproject.graphics.PacmanVisualisation;
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
public class GameUI extends Application {

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

	@Override
	public void start(final Stage primaryStage) throws Exception {
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
		sounds = new SoundEffects();
		logInScreen = new LogInScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this, music);
		settingsScreen = new SettingsScreen(this);
		singlePlayerLobbyScreen = new SinglePlayerLobbyScreen(this);
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

	// TODO: refactor - these 2 methods are very very similar
	public void startNewSinglePlayerGame() {
		switchToGame();

		final Event<NewGameRequestedEventListener, NewGameRequestedEventArguments> onNewGameRequested = new Event<>( (l, s) -> l.onNewGameRequested(s));

		onNewGameRequested.addListener(new NewGameRequestedListener());
		onNewGameRequested.fire(new NewGameRequestedEventArguments(GameType.SINGLEPLAYER, new GameSettings(), name, thisStage));
	}

	public void startNewMultiPlayerGame() {
		switchToMultiPlayerLobby();

		final Event<NewGameRequestedEventListener, NewGameRequestedEventArguments> onNewGameRequested = new Event<>( (l, s) -> l.onNewGameRequested(s));

		onNewGameRequested.addListener(new NewGameRequestedListener());
		onNewGameRequested.fire(new NewGameRequestedEventArguments(GameType.MULTIPLAYER, new GameSettings(), name, thisStage));

	}

	public void createNewPendingMultiPlayerGame() {
		Game game = GameCommandService.generateNewMultiplayerGame(name, new GameSettings());
		multiPlayerLobbyScreen.addNames();
		
		ServerInstance server = new ServerInstance(this, game.getWorld());
		ClientInstance client = new ClientInstance(this, game.getWorld(), name, "localhost");
		
		this.onGameClosing.addListener(() -> {
			server.stop();
			client.stop();
		});
		
		server.run();
		client.run();
		// create new lobby for a multiplayer game
	}

	public boolean checkGame(final String ip) {
		// check game
		return true;
	}

	public void joinGame(final String gameIp) {
		Game game = GameCommandService.generateNewMultiplayerGame(name, new GameSettings());
		multiPlayerLobbyScreen.addNames();
		ClientInstance client = new ClientInstance(this, game.getWorld(), name, gameIp);
		client.run();
		// join game with ip
	}

	public void muteMusic(final boolean bool) {
		music.setOn(bool);
	}

	public void muteSounds(final boolean bool) {
		sounds.setOn(bool);
	}
}
