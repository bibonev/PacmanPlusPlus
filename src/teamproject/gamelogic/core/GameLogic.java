package teamproject.gamelogic.core;

import teamproject.event.Event;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;

public interface GameLogic {
	public void gameStep(int delay);
	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated();
}
