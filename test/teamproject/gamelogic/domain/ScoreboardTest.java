package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class ScoreboardTest {

	@Test
	public void shouldConstruct() {
		// When
		final Scoreboard scoreboard = new Scoreboard();

		// Then
		assertThat(scoreboard.getScores(), Is.is(new HashMap<Long, Integer>()));
	}

	@Test
	public void shouldSetAndGetScoreForPlayerId() {
		// Given
		final Scoreboard scoreboard = new Scoreboard();
		final int id1 = Randoms.randomInteger();
		final int score1 = Randoms.randomInteger();
		final int id2 = Randoms.randomInteger();
		final int score2 = Randoms.randomInteger();

		// When
		scoreboard.setScoreForPlayerId(id1, score1);
		scoreboard.setScoreForPlayerId(id2, score2);

		// Then
		assertThat(scoreboard.getScoreForPlayerId(id1), Is.is(score1));
		assertThat(scoreboard.getScoreForPlayerId(id2), Is.is(score2));
	}

}
