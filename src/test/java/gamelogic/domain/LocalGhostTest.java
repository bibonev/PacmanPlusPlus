package test.java.gamelogic.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.gamelogic.domain.LocalGhost;

public class LocalGhostTest {

	@Test
	public void shouldConstruct() {
		// When
		final LocalGhost ghost = new LocalGhost();

		// Then
		assertNotNull(ghost);
	}

}
