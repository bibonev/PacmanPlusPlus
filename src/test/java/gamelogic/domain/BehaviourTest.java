package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import main.java.ai.AIGhost;
import main.java.ai.AIPlayer;
import main.java.constants.CellState;
import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.domain.stubs.BehaviourStub;
import test.java.gamelogic.random.Randoms;

public class BehaviourTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Behaviour.Type type = Randoms.randomEnum(Behaviour.Type.class);
		final World world = new World(new RuleChecker(),Map.generateMap(),false);
		final AIPlayer ghostEntity = new AIPlayer();
		ghostEntity.setPosition(new Position(0,0));
		final SkillSet stash = Randoms.randomLocalSkillSet();

		// When
		final Behaviour behaviour = new BehaviourStub(world, ghostEntity, stash, type);

		// Then
		assertThat(behaviour.getType(), Is.is(type));
		assertThat(world.getMap().getCell(behaviour.pickTarget()),IsNot.not(CellState.OBSTACLE));
		Position oldPos = behaviour.entity.getPosition();
		behaviour.run();
		Position newPos = behaviour.entity.getPosition();
		assertThat(oldPos, IsNot.not(newPos));
	}

}
