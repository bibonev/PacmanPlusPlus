package main.java.event.listener;

import main.java.event.arguments.NewMultiplayerGameRequestedEventArgs;

public interface NewMultiplayerGameRequestedEventListener {
	void onNewMultiplayerGameRequested(NewMultiplayerGameRequestedEventArgs args);
}
