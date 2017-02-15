package teamproject.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import teamproject.audio.Music;
import teamproject.audio.SoundEffects;
import teamproject.constants.GameType;
import teamproject.event.Event;
import teamproject.event.arguments.container.NewGameRequestedEventArguments;
import teamproject.event.listener.NewGameRequestedEventListener;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;

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
	private MultiPlayerLobbyScreen multiPlayerLobbyScreen;
	private MultiPlayerOptionScreen multiPlayerOptionScreen;
	private MultiPlayerJoinScreen multiPlayerJoinScreen;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		setup();

		thisStage = primaryStage;
		primaryStage.setTitle("PacMac");

		pane = new BorderPane();

		centerPane = new StackPane();
		pane.setCenter(centerPane);
		final Scene scene = new Scene(pane, 500, 500);

		final String css = this.getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().add(css);
		pane.getStyleClass().add("paneStyle");

		setUpSettingsButton();

		primaryStage.setScene(scene);
		switchToLogIn();
		primaryStage.show();
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
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public void startNewSinglePlayerGame() {
		switchToGame();

		// start single player game
		final Event<NewGameRequestedEventListener, NewGameRequestedEventArguments> event = new Event<>(
				(listener, arg) -> listener.onNewGameRequested(arg));
		event.addListener(new teamproject.gamelogic.event.listener.NewGameRequestedEventListener());
		// TODO replace hard-coded user name
		event.fire(new NewGameRequestedEventArguments(GameType.SINGLEPLAYER, new GameSettings(), "test", thisStage));
	}

	public void startNewMultiPlayerGame() {
		switchToGame();
		// start multiplayer game
	}

	public void createNewPendingMultiPlayerGame() {
		// create new lobby for a multiplayer game
		multiPlayerLobbyScreen.addNames();
	}

	public boolean checkGame(final String ip) {
		// check game
		return true;
	}

	public void joinGame(final String gameIp) {
		// join game with ip
		multiPlayerLobbyScreen.addNames();
	}

	public void muteMusic(final boolean bool) {
		music.setOn(bool);
	}

	public void muteSounds(final boolean bool) {
		sounds.setOn(bool);
	}
}
