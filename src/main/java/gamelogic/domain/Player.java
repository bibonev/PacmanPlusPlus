package main.java.gamelogic.domain;

import main.java.constants.CellState;
import main.java.event.arguments.PlayerMovedEventArgs;

/**
 *
 * @author Tom Galvin
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
*/
public abstract class Player extends Entity {
	private String deathReason;
	private String name;
	private double angle;
	private int dotsEaten;
	private SkillSet skillSet;
    private boolean laserFired;

	public Player(final String name) {
		this.name = name;
		dotsEaten = 0;
		this.laserFired = false;
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

	public int getDotsEaten() {
		return dotsEaten;
	}

	/**
	 * Get whether a laserFired has been fired
	 * @return the laserFired
	 */
	public boolean getLaserFired(){
		return this.laserFired;
	}

	/**
	 * Set that a laserFired has been fired
	 * @param laserFired
	 */
	public void setLaserFired(boolean laserFired){
		this.laserFired = laserFired;
	}
    
    
	/**
	 * Fetch the player's skillset
	 *
	 * @return player skillset
	 */
	public SkillSet getSkillSet() {
		return skillSet;
	}


	/**
	 * Set the player's skillset
	 *
	 * @return player skillset
	 */
	public void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	/**
	 * Update the player's angle
	 *
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(final double angle) {
		this.angle = angle;
		getOnMovedEvent()
				.fire(new PlayerMovedEventArgs(getPosition().getRow(), getPosition().getColumn(), angle, this));
	}

	@Override
	public boolean setPosition(final Position position) {
		eatDot();
		final boolean returnValue = super.setPosition(position);
		return returnValue;
	}

	protected void eatDot() {
		if (getWorld() != null/* && !getWorld().isRemote() */) {
			final Cell currentCell = getWorld().getMap().getCell(getPosition());

			if (currentCell.getState() == CellState.FOOD) {
				currentCell.setState(CellState.EMPTY);
				dotsEaten++;
			}
		}
	}

	public String getDeathReason() {
		return deathReason;
	}

	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}
	
	@Override
	public void gameStep(Game game) {
		super.gameStep(game);
		if(this.getSkillSet() != null)
			this.getSkillSet().incrementCooldown();
	}
}
