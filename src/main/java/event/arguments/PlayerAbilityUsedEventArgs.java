package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

public class PlayerAbilityUsedEventArgs {
	private Player player;
	private char slot;
	
	public PlayerAbilityUsedEventArgs(Player player, char slot) {
		this.player = player;
		this.slot = slot;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public char getSlot() {
		return slot;
	}
}
