package test.java.gamelogic.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import main.java.constants.CellState;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import test.java.gamelogic.domain.stubs.CellStub;

public class RuleCheckerTest {

	@Test
	public void shouldCheckCellValidity_validCell() {
		// Given
		final Cell cell = new CellStub(CellState.EMPTY, new Position(1, 1));

		// Then
		assertTrue(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_type() {
		// Given
		final Cell cell = new CellStub(CellState.OBSTACLE, new Position(1, 1));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_state() {
		// Given
		final Cell cell = new CellStub(CellState.OBSTACLE, new Position(1, 1));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posRow() {
		// Given
		final Cell cell = new CellStub(CellState.EMPTY, new Position(-2, 2));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posColumn() {
		// Given
		final Cell cell = new CellStub(CellState.EMPTY, new Position(2, -2));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}
}
