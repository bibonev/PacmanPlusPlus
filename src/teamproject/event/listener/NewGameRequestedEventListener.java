package teamproject.event.listener;

import teamproject.event.arguments.NewGameRequestedEventArguments;

public interface NewGameRequestedEventListener {
	void onNewGameRequested(NewGameRequestedEventArguments args);
}
