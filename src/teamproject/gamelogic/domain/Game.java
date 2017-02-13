package teamproject.gamelogic.domain;

public class Game {

	private long id;
	private World world;
	private GameSettings gameSettings;

	public Game(final long id, final World world, final GameSettings gameSettings) {
		this.id = id;
		this.world = world;
		this.gameSettings = gameSettings;
	}

	public long getId() {
		return id;
	}

	public World getWorld() {
		return world;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setWorld(final World world) {
		this.world = world;
	}

	public void setGameSettings(final GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

}
