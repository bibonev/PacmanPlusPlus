package teamproject.ai;

import teamproject.gamelogic.domain.*;
import teamproject.gamelogic.domain.Behaviour.Type;
import teamproject.constants.*;

//TODO: to be implemented
public class AggressiveBehaviour extends Behaviour {

	/**
	 * Instantiates a new default behavior.
	 *
	 * @param map the map
	 * @param entity the controlled entity
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public AggressiveBehaviour(World world, Entity entity, int speed, Inventory stash, Type type) {
		super(world, entity, speed, stash, type);
	}

	@Override
	public Position pickTarget() {
		return null;
	}

}
