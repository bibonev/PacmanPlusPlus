package teamproject.gamelogic.domain;

import teamproject.constants.EntityType;
import teamproject.event.arguments.EntityMovedEventArgs;

public abstract class Player extends Entity {
	private String name;
	private double angle;

	public Player(final String name) {
		setType(EntityType.PLAYER);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
		getOnMovedEvent().fire(
				new EntityMovedEventArgs(getPosition().getRow(), getPosition().getColumn(), angle, this));
	}
}
