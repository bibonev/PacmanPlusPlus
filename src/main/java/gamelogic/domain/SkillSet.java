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
public class SkillSet {

	private Ability q;
	private Ability w;
	private Ability e;
	// private Ability r;

	/**
	 * Set q ability.
	 *
	 * @param q
	 */
	public void setQ(final Ability q) {
		this.q = q;
	}


	/**
	 * Set w ability.
	 *
	 * @param w
	 */
	public void setW(final Ability w) {
		this.w = w;
	}

	/**
	 * Get q ability.
	 *
	 */
	public Ability getQ() {
		return q;
	}

	/**
	 * Get W ability.
	 *
	 */
	public Ability getW() {
		return w;
	}

	/**
	 * Use Q skill.
	 */
	public void activateQ() {
		q.activate();
	}

	/**
	 * Use W skill.
	 */
	public void activateW() {
		w.activate();
	}

}
