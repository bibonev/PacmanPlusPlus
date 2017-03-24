package main.java.event.listener;

import main.java.event.arguments.GameSettingsChangedEventArgs;
import main.java.gamelogic.domain.GameSettings;

/**
 * Represents objects which receive messages when a new set of currently 
 * active game settings are chosen by the user.
 * 
 * @author Rose Kirtley
 *
 */
public interface GameSettingsChangedEventListener {
	/**
	 * Called when a new {@link GameSettings} object has been chosen.
	 * 
	 * @param args Information or parameters regarding/describing the event.
	 */
	void onGameSettingsChanged(GameSettingsChangedEventArgs args);
}
