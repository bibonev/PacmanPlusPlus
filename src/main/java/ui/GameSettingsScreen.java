package main.java.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import main.java.event.Event;
import main.java.event.arguments.GameSettingsChangedEventArgs;
import main.java.event.listener.GameSettingsChangedEventListener;

/**
 * Class for settings the game settings
 * 
 * @author Rose Kirtley
 *
 */
public class GameSettingsScreen extends Screen {

	public Button returnButton;
	private Label label;
	private CheckBox ai;
	private ChoiceBox<String> lives, map, ghosts;
	private Event<GameSettingsChangedEventListener, GameSettingsChangedEventArgs> event
			= new Event<>((l, a) -> l.onGameSettingsChanged(a));
	private List<String> mapNames;

	public GameSettingsScreen(final GameUI game) {
		super(game);

		returnButton = new Button("Return");
		returnButton.getStyleClass().add("backButtonStyle");
		returnButton.setOnAction(e -> closeSettings());

		ai = new CheckBox("Play against AI");
		ai.getStyleClass().add("check-box");
		ai.setSelected(true);

		lives = new ChoiceBox<>();
		lives.getStyleClass().add("check-box");
		for(int i = 0; i < 5; i++) {
			lives.getItems().add(String.format("%d lives", i + 1));
		}
		lives.getSelectionModel().select(3);

		ghosts = new ChoiceBox<>();
		ghosts.getStyleClass().add("check-box");
		for(int i = 0; i < 4; i++) {
			ghosts.getItems().add(String.format("%d ghosts", i));
		}
		ghosts.getSelectionModel().select(1);

		label = new Label("Game Settings");
		label.getStyleClass().add("miniTitleStyle");

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");
		
		map = new ChoiceBox<>();
		map.getStyleClass().add("check-box");
		mapNames = new ArrayList<>();
		mapNames.addAll(game.getMapService().getAvailableMaps());
		map.getItems().addAll(mapNames);
		map.getSelectionModel().select(0);

		pane.getChildren().addAll(label, separator, lives, ai, ghosts, map, returnButton);
	}
	
	public Event<GameSettingsChangedEventListener, GameSettingsChangedEventArgs> getGameSettingsChangedEvent(){
		return event;
	}
	
	private void closeSettings(){
		saveSettings();
		game.removeGameSettingsScreen();
	}

	public void saveSettings() {
		game.multiPlayerLobbyScreen.getMultiplayerSettings().setInitialPlayerLives(lives.getSelectionModel().getSelectedIndex() + 1);
		game.multiPlayerLobbyScreen.getMultiplayerSettings().setMapName(map.getValue());
		game.multiPlayerLobbyScreen.getMultiplayerSettings().setAIPlayer(ai.isSelected());
		game.multiPlayerLobbyScreen.getMultiplayerSettings().setGhostCount(ghosts.getSelectionModel().getSelectedIndex());
		onSettingsChanged();
	}

	protected void onSettingsChanged() {
		event.fire(new GameSettingsChangedEventArgs(game.multiPlayerLobbyScreen.getMultiplayerSettings()));
	}

	@Override
	public void changeSelection(final boolean up) {
		// Only one button
	}

	@Override
	public void unselectAll() {
		// only one button
	}

	@Override
	public void makeSelection() {
		closeSettings();

	}
	
	public void loadSettings() {
		ai.setSelected(game.multiPlayerLobbyScreen.getMultiplayerSettings().getAIPlayer());
	    lives.getSelectionModel().select(game.multiPlayerLobbyScreen.getMultiplayerSettings().getInitialPlayerLives() - 1);
	    ghosts.getSelectionModel().select(game.multiPlayerLobbyScreen.getMultiplayerSettings().getGhostCount());
	    
	    int mapIndex = mapNames.indexOf(game.multiPlayerLobbyScreen.getMultiplayerSettings().getMapName());
	    map.getSelectionModel().select(mapIndex);
	}
}
