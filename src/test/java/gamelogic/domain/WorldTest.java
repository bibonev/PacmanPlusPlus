package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.ai.AIPlayer;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.random.Randoms;

public class WorldTest {

	@Test
	public void shouldConstruct() {
		// Given
		final RuleChecker ruleEnforcer = Randoms.randomRuleEnforcer();
		final Map map = Randoms.randomMap();
		AIPlayer player = new AIPlayer();
		((Entity) player).setPosition(new Position(0,0));
		player.setID(-1);
		// When
		final World world = new World(ruleEnforcer, map, true);
		player.setWorld(world);
		world.addEntity(player);
		// Then
		assertThat(world.getRuleEnforcer(), Is.is(ruleEnforcer));
		assertThat(world.getMap(), Is.is(map));
		assertThat(world.getEntity(-1),Is.is(player));
		assertTrue(world.getEntitiesAt(new Position(0,0)).size()==1);
	}

}
