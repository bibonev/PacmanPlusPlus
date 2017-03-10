package teamproject.event.listener;

import teamproject.event.arguments.GameStartedEventArgs;
import teamproject.gamelogic.domain.Game;

public interface GameStartedListener {
	public void onGameStarted(GameStartedEventArgs args);
}
