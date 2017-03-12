package main.java.event.listener;

import main.java.event.arguments.GameStateChangedEventArgs;

public interface GameStateChangedEventListener {
	void onGameStateChanged(GameStateChangedEventArgs args);
}
