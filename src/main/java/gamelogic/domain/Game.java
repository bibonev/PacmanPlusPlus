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
	private GameType type;
	private boolean started, ended;

	public Game(final World world, final GameSettings gameSettings, final GameType type) {
		this.world = world;
		this.gameSettings = gameSettings;
		this.type = type;
		ended = false;
	}

	/**
	 * This will return whether the game/round has ended or not.
	 * This should never return true if {@link Game#hasStarted()} returns
	 * false (ie. the game cannot end before it starts).
	 * 
	 * @return
	 */
	public boolean hasEnded() {
		return ended;
	}

	public void setEnded() {
		ended = true;
	}
	
	/**
	 * Gets whether the game has started yet or not.
	 * This will return false if the pre-game countdown is still counting down.
	 * @return
	 */
	public boolean hasStarted() {
		return started;
	}
	
	public void setStarted() {
		started = true;
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
	 * Determines the type of the game - ie. singleplayer, multiplayer
	 * serverside, or multiplayer clientside.
	 * 
	 * @return
	 */
	public GameType getGameType() {
		return type;
	}
}
