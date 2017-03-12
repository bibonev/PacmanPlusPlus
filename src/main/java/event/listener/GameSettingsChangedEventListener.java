package teamproject.event.listener;

import teamproject.event.arguments.GameSettingsChangedEventArgs;

public interface GameSettingsChangedEventListener {
	void onGameSettingsChanged(GameSettingsChangedEventArgs args);
}
