package main.java.event.listener;

import main.java.event.arguments.GameEndedEventArgs;

public interface GameEndedListener {
	public void onGameEnded(GameEndedEventArgs args);
}
