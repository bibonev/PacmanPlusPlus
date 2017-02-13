package teamproject.event.listener;

import teamproject.event.arguments.container.GameStateChangedEventArguments;

public interface GameStateChangedEventListener {
	public void onGamePaused(GameStateChangedEventArguments args);
}
