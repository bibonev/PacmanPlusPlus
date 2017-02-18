package teamproject.event.listener;

import teamproject.event.arguments.GameStateChangedEventArguments;

public interface GameStateChangedEventListener {
	void onGameStateChanged(GameStateChangedEventArguments args);
}
