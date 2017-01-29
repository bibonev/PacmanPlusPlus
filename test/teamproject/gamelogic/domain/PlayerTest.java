package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class PlayerTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();

		// When
		final Player player = new Player(name);
		// Then
		assertThat(player.getName(), Is.is(name));
	}

}
