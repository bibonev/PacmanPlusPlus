package teamproject.event.listener;

import teamproject.event.arguments.NewMultiplayerGameRequestedEventArguments;

public interface NewMultiplayerGameRequestedEventListener {
	void onNewMultiplayerGameRequested(NewMultiplayerGameRequestedEventArguments args);
}
