package main.java.gamelogic.domain;

import main.java.event.Event;
import main.java.event.arguments.*;
import main.java.event.listener.*;

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
public class RemoteSkillSet implements SkillSet {
	private Player owner;
	private Event<PlayerAbilityUsedListener, PlayerAbilityUsedEventArgs> onPlayerAbilityUsed;
	private Event<PlayerCooldownChangedListener,PlayerCooldownChangedEventArgs> onPlayerCooldownChanged;
	private Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> onPlayerLaserActivated;
    private Event<PlayerShieldActivatedListener,PlayerShieldActivatedEventArgs> onPlayerShieldActivated;
    private Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> onPlayerShieldRemoved;



	public RemoteSkillSet(Player owner) {
		this.owner = owner;
		onPlayerAbilityUsed = new Event<>((l, a) -> l.onPlayerAbilityUsed(a));
		onPlayerCooldownChanged = new Event<>((l, a) -> l.onPlayerCooldownChanged(a));
		onPlayerLaserActivated = new Event<>((l, a) -> l.onPlayerLaserActivated(a));
        onPlayerShieldActivated = new Event<>((l, a) -> l.onPlayerShieldActivated(a));
        onPlayerShieldRemoved = new Event<>((l, a) -> l.onPlayerShieldRemoved(a));
	}


	@Override
	public Event<PlayerCooldownChangedListener, PlayerCooldownChangedEventArgs> getOnPlayerCooldownChanged() {
		return onPlayerCooldownChanged;
	}

	/**
	 * Use Q skill.
	 */
	@Override
	public void activateQ() {
	    onPlayerAbilityUsed.fire(new PlayerAbilityUsedEventArgs(owner, 'q'));
	}

	/**
	 * Use W skill.
	 */
	@Override
	public void activateW() {
        onPlayerAbilityUsed.fire(new PlayerAbilityUsedEventArgs(owner, 'w'));
	}

	public Event<PlayerAbilityUsedListener, PlayerAbilityUsedEventArgs> getOnPlayerAbilityUsed() {
		return onPlayerAbilityUsed;
	}

    public Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> getOnPlayerLaserActivated() {
        return onPlayerLaserActivated;
    }

    @Override
    public Event<PlayerShieldActivatedListener, PlayerShieldActivatedEventArgs> getOnPlayerShieldActivated() {
        return onPlayerShieldActivated;
    }

    @Override
    public Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> getOnPlayerShieldRemoved() {
        return onPlayerShieldRemoved;
    }

    @Override
	public void incrementCooldown() {
		// nop
	}

    @Override
    public void removeShield() {

    }

    @Override
	public void setQ(Ability q) {
		// nop
	}

	@Override
	public void setW(Ability w) {
		// nop
	}

	@Override
	public Ability getQ() {
		return null;
	}

	@Override
	public Ability getW() {
		return null;
	}
}
