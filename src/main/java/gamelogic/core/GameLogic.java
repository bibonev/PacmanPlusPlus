package main.java.gamelogic.core;

import main.java.event.Event;
import main.java.event.arguments.GameDisplayInvalidatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.listener.GameDisplayInvalidatedListener;
import main.java.event.listener.GameEndedListener;

public interface GameLogic {
	public void gameStep(int delay);

	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated();

	public Event<GameEndedListener, GameEndedEventArgs> getOnGameEnded();
}
