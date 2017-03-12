package teamproject.gamelogic.domain;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import main.java.gamelogic.domain.PacBomb;
import main.java.gamelogic.domain.PacShield;

public class SkillsTest {

	@Test
	public void shouldConstruct() {


		// When
		final PacBomb pacBomb = new PacBomb();
		final PacShield pacShield = new PacShield();

		pacShield.activate();
		//pacBomb.activate();

		// Pacbomb test will be implemented later when introduced cooldown


		// Then
		assertTrue(pacShield.getOwner().getShield());
	}

}
