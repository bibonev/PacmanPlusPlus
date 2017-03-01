package teamproject.gamelogic.core;

import java.util.HashMap;
import java.util.Set;

import teamproject.event.Event;
import teamproject.event.arguments.LobbyChangedEventArgs;
import teamproject.event.listener.LobbyStateChangedListener;

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
	private Event<LobbyStateChangedListener, LobbyChangedEventArgs> lobbyStateChangedEvent;
	
	public Lobby() {
		players = new HashMap<>();
		settingsString = new String[0];
		lobbyStateChangedEvent = new Event<>((l, a) -> l.onLobbyStateChanged(a));
	}
	
	public Event<LobbyStateChangedListener, LobbyChangedEventArgs> getLobbyStateChangedEvent() {
		return lobbyStateChangedEvent;
	}
	
	public String[] getSettingsString() {
		return settingsString;
	}
	
	public void setSettingsString(String[] settingsString) {
		this.settingsString = settingsString;
		lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyRulesChangedEventArgs(settingsString));
	}
	
	public Set<Integer> getPlayerIDs() {
		return players.keySet();
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public void addPlayer(int id, LobbyPlayerInfo name) {
		players.put(id, name);
		lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs(id, name));
	}
	
	public LobbyPlayerInfo getPlayer(int id) {
		return players.get(id);
	}

	public void removePlayer(int playerID) {
		if(players.containsKey(playerID)) {
			players.remove(playerID);
			lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyPlayerLeftEventArgs(playerID));
		} else {
			throw new IllegalArgumentException("No player with ID " + playerID + " in the lobby.");
		}
	}
	
	
}
