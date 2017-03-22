package main.java.gamelogic.domain;
/**
 * Represent a player's inventory
 */

/**
 * The player's skillset. Contains 3 items that will be bound to the Q,W and E
 * keys.
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public interface SkillSet {

	/**
	 * Use Q skill.
	 */
	public void activateQ();

	/**
	 * Use W skill.
	 */
	public void activateW();

	public void incrementCooldown();

	/**
	 * Set q ability.
	 *
	 * @param q
	 */
	public void setQ(final Ability q);


	/**
	 * Set w ability.
	 *
	 * @param w
	 */
	public void setW(final Ability w);

	/**
	 * Get q ability.
	 *
	 */
	public Ability getQ();

	/**
	 * Get W ability.
	 *
	 */
	public Ability getW();

}
