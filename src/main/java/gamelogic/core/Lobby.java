package main.java.gamelogic.core;

import java.util.HashMap;
import java.util.Set;

import main.java.event.Event;
import main.java.event.arguments.LobbyChangedEventArgs;
import main.java.event.listener.LobbyStateChangedListener;

/**
 * Represents the visual state of the pre-game multiplayer lobby before the game
 * is started. This allows the user to see the list of connected players, as
 * well as the currently-assigned game settings.
 *
 * This is just a view model object, and hence contains no information of the
 * actual game settings - just information to represent what is visible on the
 * lobby screen.
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

	public void setSettingsString(final String[] settingsString) {
		this.settingsString = settingsString;
		lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyRulesChangedEventArgs(settingsString));
	}

	public Set<Integer> getPlayerIDs() {
		return players.keySet();
	}

	public int getPlayerCount() {
		return players.size();
	}

	public void addPlayer(final int id, final LobbyPlayerInfo name) {
		players.put(id, name);
		lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs(id, name));
	}
	
	public boolean allReady() {
		for(int i : players.keySet()) {
			if(!players.get(i).isReady()) return false;
		}
		return true;
	}
	
	public void resetReady() {
		for(int i : players.keySet()) {
			players.get(i).setReady(false);
		}
	}

	public LobbyPlayerInfo getPlayer(final int id) {
		return players.get(id);
	}

	public void removePlayer(final int playerID) {
		if (players.containsKey(playerID)) {
			players.remove(playerID);
			lobbyStateChangedEvent.fire(new LobbyChangedEventArgs.LobbyPlayerLeftEventArgs(playerID));
		} else {
			throw new IllegalArgumentException("No player with ID " + playerID + " in the lobby.");
		}
	}

	public boolean containsPlayer(final int playerID) {
		return players.containsKey(playerID);
	}
}
