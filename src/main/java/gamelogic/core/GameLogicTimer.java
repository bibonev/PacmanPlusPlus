package main.java.gamelogic.core;

import java.util.Timer;
import java.util.TimerTask;

import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.listener.GameEndedListener;

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

	public class GameLogicTimerTask extends TimerTask implements GameEndedListener {
		private int delay;

		public GameLogicTimerTask(final int delay) {
			this.delay = delay;
			gameLogic.getOnGameEnded().addListener(this);
		}

		@Override
		public void run() {
			gameLogic.gameStep(delay);
		}

		@Override
		public void onGameEnded(GameEndedEventArgs args) {
			this.cancel();
		}
	}
}
