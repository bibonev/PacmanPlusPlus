package teamproject.event.listener;

import teamproject.event.arguments.NewGameRequestedEventArgs;

public interface NewGameRequestedEventListener {
	void onNewGameRequested(NewGameRequestedEventArgs args);
}
