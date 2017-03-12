package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.constants.CellState;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Position;
import test.java.gamelogic.domain.stubs.CellStub;
import test.java.gamelogic.random.Randoms;

public class CellVisualisationTest {

	@Test
	public void shouldConstruct() {
		// Given
		final CellState state = Randoms.randomCellState();
		final Position position = Randoms.randomPosition();

		// When
		final Cell cell = new CellStub(state, position);

		// Then
		assertThat(cell.getState(), Is.is(state));
		assertThat(cell.getPosition(), Is.is(position));
	}

}
