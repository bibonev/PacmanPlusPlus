package main.java.event.arguments;

import main.java.gamelogic.domain.ControlledPlayer;

public class LocalPlayerSpawnEventArgs {
	private ControlledPlayer player;

	public LocalPlayerSpawnEventArgs(ControlledPlayer player) {
		this.player = player;
	}

	public ControlledPlayer getPlayer() {
		return player;
	}
}
