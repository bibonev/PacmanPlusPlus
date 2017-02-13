package teamproject.event.listener;

import teamproject.event.arguments.container.NewGameStartedEventArguments;

public interface NewGameStartedEventListener {
	void onNewGameStarted(NewGameStartedEventArguments args);
}
