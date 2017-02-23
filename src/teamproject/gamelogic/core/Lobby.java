package teamproject.gamelogic.core;

import java.util.HashMap;

/**
 * Represents the visual state of the pre-game multiplayer lobby before
 * the game is started. This allows the user to see the list of connected
 * players, as well as the currently-assigned game settings.
 * 
 * This is just a view model object, and hence contains no information of
 * the actual game settings - just information to represent what is visible
 * on the lobby screen.
 * 
 * @author Tom Galvin
 *
 */
public class Lobby {
	private HashMap<Integer, LobbyPlayerInfo> players;
	private String[] settingsString;
	
	public Lobby() {
		players = new HashMap<>();
		settingsString = new String[0];
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
	
	public void addPlayer(int id, LobbyPlayerInfo name) {
		players.put(id, name);
	}
}
