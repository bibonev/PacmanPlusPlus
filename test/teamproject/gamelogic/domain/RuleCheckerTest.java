package teamproject.gamelogic.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.constants.GameOutcome;
import teamproject.constants.GameType;
import teamproject.gamelogic.domain.stubs.CellStub;
import teamproject.gamelogic.random.Randoms;

public class RuleCheckerTest {

	@Test
	public void shouldCheckCellValidity_validCell() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(1, 1));

		// Then
		assertTrue(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_type() {
		// Given
		final Cell cell = new CellStub(CellType.WALL, CellState.EMPTY, new Position(1, 1));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_state() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.OBSTACLE, new Position(1, 1));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posRow() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(-2, 2));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldCheckCellValidity_invalidCell_posColumn() {
		// Given
		final Cell cell = new CellStub(CellType.NORMAL, CellState.EMPTY, new Position(2, -2));

		// Then
		assertFalse(RuleChecker.checkCellValidity(cell));
	}

	@Test
	public void shouldReportPlayerLost_PlayerEaten() {
		// Given
		final Game game = Randoms.randomGame();
		game.getWorld().getMap().getCell(0, 0).setState(CellState.PLAYER_AND_ENEMY);

		// Then
		assertThat(RuleChecker.getGameOutcome(game, GameType.SINGLEPLAYER), Is.is(GameOutcome.LOCAL_PLAYER_LOST));
	}

	@Test
	public void shouldReportPlayerWon_NoFoodLeft() {
		// Given
		final Game game = Randoms.randomGame();
		final Cell[][] cells = game.getWorld().getMap().getCells();

		// Eat ALL the food :)
		for (final Cell[] cellRow : cells) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cellRow[j].getState().equals(CellState.FOOD)) {
					cellRow[j].setState(CellState.EMPTY);
				}
			}
		}

		// Then
		assertThat(RuleChecker.getGameOutcome(game, GameType.SINGLEPLAYER), Is.is(GameOutcome.LOCAL_PLAYER_WON));
	}

	@Test
	public void shouldReportGameStillPlaying() {
		// Given
		final Game game = Randoms.randomGame();
		final Cell[][] cells = game.getWorld().getMap().getCells();

		// Make sure there's food left
		cells[0][0].setState(CellState.FOOD);

		// Make sure player's not eaten
		for (final Cell[] cellRow : cells) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cellRow[j].getState().equals(CellState.PLAYER_AND_ENEMY)) {
					cellRow[j].setState(CellState.ENEMY);
				}
			}
		}

		// Then
		assertThat(RuleChecker.getGameOutcome(game, GameType.SINGLEPLAYER), Is.is(GameOutcome.STILL_PLAYING));
	}

}
