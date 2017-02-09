package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class PositionTest {

	@Test
	public void shouldConstruct() {
		// Given
		final int x = Randoms.randomInteger();
		final int y = Randoms.randomInteger();

		// When
		final Position position = new Position(x, y);

		// Then
		assertThat(position.getX(), Is.is(x));
		assertThat(position.getY(), Is.is(y));
	}

}
