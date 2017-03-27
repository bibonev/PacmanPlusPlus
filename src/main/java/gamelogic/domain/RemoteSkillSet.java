package main.java.gamelogic.domain;

import main.java.event.Event;
import main.java.event.arguments.*;
import main.java.event.listener.*;

/**
 * The player's skillset. Contains 3 items that will be bound to the Q,W and E
 * keys.
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public class RemoteSkillSet implements SkillSet {
	
	/** The owner. */
	private Player owner;
	
	/** The on player ability used. */
	private Event<PlayerAbilityUsedListener, PlayerAbilityUsedEventArgs> onPlayerAbilityUsed;
	
	/** The on player cooldown changed. */
	private Event<PlayerCooldownChangedListener,PlayerCooldownChangedEventArgs> onPlayerCooldownChanged;
	
	/** The on player laser activated. */
	private Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> onPlayerLaserActivated;
    
    /** The on player shield activated. */
    private Event<PlayerShieldActivatedListener,PlayerShieldActivatedEventArgs> onPlayerShieldActivated;
    
    /** The on player shield removed. */
    private Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> onPlayerShieldRemoved;



	/**
	 * Instantiates a new remote skill set.
	 *
	 * @param owner the owner
	 */
	public RemoteSkillSet(Player owner) {
		this.owner = owner;
		onPlayerAbilityUsed = new Event<>((l, a) -> l.onPlayerAbilityUsed(a));
		onPlayerCooldownChanged = new Event<>((l, a) -> l.onPlayerCooldownChanged(a));
		onPlayerLaserActivated = new Event<>((l, a) -> l.onPlayerLaserActivated(a));
        onPlayerShieldActivated = new Event<>((l, a) -> l.onPlayerShieldActivated(a));
        onPlayerShieldRemoved = new Event<>((l, a) -> l.onPlayerShieldRemoved(a));
	}


	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.SkillSet#getOnPlayerCooldownChanged()
	 */
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

	/**
	 * Gets the on player ability used.
	 *
	 * @return the on player ability used
	 */
	public Event<PlayerAbilityUsedListener, PlayerAbilityUsedEventArgs> getOnPlayerAbilityUsed() {
		return onPlayerAbilityUsed;
	}

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#getOnPlayerLaserActivated()
     */
    public Event<PlayerLaserActivatedListener,PlayerLaserActivatedEventArgs> getOnPlayerLaserActivated() {
        return onPlayerLaserActivated;
    }

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#getOnPlayerShieldActivated()
     */
    @Override
    public Event<PlayerShieldActivatedListener, PlayerShieldActivatedEventArgs> getOnPlayerShieldActivated() {
        return onPlayerShieldActivated;
    }

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#getOnPlayerShieldRemoved()
     */
    @Override
    public Event<PlayerShieldRemovedListener,PlayerShieldRemovedEventArgs> getOnPlayerShieldRemoved() {
        return onPlayerShieldRemoved;
    }

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#incrementCooldown()
     */
    @Override
	public void incrementCooldown() {
	}

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#removeShield()
     */
    @Override
    public void removeShield() {

    }

    /* (non-Javadoc)
     * @see main.java.gamelogic.domain.SkillSet#setQ(main.java.gamelogic.domain.Ability)
     */
    @Override
	public void setQ(Ability q) {
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.SkillSet#setW(main.java.gamelogic.domain.Ability)
	 */
	@Override
	public void setW(Ability w) {
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.SkillSet#getQ()
	 */
	@Override
	public Ability getQ() {
		return null;
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.SkillSet#getW()
	 */
	@Override
	public Ability getW() {
		return null;
	}
}
