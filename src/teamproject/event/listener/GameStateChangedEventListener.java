package teamproject.event.listener;

import teamproject.event.arguments.container.GameStateChangedEventArguments;

public interface GameStateChangedEventListener {
	void onGameStateChanged(GameStateChangedEventArguments args);
}
