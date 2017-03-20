package main.java.gamelogic.domain;

import main.java.event.Event;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.listener.EntityMovedListener;

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
	private boolean isKilledByLaser = false;

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

	protected boolean canSetPosition(final Position p) {
		return world == null || world.isOccupiable(p);
	}

	/**
	 * Update the entity's position
	 *
	 * @param position
	 *            the new position
	 */
	public boolean setPosition(final Position position) {
		if (canSetPosition(position)) {
			this.position = position;
			getOnMovedEvent().fire(new EntityMovedEventArgs(position.getRow(), position.getColumn(), this));
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
     * Fetch the entity's coolDown
     *
     * @return an boolean isKilledByLaser
     */
    public boolean getIsKilled() {
        return isKilledByLaser;
    }

    /**
     * Update the entity's isKilledByLaser
     *
     * @param isKilledByLaser
     *            the new isKilledByLaser
     */
    protected void setIsKilled(final boolean isKilledByLaser) {
        this.isKilledByLaser = isKilledByLaser;
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
		return world;
	}

	public void gameStep(final Game game) {
		// nothing to do in here yet
	}
}
