package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.Position;
import test.java.gamelogic.random.Randoms;

public class PositionTest {

	@Test
	public void shouldConstruct() {
		// Given
		final int x = Randoms.randomInteger();
		final int y = Randoms.randomInteger();

		// When
		final Position position = new Position(x, y);

		// Then
		assertThat(position.getRow(), Is.is(x));
		assertThat(position.getColumn(), Is.is(y));
	}

}
