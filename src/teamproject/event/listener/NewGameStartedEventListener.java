package teamproject.event.listener;

import teamproject.event.arguments.NewGameStartedEventArguments;

public interface NewGameStartedEventListener {
	void onNewGameStarted(NewGameStartedEventArguments args);
}
