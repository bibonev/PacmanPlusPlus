package main.java.event.listener;

import main.java.event.arguments.GameSettingsChangedEventArgs;

public interface GameSettingsChangedEventListener {
	void onGameSettingsChanged(GameSettingsChangedEventArgs args);
}
