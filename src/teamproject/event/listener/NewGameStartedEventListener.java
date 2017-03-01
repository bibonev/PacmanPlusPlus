package teamproject.event.listener;

import teamproject.event.arguments.NewGameStartedEventArgs;

public interface NewGameStartedEventListener {
	void onNewGameStarted(NewGameStartedEventArgs args);
}
