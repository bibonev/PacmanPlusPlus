package teamproject.gamelogic.core;

public class LobbyPlayerInfo {
	private String name;
	private int id;
	
	public LobbyPlayerInfo(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}
}
