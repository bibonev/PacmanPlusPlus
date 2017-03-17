package main.java.gamelogic.domain;

/**
 * Represent in game settings
 *
 * @author aml
 *
 */
public class GameSettings {
	// If you add any settings in here, please create an issue
	// on the GitHub repository so the network code can be
	// updated accordingly
	
	private int initialPlayerLives;
	
	public GameSettings() {
		initialPlayerLives = 3;
		
	}
	
	public int getInitialPlayerLives() {
		return initialPlayerLives;
	}
	
	public void setInitialPlayerLives(int initialPlayerLives) {
		this.initialPlayerLives = initialPlayerLives;
	}
	
	@Override
	public String toString() {
		return String.format("Settings:\nInitial lives: %d", getInitialPlayerLives());
	}
}
