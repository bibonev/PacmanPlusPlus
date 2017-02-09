package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.domain.stubs.CellStub;
import teamproject.gamelogic.random.Randoms;
import teamproject.constants.CellState;
import teamproject.constants.CellType;

public class CellVisualisationTest {

	@Test
	public void shouldConstruct() {
		// Given
		final CellType type = Randoms.randomCellType();
		final CellState state = Randoms.randomCellState();
		final Position position = Randoms.randomPosition();

		// When
		final Cell cell = new CellStub(type, state, position);

		// Then
		assertThat(cell.getType(), Is.is(type));
		assertThat(cell.getState(), Is.is(state));
		assertThat(cell.getPosition(), Is.is(position));
	}

}
