package main.java.event.listener;

import main.java.event.arguments.LobbyChangedEventArgs;

public interface LobbyStateChangedListener {
	public void onLobbyStateChanged(LobbyChangedEventArgs args);
}
