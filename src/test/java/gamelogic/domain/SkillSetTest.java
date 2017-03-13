package test.java.gamelogic.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.gamelogic.domain.SkillSet;

public class SkillSetTest {

	@Test
	public void shouldConstruct() {
		// When
		final SkillSet skillSet = new SkillSet();

		// Then
		assertNotNull(skillSet);
	}

}
