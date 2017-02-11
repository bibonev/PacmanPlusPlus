package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.domain.stubs.PositionStub;
import teamproject.gamelogic.random.Randoms;

public class PositionVisualisationTest {

	@Test
	public void shouldConstruct() {
		// Given
		final int x = Randoms.randomInteger();
		final int y = Randoms.randomInteger();

		// When
		final Position position = new PositionStub(x, y);

		// Then
		assertThat(position.getRow(), Is.is(x));
		assertThat(position.getColumn(), Is.is(y));
	}

}