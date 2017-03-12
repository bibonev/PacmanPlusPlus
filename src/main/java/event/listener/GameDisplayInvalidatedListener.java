package main.java.event.listener;

import main.java.event.arguments.GameDisplayInvalidatedEventArgs;

public interface GameDisplayInvalidatedListener {
	public void onGameDisplayInvalidated(GameDisplayInvalidatedEventArgs args);
}
