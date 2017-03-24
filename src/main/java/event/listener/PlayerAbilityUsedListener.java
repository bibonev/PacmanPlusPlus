package main.java.event.listener;

import main.java.event.arguments.PlayerAbilityUsedEventArgs;

/**
 * Represents objects which receive messages when an ability
 * (skill) is used by the player.
 * 
 * @author Tom Galvin
 *
 */
public interface PlayerAbilityUsedListener {
	/**
	 * Called when an ability is used by a player.
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onPlayerAbilityUsed(PlayerAbilityUsedEventArgs args);
}
