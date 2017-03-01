package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.constants.GameType;
import teamproject.gamelogic.random.Randoms;

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
