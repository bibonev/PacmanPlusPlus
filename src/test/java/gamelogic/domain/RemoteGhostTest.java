package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class RemoteGhostTest {

	@Test
	public void shouldConstruct() {
		// Given
		final int id = Randoms.randomInteger();

		// When
		final RemoteGhost ghost = new RemoteGhost(id);

		// Then
		assertThat(ghost.getID(), Is.is(id));
	}

}
