package teamproject.event.listener;

import teamproject.event.arguments.container.NewMultiplayerGameRequestedEventArguments;

public interface NewMultiplayerGameRequestedEventListener {
	void onNewMultiplayerGameRequested(NewMultiplayerGameRequestedEventArguments args);
}
