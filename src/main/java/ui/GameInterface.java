package main.java.ui;

import main.java.event.Event;
import main.java.event.listener.PlayerLeavingGameListener;
import main.java.gamelogic.core.Lobby;

/**
 * Represents a game interface which represents a multiplayer game lobby in
 * some way, including support for a countdown timer, and also provides events
 * which are fired when the player attempts to leave the currently active
 * game such that networking systems may gracefully close the connection.
 * 
 * @author Tom Galvin
 *
 */
public interface GameInterface {
	
	void setAIPlayer(boolean ai);
	
	void setLives(int lives);
	
	/**
	 * Set the currently active multiplayer game lobby.
	 * @param lobby The {@link Lobby} object representing the lobby to use.
	 */
	void setLobby(Lobby lobby);

	/**
	 * Gets the event fired when a player leaves the game.
	 * @return
	 */
	public Event<PlayerLeavingGameListener,Object> getOnPlayerLeavingGame();

	/**
	 * Called when the player leaves the game.
	 */
	void onPlayerLeavingGame();

	/**
	 * Called when the lobby timer is to start.
	 */
	void timer();
}
