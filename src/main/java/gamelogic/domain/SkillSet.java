package main.java.gamelogic.domain;
/**
 * Represent a player's inventory
 */

import main.java.event.Event;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.listener.PlayerCooldownChangedListener;

/**
 * The player's skillset. Contains 3 items that will be bound to the Q,W and E
 * keys.
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public interface SkillSet {
	public Event<PlayerCooldownChangedListener, PlayerCooldownChangedEventArgs> getOnPlayerCooldownChanged();

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
