package teamproject.gamelogic.domain;

import java.util.HashMap;

/**
 * Represent a score board
 *
 * @author aml
 *
 */
public class Scoreboard {
	private HashMap<Integer, Integer> scores;

	public Scoreboard() {
		scores = new HashMap<Integer, Integer>();
	}

	/**
	 * Fetch all the scores
	 *
	 * @return a map containing the scores and ids
	 */
	public HashMap<Integer, Integer> getScores() {
		return scores;
	}

	/**
	 * Fetch the score of a player
	 *
	 * @param id
	 *            the id of the player
	 * @return score as integer
	 */
	public int getScoreForPlayerId(final int id) {
		return scores.get(id);
	}

	/**
	 * Update the score of a player
	 *
	 * @param id
	 *            the id of the player
	 * @param score
	 *            new score as integer
	 */
	public void setScoreForPlayerId(final int id, final int score) {
		scores.put(id, score);
	}

}
