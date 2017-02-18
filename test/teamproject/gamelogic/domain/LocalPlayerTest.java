package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class LocalPlayerTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();

		// When
		final LocalPlayer player = new LocalPlayer(name);

		// Then
		assertThat(player.getName(), Is.is(name));
	}

}
