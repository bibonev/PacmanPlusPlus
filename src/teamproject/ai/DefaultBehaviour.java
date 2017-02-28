package teamproject.ai;

import teamproject.gamelogic.domain.*;

/**
 * The Class DefaultBehavior.
 *@author Lyubomir Pashev
 */
public class DefaultBehaviour extends Behaviour {
	/**
	 * Instantiates a new default behavior.
	 *
	 * @param map the map
	 * @param entity the controlled entity
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public DefaultBehaviour(World world, Entity entity, int speed, Inventory stash, Type type) {
		super(world, entity, speed, stash, type);
	}
}
