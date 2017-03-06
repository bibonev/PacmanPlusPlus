package teamproject.event.listener;

import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;

public interface GameDisplayInvalidatedListener {
	public void onGameDisplayInvalidated(GameDisplayInvalidatedEventArgs args);
}
