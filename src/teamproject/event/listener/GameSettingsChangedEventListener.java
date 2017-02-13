package teamproject.event.listener;

import teamproject.event.arguments.container.GameSettingsChangedEventArguments;

public interface GameSettingsChangedEventListener {
	void onGameSettingsChanged(GameSettingsChangedEventArguments args);
}
