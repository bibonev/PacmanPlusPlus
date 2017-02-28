package teamproject.gamelogic.domain;

import teamproject.constants.EntityType;
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
	private EntityType type;

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

	/**
	 * Update the entity's position
	 * 
	 * @param position
	 *            the new position
	 */
	public void setPosition(final Position position) {
		this.position = position;
		getOnMovedEvent().fire(
				new EntityMovedEventArgs(position.getRow(), position.getColumn(), 0, this));
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

	/**
	 * Update the entity's type
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(final EntityType type) {
		this.type = type;
	}

	/**
	 * Fetch the entity's type
	 * 
	 * @return enum value showing the type
	 */
	public EntityType getType() {
		return type;
	}
}
