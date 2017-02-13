package teamproject.event.listener;

import teamproject.event.arguments.container.GamePausedEventArguments;

public interface GamePausedEventListener {
	public void onGamePaused(GamePausedEventArguments args);
}
