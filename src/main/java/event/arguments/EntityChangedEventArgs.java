package teamproject.event.arguments;

import teamproject.gamelogic.domain.World;

public class EntityChangedEventArgs {
	private int entityID;
	private World world;
	
	public EntityChangedEventArgs(int entityID, World world) {
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
