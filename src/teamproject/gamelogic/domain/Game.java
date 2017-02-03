package teamproject.gamelogic.domain;

public class Game {

	private World world;
	private GameSettings gameSettings;

	public Game(final World world, final GameSettings gameSettings) {
		this.world = world;
		this.gameSettings = gameSettings;
	}

	public World getWorld() {
		return world;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setWorld(final World world) {
		this.world = world;
	}

	public void setGameSettings(final GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

}
