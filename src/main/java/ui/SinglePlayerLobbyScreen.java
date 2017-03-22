package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import main.java.event.Event;
import main.java.event.arguments.SingleplayerGameStartingEventArgs;
import main.java.event.listener.SingleplayerGameStartingListener;
import main.java.gamelogic.domain.GameSettings;

/**
 * Screen for single player lobby
 *
 * @author Rose Kirtley
 *
 */
public class SinglePlayerLobbyScreen extends Screen {

	private Button play;
	private Label label;
	private Button back;
	private Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> onStartingSingleplayerGame;

	public SinglePlayerLobbyScreen(final GameUI game) {
		super(game);

		play = new Button("Play!");
		play.getStyleClass().add("buttonStyle");
		play.setOnAction(e -> getOnStartingSingleplayerGame()
				.fire(new SingleplayerGameStartingEventArgs(getSinglePlayerSettings(), game.getName())));
		setUpHover(play);

		label = new Label("Single Player");
		label.getStyleClass().add("miniTitleStyle");

		back = new Button("Back");
		back.getStyleClass().add("backButtonStyle");
		back.setOnAction(e -> game.switchToMenu());
		setUpHover(back);
		
		Label pause = new Label("Press esc to pause the game.");
		pause.getStyleClass().add("textStyle");

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");

		pane.getChildren().addAll(label, separator, play, back, pause);

		onStartingSingleplayerGame = new Event<>((l, s) -> l.onSingleplayerGameStarting(s));
	}

	public GameSettings getSinglePlayerSettings() {
		// TODO Rose: when we get round to adding game settings that we need
		// to change, add a screen to let you edit game settings for single
		// player games from this lobby screen - then make this method look
		// at the choices the player has made and return a GameSettings
		// object from it

		// temporary implementation
		return new GameSettings();
	}

	public Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> getOnStartingSingleplayerGame() {
		return onStartingSingleplayerGame;
	}

	@Override
	public void changeSelection(final boolean up) {
		if (play.isDefaultButton()) {
			unselect(play);
			selectBack(back);
		} else {
			select(play);
			unselectBack(back);
		}
	}

	@Override
	public void makeSelection() {
		if (play.isDefaultButton()) {
			getOnStartingSingleplayerGame()
					.fire(new SingleplayerGameStartingEventArgs(getSinglePlayerSettings(), game.getName()));
		} else {
			game.switchToMenu();
		}
	}

	@Override
	public void unselectAll() {
		reset(play);
		resetBack(back);
	}
}
