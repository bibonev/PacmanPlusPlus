package main.java.gamelogic.core;

public class LobbyPlayerInfo {
	private String name;
	private int id;
	private boolean inGame;
	private boolean ready;

	public LobbyPlayerInfo(final int id, final String name) {
		this.id = id;
		this.name = name;
		this.inGame = false;
		this.ready = false;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}
	
	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
