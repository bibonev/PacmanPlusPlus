package main.java.event.listener;

import main.java.event.arguments.GameStartedEventArgs;

public interface GameStartedListener {
	public void onGameStarted(GameStartedEventArgs args);
}
