package teamproject.event.listener;

import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.arguments.RemoteGameEndedEventArgs;

public interface GameEndedListener {
	public void onGameEnded(GameEndedEventArgs args);
}
