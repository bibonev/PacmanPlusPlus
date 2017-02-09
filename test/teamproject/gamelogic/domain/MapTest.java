package teamproject.gamelogic.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.exception.ViolatedAssumptionException;
import teamproject.gamelogic.random.Randoms;

public class MapTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Cell[][] cells = Randoms.randomCells(Randoms.randomInteger());

		// When
		final Map map = new Map(cells);

		// Then
		assertThat(map.getCells().length, Is.is(cells.length));

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				assertThat(map.getCells()[i][j], Is.is(cells[i][j]));
			}
		}
	}

	@Test
	public void shouldAddCell() throws ViolatedAssumptionException {
		// Given
		final Map map = Randoms.randomMap();
		final Position position = Randoms.randomPositionInRange(map.getCells().length);
		final Cell cell = Randoms.randomCell(position);

		// Free up cell position
		map.getCells()[position.getX()][position.getY()] = null;

		// When
		map.addCell(cell);

		// Then
		assertThat(map.getCells()[position.getX()][position.getY()], Is.is(cell));
	}

	@Test
	public void shouldNotAddCell_PositionNotFree() {
		// Given
		final Map map = Randoms.randomMap();
		Exception expected = null;

		// When
		try {
			map.addCell(Randoms.randomCell(Randoms.randomPositionInRange(map.getCells().length)));
		} catch (final ViolatedAssumptionException actual) {
			expected = actual;
		}

		// Then
		assertNotNull(expected);
		assertThat(expected.getMessage(), Is.is("Assumption was violated: Position is not empty so cannot add cell."));
	}

}
