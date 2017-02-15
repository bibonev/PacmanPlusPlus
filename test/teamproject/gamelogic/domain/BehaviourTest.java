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

		final Map map = Randoms.randomMap();
		// When
		final Behaviour behaviour = Randoms.randomBehaviour();

		// Then
		assertThat(behaviour.getType(), Is.is(type));
	}

}
