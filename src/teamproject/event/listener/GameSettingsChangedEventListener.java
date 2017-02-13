package teamproject.event.listener;

import teamproject.event.arguments.container.GameSettingsChangedEventArguments;

public interface GameSettingsChangedEventListener {
	public void onGameSettingsChanged(GameSettingsChangedEventArguments args);
}
