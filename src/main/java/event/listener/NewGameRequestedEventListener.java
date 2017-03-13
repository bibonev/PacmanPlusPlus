package main.java.event.listener;

import main.java.event.arguments.NewGameRequestedEventArgs;

public interface NewGameRequestedEventListener {
	void onNewGameRequested(NewGameRequestedEventArgs args);
}
