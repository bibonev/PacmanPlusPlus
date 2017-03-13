package main.java.event.arguments;

import main.java.gamelogic.domain.World;

public class EntityChangedEventArgs {
	private int entityID;
	private World world;

	public EntityChangedEventArgs(final int entityID, final World world) {
		this.entityID = entityID;
		this.world = world;
	}

	public int getEntityID() {
		return entityID;
	}

	public World getWorld() {
		return world;
	}
}
