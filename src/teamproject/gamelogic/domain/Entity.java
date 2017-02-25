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
		if(this.position != null) {
			return this.position;
		} else {
			throw new IllegalStateException("Position not set for entity ID " + id + " of type " + getClass().getSimpleName());
		}
	}
	
	public void setPosition(Position position) {
		this.position = position;
		getOnMovedEvent().fire(
				new EntityMovedEventArgs(position.getRow(), position.getColumn(), 0, this));
	}
	
	public int getID() {
		return this.id;
	}
	
	protected void setID(int id) {
		this.id = id;
	}
	
	protected void setWorld(World world) {
		this.world = world;
	}
	public Event<EntityMovedListener, EntityMovedEventArgs> getOnMovedEvent() {
		return onMoved;
	}
	public void setType(EntityType type){
		this.type=type;
	}
	public EntityType getType(){
		return type;
	}
}
