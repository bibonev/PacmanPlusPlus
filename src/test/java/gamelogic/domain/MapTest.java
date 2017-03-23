package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import test.java.gamelogic.domain.stubs.MapStub;
import test.java.gamelogic.random.Randoms;

public class MapTest {

	// Also tests getCell() and getMapSize()
	@Test
	public void shouldConstruct() {
		// Given
		final Cell[][] cells = Randoms.randomCells(Randoms.randomInteger());

		// When
		final Map map = new MapStub(cells);

		// Then
		assertThat(map.getMapSize(), Is.is(cells.length));

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				assertThat(map.getCells()[i][j], Is.is(cells[i][j]));
			}
		}
	}

	// Also tests getCell(x, y)
	@Test
	public void shouldAddCell() {
		// Given
		final Map map = Randoms.randomMap();
		final Position position = Randoms.randomPositionInRange(map.getCells().length);
		final Cell cell = Randoms.randomCell(position);

		// When
		map.addCell(cell);

		// Then
		assertThat(map.getCell(position.getRow(), position.getColumn()), Is.is(cell));
		assertTrue(map.getMapSize()==map.getCells().length);
	}

}
