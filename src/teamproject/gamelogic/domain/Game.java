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

	public Game(final World world, final GameSettings gameSettings, final ControlledPlayer player) {
		this.world = world;
		this.gameSettings = gameSettings;
		this.player = player;
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
}
