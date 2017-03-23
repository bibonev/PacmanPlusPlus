package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private CheckBox ai;
	private GameSettings settings;
	private Event<SingleplayerGameStartingListener, SingleplayerGameStartingEventArgs> onStartingSingleplayerGame;

	public SinglePlayerLobbyScreen(final GameUI game) {
		super(game);
		settings = new GameSettings();

		play = new Button("Play!");
		play.getStyleClass().add("buttonStyle");
		play.setOnAction(e -> getOnStartingSingleplayerGame()
				.fire(new SingleplayerGameStartingEventArgs(getSinglePlayerSettings(), game.getName())));
		setUpHover(play);

		label = new Label("Single Player");
		label.getStyleClass().add("miniTitleStyle");
		
		ai = new CheckBox("Play against AI");
		ai.getStyleClass().add("check-box");
		ai.setSelected(true);
		ai.selectedProperty().addListener(e -> changeSettings(ai.isSelected()));

		back = new Button("Back");
		back.getStyleClass().add("backButtonStyle");
		back.setOnAction(e -> game.switchToMenu());
		setUpHover(back);
		
		Label pause = new Label("Press esc to pause the game.");
		pause.getStyleClass().add("textStyle");

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");

		pane.getChildren().addAll(label, separator, play, ai, back, pause);

		onStartingSingleplayerGame = new Event<>((l, s) -> l.onSingleplayerGameStarting(s));
	}
	
	private void changeSettings(boolean b){
		settings.setAIPlayer(b);
	}

	public GameSettings getSinglePlayerSettings() {
		return settings;
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
