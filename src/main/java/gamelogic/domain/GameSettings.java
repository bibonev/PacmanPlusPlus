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
	private boolean aiPlayer;
	
	public GameSettings() {
		initialPlayerLives = 3;
		aiPlayer = true;
	}
		
	public int getInitialPlayerLives() {
		return initialPlayerLives;
	}
	
	public boolean getAIPlayer(){
		return aiPlayer;
	}
	
	public void setAIPlayer(boolean b){
		aiPlayer = b;
	}
	
	public void setInitialPlayerLives(int initialPlayerLives) {
		this.initialPlayerLives = initialPlayerLives;
	}
	
	public String[] toDisplayString() {
		return new String[] {
				String.format("Player lives: %d", getInitialPlayerLives()),
				String.format("AI Player %s", getAIPlayer() ? "enabled" : "disabled")
		};
	}
}
