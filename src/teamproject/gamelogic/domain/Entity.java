package teamproject.gamelogic.domain;

public abstract class Entity {
	private Position position;
	private int id = -1;
	private World world;

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
}
