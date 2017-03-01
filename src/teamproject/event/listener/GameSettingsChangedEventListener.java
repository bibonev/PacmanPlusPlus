package teamproject.event.listener;

import teamproject.event.arguments.GameSettingsChangedEventArguments;

public interface GameSettingsChangedEventListener {
	void onGameSettingsChanged(GameSettingsChangedEventArguments args);
}
