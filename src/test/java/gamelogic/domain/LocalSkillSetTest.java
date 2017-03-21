package test.java.gamelogic.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.SkillSet;

public class LocalSkillSetTest {

	@Test
	public void shouldConstruct() {
		// When
		final SkillSet skillSet = new LocalSkillSet();

		// Then
		assertNotNull(skillSet);
	}

}
