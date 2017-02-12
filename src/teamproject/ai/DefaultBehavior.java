package teamproject.ai;

import teamproject.gamelogic.domain.*;

/**
 * The Class DefaultBehavior.
 *@author Lyubomir Pashev
 */
public class DefaultBehavior extends Behavior {

	/**
	 * Instantiates a new default behavior.
	 *
	 * @param map the map
	 * @param currentPos the current position
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public DefaultBehavior(Map map, Position currentPos, int speed, Inventory stash) {
		super(map, currentPos, speed, stash);
	}
}
