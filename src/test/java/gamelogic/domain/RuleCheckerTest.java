package test.java.gamelogic.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import main.java.gamelogic.domain.*;
import org.junit.Assert;
import org.junit.Test;

import main.java.constants.CellState;
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

	/*
	 * TODO should move all of these into a LocalGameLogicTest class
	 *
	 * @Test public void shouldReportPlayerLost_PlayerEaten() { // Given final
	 * Game game = Randoms.randomGame(); final ControlledPlayer player = new
	 * ControlledPlayer(1, "TestPlayer"); player.setPosition(new Position(6,
	 * 0));
	 *
	 * AIGhost ghost = new AIGhost(); ghost.setPosition(new Position(6, 0));
	 * game.getWorld().addEntity(ghost);
	 *
	 * // Then assertThat(RuleChecker.getGameOutcome(game),
	 * Is.is(GameOutcomeType.LOCAL_PLAYER_LOST)); }
	 *
	 * @Test public void shouldReportPlayerWon_NoFoodLeft() { // Given final
	 * Game game = Randoms.randomGame(); final Cell[][] cells =
	 * game.getWorld().getMap().getCells();
	 *
	 * // Eat ALL the food :) for (final Cell[] cellRow : cells) { for (int j =
	 * 0; j < cells[0].length; j++) { if
	 * (cellRow[j].getState().equals(CellState.FOOD)) {
	 * cellRow[j].setState(CellState.EMPTY); } } }
	 *
	 * // Then assertThat(RuleChecker.getGameOutcome(game),
	 * Is.is(GameOutcomeType.LOCAL_PLAYER_WON)); }
	 *
	 * @Test public void shouldReportGameStillPlaying() { // Given final Game
	 * game = Randoms.randomGame(); final Cell[][] cells =
	 * game.getWorld().getMap().getCells();
	 *
	 * final ControlledPlayer player = new ControlledPlayer(1, "TestPlayer");
	 * player.setPosition(new Position(6, 0));
	 *
	 * AIGhost ghost = new AIGhost(); ghost.setPosition(new Position(14, 14));
	 * game.getWorld().addEntity(ghost);
	 *
	 * // Make sure there's food left cells[0][0].setState(CellState.FOOD);
	 *
	 * // Make sure player's not eaten
	 *
	 * // Then assertThat(RuleChecker.getGameOutcome(game),
	 * Is.is(GameOutcomeType.STILL_PLAYING)); }
	 */

    public static class SkillsTest {

        @Test
        public void shouldConstruct() {


            // When
            final PacBomb pacBomb = new PacBomb();
            final PacShield pacShield = new PacShield();

            pacShield.activate();
            //pacBomb.activate();

            // Pacbomb test will be implemented later when introduced cooldown


            // Then
            Assert.assertTrue(pacShield.getOwner().getShield());
        }

    }
}
