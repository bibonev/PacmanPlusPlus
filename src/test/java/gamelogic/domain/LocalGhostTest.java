package teamproject.gamelogic.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LocalGhostTest {

	@Test
	public void shouldConstruct() {
		// When
		final LocalGhost ghost = new LocalGhost();

		// Then
		assertNotNull(ghost);
	}

}
