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
	private String mapName;
	private int ghostCount;
	
	public GameSettings() {
		initialPlayerLives = 3;
		aiPlayer = true;
		mapName = "Default Map";
		ghostCount = 2;
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
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public String[] toDisplayString() {
		return new String[] {
				String.format("Player lives: %d", getInitialPlayerLives()),
				String.format("Ghost count: %d", getGhostCount()),
				String.format("Map: %s", getMapName()),
				String.format("AI Player %s", getAIPlayer() ? "enabled" : "disabled")
		};
	}

	public int getGhostCount() {
		return ghostCount;
	}
	
	public void setGhostCount(int ghostCount) {
		this.ghostCount = ghostCount;
	}
}
