package teamproject.gamelogic.core;

import java.util.Timer;
import java.util.TimerTask;

public class GameLogicTimer {
	private GameLogic gameLogic;
	private Timer timer;
	
	public GameLogicTimer(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.timer = new Timer("game-logic-timer");
	}
	
	public void start(int interval) {
		timer.scheduleAtFixedRate(new GameLogicTimerTask(interval), 0, interval);
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public class GameLogicTimerTask extends TimerTask {
		private int delay;
		
		public GameLogicTimerTask(int delay) {
			this.delay = delay;
		}
		
		@Override
		public void run() {
			gameLogic.gameStep(delay);
		}
	}
}
