package teamproject.gamelogic.domain;

import java.util.HashMap;

public class Scoreboard {
	private HashMap<Integer, Integer> scores;

	public Scoreboard() {
		scores = new HashMap<Integer, Integer>();
	}

	public HashMap<Integer, Integer> getScores() {
		return scores;
	}

	public int getScoreForPlayerId(final int id) {
		return scores.get(id);
	}

	public void setScoreForPlayerId(final int id, final int score) {
		scores.put(id, score);
	}

}
