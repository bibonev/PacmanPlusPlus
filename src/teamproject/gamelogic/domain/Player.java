package teamproject.gamelogic.domain;

/**
 * Represent an abstract player
 *
 * @author aml
 *
 */

public abstract class Player extends Entity {
	private String name;
	private double angle;

	public Player(final String name) {
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
	}
}
