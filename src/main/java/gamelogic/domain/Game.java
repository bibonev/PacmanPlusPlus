package main.java.gamelogic.domain;

import main.java.constants.GameType;

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
	private GameType type;
	private boolean ended;

	public Game(final World world, final GameSettings gameSettings, final ControlledPlayer player,
			final GameType type) {
		this.world = world;
		this.gameSettings = gameSettings;
		this.player = player;
		this.type = type;
		ended = false;
	}

	public boolean hasEnded() {
		return ended;
	}

	public void setEnded() {
		ended = true;
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
	 * Determines the type of the game - ie. singleplayer, multiplayer
	 * serverside, or multiplayer clientside.
	 * 
	 * @return
	 */
	public GameType getGameType() {
		return type;
	}
}
