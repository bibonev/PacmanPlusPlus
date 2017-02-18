package teamproject.gamelogic.domain.stubs;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;

public class BehaviourStub extends Behaviour {

	public BehaviourStub(final Map map, final Entity entity, final int speed, final Inventory stash, final Type type) {
		super(map, entity, speed, stash, type);
	}

}
