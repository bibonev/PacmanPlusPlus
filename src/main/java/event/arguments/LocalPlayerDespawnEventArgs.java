package main.java.event.arguments;

public class LocalPlayerDespawnEventArgs {
	private boolean canRespawn;
	private String message;
	
	public LocalPlayerDespawnEventArgs(boolean canRespawn, String message) {
		super();
		this.canRespawn = canRespawn;
		this.message = message;
	}
	
	public boolean isCanRespawn() {
		return canRespawn;
	}
	
	public String getMessage() {
		return message;
	}
}
