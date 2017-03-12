package teamproject.gamelogic.domain;
/**
 * Represent a player's inventory
 */

import teamproject.abilities.Ability;

/**
 * The player's skillset. Contains 3 items that will be bound to the Q,W and E
 * keys.
 * 
 * @author Lyubomir Pashev
 *
 */
public class SkillSet {

	private Ability q;
	private Ability w;
	private Ability e;
	// private Ability r;

	public SkillSet() {
	}

	/**
	 * Set q ability.
	 * 
	 * @param q
	 */
	public void setQ(Ability q) {
		this.q = q;
	}

	/**
	 * Set q ability.
	 * 
	 * @param e
	 */
	public void setE(Ability e) {
		this.e = e;
	}

	/**
	 * Set w ability.
	 * 
	 * @param w
	 */
	public void setW(Ability w) {
		this.w = w;
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

	/**
	 * Use E skill.
	 */
	public void activateE() {
		e.activate();
	}

	/*
	 * maybe we'll add a 4th skill if we have time/think of that many skills
	 *
	 * public void activateR(){ r.activate(); }
	 */
}
