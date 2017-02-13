package teamproject.event.listener;

import teamproject.event.arguments.container.NewGameRequestedEventArguments;

public interface NewGameRequestedEventListener {
	public void onNewGameRequested(NewGameRequestedEventArguments args);
}
