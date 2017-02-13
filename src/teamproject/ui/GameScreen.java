package teamproject.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import teamproject.audio.Music;
import teamproject.constants.GameStateType;
import teamproject.event.Event;
import teamproject.event.arguments.container.GameStateChangedEventArguments;
import teamproject.event.listener.GameStateChangedEventListener;

/**
 * Screen for the actual game
 *
 * @author Rose Kirtley
 *
 */
public class GameScreen extends Screen {

	private Button pause;
	private Rectangle gameGraphics;
	private StackPane mainPane;
	private FlowPane gamePane;
	private Screen pauseScreen;

	private Music music;

	public GameScreen(final GameUI game, final Music music) {
		super(game);

		this.music = music;

		pause = new Button("pause");
		pause.getStyleClass().add("buttonStyle");
		pause.setOnAction(e -> paused());

		gamePane = new FlowPane();
		gamePane.setPadding(new Insets(5, 0, 5, 0));
		gamePane.setVgap(4);
		gamePane.setHgap(4);
		gamePane.setOrientation(Orientation.VERTICAL);
		gamePane.setAlignment(Pos.TOP_CENTER);

		gameGraphics = new Rectangle(300, 300);
		gameGraphics.getFill();

		gamePane.getChildren().addAll(pause, gameGraphics);
		pauseScreen = new PauseScreen(game, this);

		mainPane = new StackPane();
		mainPane.getChildren().add(gamePane);

		pane.getChildren().addAll(mainPane);
	}

	private void paused() {
		music.stopMusic();
		game.settings.setDisable(false);
		pause.setDisable(true);
		mainPane.getChildren().add(pauseScreen.getPane());
		game.setIsPlaying(false);

		final Event<GameStateChangedEventListener, GameStateChangedEventArguments> event = new Event<>(
				(listener, args) -> listener.onGameStateChanged(args));
		event.fire(new GameStateChangedEventArguments(game.logInScreen.getName(), GameStateType.PAUSE));
	}

	public void play() {
		music.playMusic();
		game.settings.setDisable(true);
		pause.setDisable(false);
		mainPane.getChildren().removeAll(pauseScreen.getPane());
		game.setIsPlaying(true);

		final Event<GameStateChangedEventListener, GameStateChangedEventArguments> event = new Event<>(
				(listener, args) -> listener.onGameStateChanged(args));
		event.fire(new GameStateChangedEventArguments(game.logInScreen.getName(), GameStateType.PLAY));
	}

	public void quit() {
		pause.setDisable(false);
		mainPane.getChildren().removeAll(pauseScreen.getPane());

		final Event<GameStateChangedEventListener, GameStateChangedEventArguments> event = new Event<>(
				(listener, args) -> listener.onGameStateChanged(args));
		event.fire(new GameStateChangedEventArguments(game.logInScreen.getName(), GameStateType.QUIT));
	}

}
