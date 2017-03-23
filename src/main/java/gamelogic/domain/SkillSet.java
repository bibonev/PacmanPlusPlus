package main.java.gamelogic.domain;
/**
 * Represent a player's inventory
 */

import main.java.event.Event;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.arguments.PlayerLaserActivatedEventArgs;
import main.java.event.arguments.PlayerShieldActivatedEventArgs;
import main.java.event.arguments.PlayerShieldRemovedEventArgs;
import main.java.event.listener.PlayerCooldownChangedListener;
import main.java.event.listener.PlayerLaserActivatedListener;
import main.java.event.listener.PlayerShieldActivatedListener;
import main.java.event.listener.PlayerShieldRemovedListener;

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
	public Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> getOnPlayerLaserActivated();
	public Event<PlayerShieldActivatedListener,PlayerShieldActivatedEventArgs> getOnPlayerShieldActivated();
    public Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> getOnPlayerShieldRemoved();

	/**
	 * Use Q skill.
	 */
	public void activateQ();

	/**
	 * Use W skill.
	 */
	public void activateW();


	public void incrementCooldown();

    public void removeShield();

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
