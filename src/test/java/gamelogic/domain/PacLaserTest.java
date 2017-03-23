package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.PacLaser;

public class PacLaserTest {

	@Test
	public void shouldConstruct() {
		// When
		final PacLaser pacLaser = new PacLaser();

		// Then
		assertThat(pacLaser.getName(), Is.is("PacLaser"));
	}

}
