package teamproject.event.listener;

import teamproject.event.arguments.LobbyChangedEventArgs;

public interface LobbyStateChangedListener {
	public void onLobbyStateChanged(LobbyChangedEventArgs args);
}
