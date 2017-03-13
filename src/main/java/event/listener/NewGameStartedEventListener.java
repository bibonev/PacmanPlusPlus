package main.java.event.listener;

import main.java.event.arguments.NewGameStartedEventArgs;

public interface NewGameStartedEventListener {
	void onNewGameStarted(NewGameStartedEventArgs args);
}
