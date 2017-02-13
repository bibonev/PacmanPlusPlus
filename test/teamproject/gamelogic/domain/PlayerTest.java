package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class PlayerTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Optional<Long> id = Optional.of(Randoms.randomLong());
		final String name = Randoms.randomString();

		// When
		final Player player = new Player(id, name);
		// Then
		assertThat(player.getId(), Is.is(id));
		assertThat(player.getName(), Is.is(name));
	}

}
