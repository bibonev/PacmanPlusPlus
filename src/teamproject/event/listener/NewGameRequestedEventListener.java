package teamproject.event.listener;

import teamproject.event.arguments.container.NewGameRequestedEventArguments;

public interface NewGameRequestedEventListener {
	void onNewGameRequested(NewGameRequestedEventArguments args);
}
