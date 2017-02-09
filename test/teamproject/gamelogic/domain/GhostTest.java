package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class GhostTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();
		final Behaviour behaviour = Randoms.randomBehaviour();

		// When
		final Ghost ghost = new Ghost(behaviour, name);

		// Then
		assertThat(ghost.getBehaviour(), Is.is(behaviour));
		assertThat(ghost.getName(), Is.is(name));
	}

}
