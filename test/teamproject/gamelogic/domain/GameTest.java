package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class GameTest {

	@Test
	public void shouldConstruct() {
		// Given
		final long id = Randoms.randomLong();
		final World world = Randoms.randomWorld();
		final GameSettings gameSettings = Randoms.randomGameSettings();

		// When
		final Game game = new Game(id, world, gameSettings);

		// Then
		assertThat(game.getId(), Is.is(id));
		assertThat(game.getWorld(), Is.is(world));
		assertThat(game.getGameSettings(), Is.is(gameSettings));
	}

}
