package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.Ability;
import test.java.gamelogic.domain.stubs.AbilityStub;
import test.java.gamelogic.random.Randoms;

public class AbilityTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();

		// When
		final Ability ability = new AbilityStub(name);

		// Then
		assertThat(ability.getName(), Is.is(name));
	}

}
