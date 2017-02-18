package teamproject.gamelogic.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.stubs.CellStub;

public class RuleEnforcerTest {

	@Test
	public void shouldCheckCellValidity_validCell() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(1, 1));

		// Then
		assertTrue(RuleEnforcer.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_type() {
		// Given
		final Cell cell = new CellStub(CellType.WALL, CellState.EMPTY, new Position(1, 1));

		// Then
		assertFalse(RuleEnforcer.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_state() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.OBSTACLE, new Position(1, 1));

		// Then
		assertFalse(RuleEnforcer.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posRow() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(-2, 2));

		// Then
		assertFalse(RuleEnforcer.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posColumn() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(2, -2));

		// Then
		assertFalse(RuleEnforcer.checkCellValidity(cell));
	}

}
