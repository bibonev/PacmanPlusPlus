package teamproject.gamelogic.domain;

import teamproject.constants.CellState;
import teamproject.constants.EntityType;
import teamproject.event.arguments.PlayerMovedEventArgs;

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
				new PlayerMovedEventArgs(getPosition().getRow(), getPosition().getColumn(), angle, this));
	}
	
	@Override
	public boolean setPosition(Position position) {
		boolean returnValue = super.setPosition(position);
		eatDot();
		return returnValue;
	}

	protected void eatDot() {
		if(getWorld() != null && !getWorld().isRemote()) {
			Cell currentCell = getWorld().getMap().getCell(getPosition());
			
			if(currentCell.getState() == CellState.FOOD) {
				currentCell.setState(CellState.EMPTY);
			}
		}
	}
}
