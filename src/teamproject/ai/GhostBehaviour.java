package teamproject.ai;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;


/**
 * The Ghost behaviour. It just does random moves.
 * @author Lyubomir Pashev
 */
public class GhostBehaviour extends Behaviour {

	/**
	 * Instantiates a new ghost behaviour.
	 *
	 * @param map the map
	 * @param startPos the start position
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public GhostBehaviour(Map map, Position startPos, int speed, Inventory stash, Type type) {
		super(map, startPos, speed, stash, type);
	}
	
	/** 
	 * @see teamproject.gamelogic.domain.Behaviour#pickTarget()
	 */
	@Override
	public Position pickTarget(){
		return pickRandomTarget();
	}

}
