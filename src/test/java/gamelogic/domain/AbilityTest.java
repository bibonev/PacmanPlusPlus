package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.ai.AIPlayer;
import main.java.gamelogic.domain.Ability;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Player;
import test.java.gamelogic.domain.stubs.AbilityStub;
import test.java.gamelogic.random.Randoms;

public class AbilityTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();

		// When
		final Ability ability = new AbilityStub(name);

		ability.setOwner(new AIPlayer());
		// Then
		assertThat(ability.getName(), Is.is(name));
		assertTrue(ability.getCD()>=0 && ability.getCD()<=40);
		assertTrue(ability.getOwner() instanceof Player);
		
		
	}

}
