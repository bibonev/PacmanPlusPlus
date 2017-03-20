package test.java.gamelogic.domain;

import org.junit.Assert;
import org.junit.Test;

import main.java.gamelogic.domain.PacBomb;
import main.java.gamelogic.domain.PacShield;
import test.java.gamelogic.random.Randoms;

public class SkillsTest {

	@Test
	public void shouldConstructAndActivate() {

		// When
		final PacBomb pacBomb = new PacBomb();
		final PacShield pacShield = new PacShield();

		pacShield.setOwner(Randoms.randomControlledPlayer());
		pacShield.activate();
		// pacBomb.activate();

		// Pacbomb test will be implemented later when introduced cooldown

		// Then
		Assert.assertTrue(pacShield.getOwner().getShield());
	}

}
