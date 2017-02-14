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
	 * @param currentPos the current position
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public DefaultBehaviour(Map map, Position currentPos, int speed, Inventory stash, Type type) {
		super(map, currentPos, speed, stash, type);
	}
}
