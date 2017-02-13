package teamproject.event.listener;

import teamproject.event.arguments.container.NewGameStartedEventArguments;

public interface NewGameStartedEventListener {
	public void onNewGameStarted(NewGameStartedEventArguments args);
}
