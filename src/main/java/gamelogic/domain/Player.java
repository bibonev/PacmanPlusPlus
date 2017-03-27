package main.java.gamelogic.domain;

import main.java.constants.CellState;
import main.java.event.arguments.PlayerMovedEventArgs;

/**
 * The Class Player.
 *
 * @author Tom Galvin
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 */
public abstract class Player extends Entity {
	
	/** The death reason. */
	private String deathReason;
	
	/** The name. */
	private String name;
	
	/** The angle. */
	private double angle;
	
	/** The dots eaten. */
	private int dotsEaten;
	
	/** The skill set. */
	private SkillSet skillSet;
    
    /** The laser fired. */
    private boolean laserFired;

	/**
	 * Instantiates a new player.
	 *
	 * @param name the name
	 */
	public Player(final String name) {
		super();
		this.name = name;
		dotsEaten = 0;
		this.laserFired = false;
	}

	/**
	 * Fetch the player's name.
	 *
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Fetch the player's angle.
	 *
	 * @return the angle as a double decimal number
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Gets the dots eaten.
	 *
	 * @return the dots eaten
	 */
	public int getDotsEaten() {
		return dotsEaten;
	}

	/**
	 * Get whether a laserFired has been fired.
	 *
	 * @return the laserFired
	 */
	public boolean getLaserFired(){
		return this.laserFired;
	}

	/**
	 * Set that a laserFired has been fired.
	 *
	 * @param laserFired the new laser fired
	 */
	public void setLaserFired(boolean laserFired){
		this.laserFired = laserFired;
	}
    
    
	/**
	 * Fetch the player's skillset.
	 *
	 * @return player skillset
	 */
	public SkillSet getSkillSet() {
		return skillSet;
	}


	/**
	 * Set the player's skillset.
	 *
	 * @param skillSet the new skill set
	 * @return player skillset
	 */
	public void setSkillSet(SkillSet skillSet) {
		this.skillSet = skillSet;
	}

	/**
	 * Update the player's angle.
	 *
	 * @param angle            the new angle
	 */
	public void setAngle(final double angle) {
		this.angle = angle;
		getOnMovedEvent()
				.fire(new PlayerMovedEventArgs(getPosition().getRow(), getPosition().getColumn(), angle, this));
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.Entity#setPosition(main.java.gamelogic.domain.Position)
	 */
	@Override
	public boolean setPosition(final Position position) {
		eatDot();
		final boolean returnValue = super.setPosition(position);
		return returnValue;
	}

	/**
	 * Eat dot.
	 */
	protected void eatDot() {
		if (getWorld() != null/* && !getWorld().isRemote() */) {
			final Cell currentCell = getWorld().getMap().getCell(getPosition());

			if (currentCell.getState() == CellState.FOOD) {
				currentCell.setState(CellState.EMPTY);
				dotsEaten++;
			}
		}
	}

	/**
	 * Gets the death reason.
	 *
	 * @return the death reason
	 */
	public String getDeathReason() {
		return deathReason;
	}

	/**
	 * Sets the death reason.
	 *
	 * @param deathReason the new death reason
	 */
	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}
	
	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.Entity#gameStep(main.java.gamelogic.domain.Game)
	 */
	@Override
	public void gameStep(Game game) {
		super.gameStep(game);
		if(this.getSkillSet() != null)
			this.getSkillSet().incrementCooldown();
	}
}
