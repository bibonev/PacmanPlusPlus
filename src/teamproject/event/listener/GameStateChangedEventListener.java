package teamproject.event.listener;

import teamproject.event.arguments.GameStateChangedEventArgs;

public interface GameStateChangedEventListener {
	void onGameStateChanged(GameStateChangedEventArgs args);
}
