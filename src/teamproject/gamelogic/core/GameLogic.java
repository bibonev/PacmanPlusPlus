package teamproject.gamelogic.core;

import teamproject.event.Event;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.arguments.RemoteGameEndedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.event.listener.GameEndedListener;

public interface GameLogic {
	public void gameStep(int delay);
	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated();
	public Event<GameEndedListener, GameEndedEventArgs> getOnGameEnded();
}
