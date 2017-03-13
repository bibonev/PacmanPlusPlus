package main.java.networking.integration;

import main.java.gamelogic.domain.World;

public interface GameWorldListener {
	public void onGameWorldChanged(World world);
}
