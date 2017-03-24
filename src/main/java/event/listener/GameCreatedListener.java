package main.java.event.listener;

import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.Game;
import main.java.event.arguments.GameCreatedEventArgs;

/**
 * Represents objects which receive messages when a {@link Game}
 * object, representing all of the in-game state (such as present
 * non-dead players, enemies, the map, entities, etc.), along
 * with the {@link GameLogic} object representing the way the
 * game transforms through time, is created.
 *
 * @author Tom Galvin
 *
 */
public interface GameCreatedListener {
	/**
	 * Called when the game and game state are created.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	public void onGameCreated(GameCreatedEventArgs args);
}
