package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

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
		final ControlledPlayer controlledPlayer = Randoms.randomControlledPlayer();

		// When
		final Game game = new Game(world, gameSettings, controlledPlayer, GameType.SINGLEPLAYER);

		// Then
		assertThat(game.getPlayer(), Is.is(controlledPlayer));
		assertThat(game.getWorld(), Is.is(world));
		assertThat(game.getGameSettings(), Is.is(gameSettings));
	}

}
