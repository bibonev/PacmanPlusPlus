package teamproject.gamelogic.domain;

public class Game {
	private World world;
	private GameSettings gameSettings;
	private ControlledPlayer player;

	public Game(final World world, final GameSettings gameSettings, final ControlledPlayer player) {
		this.world = world;
		this.gameSettings = gameSettings;
		this.player = player;
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

	public ControlledPlayer getPlayer() {
		return player;
	}
}
