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
import org.mockito.cglib.core.Local;

/**
 * The player's skillset. Contains 3 items that will be bound to the Q,W and E
 * keys.
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public class LocalSkillSet implements SkillSet {

	private final Event<PlayerCooldownChangedListener,PlayerCooldownChangedEventArgs> onPlayerCooldownChanged;
	private Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> onPlayerLaserActivated;
	private Event<PlayerShieldActivatedListener,PlayerShieldActivatedEventArgs> onPlayerShieldActivated;
	private Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> onPlayerShieldRemoved;
	private Ability q;
	private Ability w;
	// private Ability r;

	public LocalSkillSet()
	{
		onPlayerCooldownChanged = new Event<>((l, a) -> l.onPlayerCooldownChanged(a));
		onPlayerLaserActivated = new Event<>((l, a) -> l.onPlayerLaserActivated(a));
		onPlayerShieldActivated = new Event<>((l, a) -> l.onPlayerShieldActivated(a));
		onPlayerShieldRemoved = new Event<>((l, a) -> l.onPlayerShieldRemoved(a));
	}


	@Override
	public Event<PlayerCooldownChangedListener, PlayerCooldownChangedEventArgs> getOnPlayerCooldownChanged() {
		return onPlayerCooldownChanged;
	}

	@Override
	public Event<PlayerLaserActivatedListener, PlayerLaserActivatedEventArgs> getOnPlayerLaserActivated() {
		return onPlayerLaserActivated;
	}

	@Override
	public Event<PlayerShieldActivatedListener,PlayerShieldActivatedEventArgs> getOnPlayerShieldActivated() {
		return onPlayerShieldActivated;
	}

	@Override
	public Event<PlayerShieldRemovedListener, PlayerShieldRemovedEventArgs> getOnPlayerShieldRemoved() {
		return onPlayerShieldRemoved;
	}

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
	@Override
	public void activateQ() {
		getOnPlayerLaserActivated().fire(new PlayerLaserActivatedEventArgs(getQ().getOwner(), getQ().getOwner().getAngle(), getQ().getCD()));
		q.activate();
	}

	/**
	 * Use W skill.
	 */
	@Override
	public void activateW() {
		w.activate();
		getOnPlayerShieldActivated().fire(new PlayerShieldActivatedEventArgs(getW().getOwner(), getW().getShieldValue()));
	}

	public static LocalSkillSet createDefaultSkillSet(Player owner) {
		// Create SkillSet for each Player when added
        PacShield shield = new PacShield();
        shield.setCD(40);
        shield.setOwner(owner);
        PacLaser laser = new PacLaser();
        laser.setCD(20);
        laser.setOwner(owner);
        LocalSkillSet skillSet = new LocalSkillSet();
        skillSet.setQ(laser); // set E button to activate laser
        skillSet.setW(shield); // set W button to activate shield
        
        return skillSet;
	}


	@Override
	public void incrementCooldown() {
		if(getQ().incrementCooldown())
			getOnPlayerCooldownChanged().fire(new PlayerCooldownChangedEventArgs(getQ().getOwner(), getQ().getCD(), 'q'));
		
		if(getW().incrementCooldown())
			getOnPlayerCooldownChanged().fire(new PlayerCooldownChangedEventArgs(getW().getOwner(), getW().getCD(), 'w'));
	}

	@Override
	public void removeShield() {
		getOnPlayerShieldRemoved().fire(new PlayerShieldRemovedEventArgs(getW().getOwner(), getW().getShieldValue()));
	}
}
