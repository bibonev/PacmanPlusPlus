package teamproject.event.listener;

import teamproject.gamelogic.domain.Game;

public interface GameStartedListener {
	public void onGameStarted(Game game);
}
