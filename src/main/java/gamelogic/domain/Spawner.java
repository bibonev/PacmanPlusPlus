package main.java.gamelogic.domain;

/**
 * Represents a spawner countdown in the game world with a specified
 * display colour which, upon reaching 0, will be removed and replaced
 * with some other entity. The purpose of this is so that players can
 * see when something (ie. another player, or a ghost) is about to
 * spawn in the game world.
 * @author Tom Galvin
 *
 */
public class Spawner extends Entity {
	private static final int MIN_NUMBER = 1, MAX_NUMBER = 5;
	private int timeRemaining;
	private boolean expired;
	private Entity entity;
	private SpawnerColor color;
	
	/**
	 * Instantiate a new {@link Spawner} with the given display
	 * colour, time to spawn, and entity which will be spawned.
	 * 
	 * @param spawnTime The number which the countdown will start at.
	 * @param entity The entity which will be spawned.
	 * @param color The colour to display the spawner as on the
	 * player's display.
	 */
	public Spawner(int spawnTime, Entity entity, SpawnerColor color) {
		this.timeRemaining = spawnTime * 2;
		this.entity = entity;
		this.color = color;
	}
	
	/**
	 * Gets the time remaining before the spawner spawns its contained
	 * entity.
	 * 
	 * @return The time remaining, in half-game-ticks (ie. if {@link Spawner#getTimeRemaining()}
	 * returns 10, then 5 game ticks until the entity is spawned).
	 */
	public int getTimeRemaining() {
		int number = (timeRemaining) / 2;
		
		if(number < MIN_NUMBER) number = MIN_NUMBER;
		if(number > MAX_NUMBER) number = MAX_NUMBER;
		return number;
	}
	
	@Override
	public void gameStep(Game game) {
		if(!expired) {
			timeRemaining -= 1;
			
			if(timeRemaining <= 0) {
				this.expired = true;
			}
		}
	}
	
	/**
	 * Determines if this spawner is expired (ie. due for removal and replacement with
	 * the contained entity).
	 * 
	 * @return {@code true} if the entity is expired - {@code false} otherwise.
	 */
	public boolean isExpired() {
		return expired;
	}
	
	/**
	 * Gets the contained entity within this spawner which will be spawned.
	 * 
	 * If this entity is contained within a world which is running on a multiplayer client
	 * rather than a server, then this may return {@code null} - indicating that the
	 * server will take care of spawning the entity, rather than the game logic running
	 * on the current client. 
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the display colour to use for this {@link Spawner} on player's machines.
	 * @return A platform agnostic representation of the colour 
	 */
	public SpawnerColor getColor() {
		return color;
	}
	
	/**
	 * An enumeration for potential spawner countdown colours.
	 * 
	 * @author Tom Galvin
	 *
	 */
	public static enum SpawnerColor {
		/**
		 * Green spawner colours, representing the local player about to spawn.
		 */
		GREEN,
		
		/**
		 * Yellow spawner colours, representing other players about to spawn.
		 */
		YELLOW,
		
		/**
		 * Red spawner colours, representing ghosts about to spawn.
		 */
		RED,
		
		/**
		 * Cyan spawner colours, reserver for anything else.
		 */
		CYAN
	}
}
