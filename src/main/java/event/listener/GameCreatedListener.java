package main.java.event.listener;

import main.java.event.arguments.GameCreatedEventArgs;

public interface GameCreatedListener {
	public void onGameCreated(GameCreatedEventArgs args);
}
