 package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.constants.GameType;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.random.Randoms;

public class GameTest {

	@Test
	public void shouldConstruct() {
		// Given
		final World world = Randoms.randomWorld();
		final GameSettings gameSettings = Randoms.randomGameSettings();

		// When
		final Game game = new Game(world, gameSettings, GameType.SINGLEPLAYER);
		game.setEnded();
		game.setStarted();

		// Then
		assertThat(game.getWorld(), Is.is(world));
		assertThat(game.getGameSettings(), Is.is(gameSettings));
		assertTrue(game.hasEnded()==true);
		assertTrue(game.hasStarted()==true);
		
	}

}
