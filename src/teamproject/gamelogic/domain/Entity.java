package teamproject.gamelogic.domain;

public abstract class Entity {
	private Position position;
	private int id = -1;
	private World world;
	
	public Entity() {
		
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
}
