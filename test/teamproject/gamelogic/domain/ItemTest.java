package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.abilities.Ability;
import teamproject.gamelogic.random.Randoms;

public class ItemTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();
		final String description = Randoms.randomString();

		// When
		final Ability item = new Ability(name, description);

		// Then
		assertThat(item.getName(), Is.is(name));
		assertThat(item.getDescription(), Is.is(description));
	}

}
