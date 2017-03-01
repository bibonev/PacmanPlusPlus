package teamproject.networking.integration;

import teamproject.gamelogic.domain.World;

public interface GameWorldListener {
	public void onGameWorldChanged(World world);
}
