package teamproject.gamelogic.domain;

/**
 * Represent a game (round?)
 * 
 * @author aml
 *
 */
public class Game {
	private World world;
	private GameSettings gameSettings;
	private ControlledPlayer player;
	private boolean isServerGame;

	public Game(final World world, final GameSettings gameSettings, final ControlledPlayer player, final boolean isServerGame) {
		this.world = world;
		this.gameSettings = gameSettings;
		this.player = player;
		this.isServerGame = isServerGame;
	}

	/**
	 * Fetch the game's world
	 * 
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Fetch in game settings
	 * 
	 * @return the settings
	 */
	public GameSettings getGameSettings() {
		return gameSettings;
	}

	/**
	 * Update the world
	 * 
	 * @param world
	 *            the new world
	 */
	public void setWorld(final World world) {
		this.world = world;
	}

	/**
	 * Update the in game settings
	 * 
	 * @param gameSettings
	 *            the new settings
	 */
	public void setGameSettings(final GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	/**
	 * Fetch the controlled player
	 * 
	 * @return the controlled player
	 */
	public ControlledPlayer getPlayer() {
		return player;
	}
	
	/**
	 * Determines if this {@link Game} instance is one which runs on the serve.r
	 * 
	 * @return Returns {@code true} if this game is to be ran on the server. Otherwise,
	 * returns {@code false} if this game instance is one which is rendered and ran on
	 * the client.
	 */
	public boolean isServerGame() {
		return isServerGame;
	}
}
