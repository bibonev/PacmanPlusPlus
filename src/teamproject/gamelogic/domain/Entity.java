package teamproject.gamelogic.domain;

import teamproject.event.Event;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.listener.EntityMovedListener;

/**
 * Represent an entity that could be a player, ghost etc.
 *
 * @author aml
 *
 */

public abstract class Entity {

	private Position position;
	private int id = -1;
	private World world;

	private Event<EntityMovedListener, EntityMovedEventArgs> onMoved;

	public Entity() {
		onMoved = new Event<>((l, p) -> l.onEntityMoved(p));
	}

	/**
	 * Get the entity's position
	 *
	 * @return a position object
	 */
	public Position getPosition() {
		if (position != null) {
			return position;
		} else {
			throw new IllegalStateException(
					"Position not set for entity ID " + id + " of type " + getClass().getSimpleName());
		}
	}
	
	protected boolean canSetPosition(Position p) {
		return world == null || world.isOccupiable(p);
	}

	/**
	 * Update the entity's position
	 * 
	 * @param position
	 *            the new position
	 */
	public boolean setPosition(final Position position) {
		if(canSetPosition(position)) {
			this.position = position;
			getOnMovedEvent().fire(
					new EntityMovedEventArgs(position.getRow(), position.getColumn(), this));
		return true;
		} else {
			return false;
		}
	}

	/**
	 * Fetch the entity's id
	 * 
	 * @return an integer id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Update the entity's id
	 * 
	 * @param id
	 *            the new id
	 */
	protected void setID(final int id) {
		this.id = id;
	}

	/**
	 * Update the world
	 * 
	 * @param world
	 *            the new world
	 */
	protected void setWorld(final World world) {
		this.world = world;
	}

	/**
	 * Fetch the entity's on moved event
	 * 
	 * @return an event
	 */
	public Event<EntityMovedListener, EntityMovedEventArgs> getOnMovedEvent() {
		return onMoved;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void gameStep(Game game) {
		// nothing to do in here yet
	}
}
