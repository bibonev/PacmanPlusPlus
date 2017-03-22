package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.domain.stubs.BehaviourStub;
import test.java.gamelogic.random.Randoms;

public class BehaviourTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Behaviour.Type type = Randoms.randomEnum(Behaviour.Type.class);
		final World world = Randoms.randomWorld();
		final Entity ghostEntity = Randoms.randomLocalGhost();
		final int speed = Randoms.randomInteger();
		final SkillSet stash = Randoms.randomLocalSkillSet();

		// When
		final Behaviour behaviour = new BehaviourStub(world, ghostEntity, speed, stash, type);

		// Then
		// TODO: Test everything else
		assertThat(behaviour.getType(), Is.is(type));
	}

}
