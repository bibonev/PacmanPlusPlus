package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class WorldTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Collection<Player> players = new ArrayList<Player>();
		players.add(Randoms.randomPlayer());

		final Collection<Ghost> ghosts = new ArrayList<Ghost>();
		ghosts.add(Randoms.randomGhost());

		final RuleEnforcer ruleEnforcer = Randoms.randomRuleEnforcer();
		final Map map = Randoms.randomMap();

		// When
		final World world = new World(players, ruleEnforcer, ghosts, map);

		// Then
		assertThat(world.getPlayers(), Is.is(players));
		assertThat(world.getGhosts(), Is.is(ghosts));
		assertThat(world.getRuleEnforcer(), Is.is(ruleEnforcer));
		assertThat(world.getMap(), Is.is(map));
	}

}
