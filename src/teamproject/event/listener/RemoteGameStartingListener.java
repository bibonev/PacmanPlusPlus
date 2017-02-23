package teamproject.event.listener;

import teamproject.event.arguments.MultiplayerGameStartingEventArgs;

public interface RemoteGameStartingListener {
	public void onRemoteGameStarting(MultiplayerGameStartingEventArgs args);
}
