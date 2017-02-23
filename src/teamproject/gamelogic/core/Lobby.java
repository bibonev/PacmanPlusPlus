package teamproject.gamelogic.core;

import java.util.HashMap;

import teamproject.gamelogic.domain.GameSettings;

/**
 * Represents the visual state of the pre-game multiplayer lobby before
 * the game is started. This allows the user to see the list of connected
 * players, as well as the currently-assigned game settings.
 * 
 * @author Tom Galvin
 *
 */
public class Lobby {
	private HashMap<Integer, String> players;
	private GameSettings settings;
	private String[] settingsString;
	
	public Lobby() {
		players = new HashMap<>();
		settingsString = new String[0];
		settings = new GameSettings();
	}
	
	public String[] getSettingsString() {
		return settingsString;
	}
	
	public void setSettingsString(String[] settingsString) {
		this.settingsString = settingsString;
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public void addPlayer(int id, String name) {
		players.put(id, name);
	}
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}
}
