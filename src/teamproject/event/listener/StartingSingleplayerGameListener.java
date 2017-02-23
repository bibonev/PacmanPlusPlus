package teamproject.event.listener;

import teamproject.event.arguments.GameStartingEventArgs;

public interface StartingSingleplayerGameListener {
	public void onStartingGame(GameStartingEventArgs eventArgs);
}
