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

	/**
	 * Fetch the player's name
	 *
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Fetch the player's angle
	 *
	 * @return the angle as a double decimal number
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Update the player's angle
	 * 
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(final double angle) {
		this.angle = angle;
		getOnMovedEvent().fire(
				new EntityMovedEventArgs(getPosition().getRow(), getPosition().getColumn(), angle, this));
	}
}
