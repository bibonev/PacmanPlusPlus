package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class WorldTest {

	@Test
	public void shouldConstruct() {
		// Given
		final RuleChecker ruleEnforcer = Randoms.randomRuleEnforcer();
		final Map map = Randoms.randomMap();

		// When
		final World world = new World(ruleEnforcer, map);

		// Then
		assertThat(world.getRuleEnforcer(), Is.is(ruleEnforcer));
		assertThat(world.getMap(), Is.is(map));
	}

}
