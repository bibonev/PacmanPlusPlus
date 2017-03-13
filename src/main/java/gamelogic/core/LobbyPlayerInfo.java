package main.java.gamelogic.core;

public class LobbyPlayerInfo {
	private String name;
	private int id;

	public LobbyPlayerInfo(final int id, final String name) {
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
