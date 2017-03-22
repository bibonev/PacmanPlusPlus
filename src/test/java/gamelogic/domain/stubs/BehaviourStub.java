package test.java.gamelogic.domain.stubs;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.World;

public class BehaviourStub extends Behaviour {

	public BehaviourStub(final World world, final Entity entity, final SkillSet stash,
			final Type type) {
		super(world, entity, stash, type);
	}

}
