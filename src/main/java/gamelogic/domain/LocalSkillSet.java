package main.java.gamelogic.domain;
/**
 * Represent a player's inventory
 */

import main.java.event.Event;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.listener.PlayerCooldownChangedListener;
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
	private Ability q;
	private Ability w;
	// private Ability r;

	public LocalSkillSet()
	{
		onPlayerCooldownChanged = new Event<>((l, a) -> l.onPlayerCooldownChanged(a));
	}


	@Override
	public Event<PlayerCooldownChangedListener, PlayerCooldownChangedEventArgs> getOnPlayerCooldownChanged() {
		return onPlayerCooldownChanged;
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
		q.activate();
	}

	/**
	 * Use W skill.
	 */
	@Override
	public void activateW() {
		w.activate();
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
		getQ().incrementCooldown();
		getW().incrementCooldown();

		getOnPlayerCooldownChanged().fire(new PlayerCooldownChangedEventArgs(getQ().getOwner(), getQ().getCD(), 'q'));
		getOnPlayerCooldownChanged().fire(new PlayerCooldownChangedEventArgs(getW().getOwner(), getW().getCD(), 'w'));
	}
}
