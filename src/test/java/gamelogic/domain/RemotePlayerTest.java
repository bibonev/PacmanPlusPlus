package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.RemotePlayer;
import test.java.gamelogic.random.Randoms;

public class RemotePlayerTest {

	@Test
	public void shouldConstruct() {
		// Given
		final int id = Randoms.randomInteger();
		final String name = Randoms.randomString();

		// When
		final RemotePlayer player = new RemotePlayer(id, name);

		// Then
		assertThat(player.getID(), Is.is(id));
		assertThat(player.getName(), Is.is(name));
	}

}
