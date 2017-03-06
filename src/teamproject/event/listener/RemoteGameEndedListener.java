package teamproject.event.listener;

import teamproject.event.arguments.RemoteGameEndedEventArgs;

public interface RemoteGameEndedListener {
	public void onRemoteGameEnded(RemoteGameEndedEventArgs args);
}
