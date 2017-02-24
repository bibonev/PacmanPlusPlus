package teamproject.gamelogic.domain;

import teamproject.constants.EntityType;
import teamproject.event.Event;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.listener.EntityMovedListener;

public abstract class Entity {

	private Position position;
	private int id = -1;
	private World world;

	private Event<EntityMovedListener, EntityMovedEventArgs> onMoved;
	private EntityType type;

	public Entity() {
		onMoved = new Event<>((l, p) -> l.onEntityMoved(p));
	}

	public Position getPosition() {
		if (position != null) {
			return position;
		} else {
			throw new IllegalStateException(
					"Position not set for entity ID " + id + " of type " + getClass().getSimpleName());
		}
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public int getID() {
		return id;
	}

	protected void setID(final int id) {
		this.id = id;
	}

	protected void setWorld(final World world) {
		this.world = world;
	}

	public Event<EntityMovedListener, EntityMovedEventArgs> getOnMovedEvent() {
		return onMoved;
	}

	public void setType(final EntityType type) {
		this.type = type;
	}

	public EntityType getType() {
		return type;
	}
}
