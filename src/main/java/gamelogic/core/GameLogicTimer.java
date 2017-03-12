package main.java.gamelogic.core;

import java.util.Timer;
import java.util.TimerTask;

public class GameLogicTimer {
	private GameLogic gameLogic;
	private Timer timer;

	public GameLogicTimer(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		timer = new Timer("game-logic-timer");
	}

	public void start(final int interval) {
		timer.scheduleAtFixedRate(new GameLogicTimerTask(interval), 0, interval);
	}

	public void stop() {
		timer.cancel();
	}

	public class GameLogicTimerTask extends TimerTask {
		private int delay;

		public GameLogicTimerTask(final int delay) {
			this.delay = delay;
		}

		@Override
		public void run() {
			gameLogic.gameStep(delay);
		}
	}
}
