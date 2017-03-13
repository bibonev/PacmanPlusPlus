package main.java.event.listener;

import main.java.event.arguments.RemoteGameEndedEventArgs;

public interface RemoteGameEndedListener {
	public void onRemoteGameEnded(RemoteGameEndedEventArgs args);
}
