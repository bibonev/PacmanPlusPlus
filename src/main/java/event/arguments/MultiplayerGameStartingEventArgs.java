package main.java.event.arguments;

import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.Map;

public class MultiplayerGameStartingEventArgs {
	private boolean serverMode;
	private GameSettings settings;
	private int localPlayerID;
	private String localUsername;
	private Map map;

	/**
	 * Create event args for when a multiplayer game is being created on the
	 * server side.
	 *
	 * @param settings
	 *            The settings to use for initialising the game and world.
	 */
	public MultiplayerGameStartingEventArgs(final GameSettings settings) {
		this.settings = settings;
		serverMode = true;
	}

	/**
	 * Create event args for when a multiplayer game is being created on the
	 * client side.
	 *
	 * @param settings
	 *            The settings to use for initialising the game and world.
	 * @param localPlayerID
	 *            The ID of the local player.
	 * @param localUsername
	 *            The username of the local player;
	 * @param map The map being played on the server.
	 */
	public MultiplayerGameStartingEventArgs(final GameSettings settings, final int localPlayerID,
			final String localUsername, Map map) {
		this.settings = settings;
		serverMode = false;
		this.localPlayerID = localPlayerID;
		this.localUsername = localUsername;
		this.map = map;
	}
	
	/**
	 * Gets the map which this game is being played on.
	 * @return
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Gets the settings/ruleset that this game is being played by.
	 * @return
	 */
	public GameSettings getSettings() {
		return settings;
	}

	/**
	 * Determines if this world is being created for the server or for connected
	 * clients.
	 *
	 * @return Returns {@code true} if the game starting is the server's game -
	 *         {@code false} if the game is starting on the client side.
	 */
	public boolean isServer() {
		return serverMode;
	}

	/**
	 * Gets the ID of the local player.
	 * @return
	 */
	public int getLocalPlayerID() {
		if (!serverMode) {
			return localPlayerID;
		} else {
			throw new IllegalStateException("Can't get local player ID when generating multiplayer game for server.");
		}
	}

	/**
	 * Gets the username of the local player.
	 * @return
	 */
	public String getLocalUsername() {
		if (!serverMode) {
			return localUsername;
		} else {
			throw new IllegalStateException(
					"Can't get local player username when generating multiplayer game for server.");
		}
	}
}
