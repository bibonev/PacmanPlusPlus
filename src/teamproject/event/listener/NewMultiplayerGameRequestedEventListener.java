package teamproject.event.listener;

import teamproject.event.arguments.NewMultiplayerGameRequestedEventArgs;

public interface NewMultiplayerGameRequestedEventListener {
	void onNewMultiplayerGameRequested(NewMultiplayerGameRequestedEventArgs args);
}
