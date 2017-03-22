package main.java.gamelogic.domain;

import main.java.event.Event;
import main.java.event.arguments.PlayerAbilityUsedEventArgs;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.listener.PlayerAbilityUsedListener;
import main.java.event.listener.PlayerCooldownChangedListener;

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


	public RemoteSkillSet(Player owner) {
		this.owner = owner;
		onPlayerAbilityUsed = new Event<>((l, a) -> l.onPlayerAbilityUsed(a));
		onPlayerCooldownChanged = new Event<>((l, a) -> l.onPlayerCooldownChanged(a));
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

	@Override
	public void incrementCooldown() {
		// nop
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
