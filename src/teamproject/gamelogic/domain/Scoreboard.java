package teamproject.gamelogic.domain;

import java.util.HashMap;

public class Scoreboard {
	private HashMap<Long, Integer> scores;

	public Scoreboard() {
		scores = new HashMap<Long, Integer>();
	}

	public HashMap<Long, Integer> getScores() {
		return scores;
	}

	public Integer getScoreForPlayerId(final long id) {
		return scores.get(id);
	}

	public void setScoreForPlayerId(final long id, final int score) {
		scores.put(id, score);
	}

}
