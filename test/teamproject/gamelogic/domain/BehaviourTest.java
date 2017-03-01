package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.domain.stubs.BehaviourStub;
import teamproject.gamelogic.random.Randoms;

public class BehaviourTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Behaviour.Type type = Randoms.randomEnum(Behaviour.Type.class);
		final World world = Randoms.randomWorld();
		final Entity ghostEntity = Randoms.randomLocalGhost();
		final int speed = Randoms.randomInteger();
		final Inventory stash = Randoms.randomInventory();

		// When
		final Behaviour behaviour = new BehaviourStub(world, ghostEntity, speed, stash, type);

		// Then
		// TODO: Test everything else
		assertThat(behaviour.getType(), Is.is(type));
	}

}
