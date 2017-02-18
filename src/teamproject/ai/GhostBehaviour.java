package teamproject.ai;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.Behaviour.Type;


/**
 * The Ghost behaviour. It just does random moves.
 * @author Lyubomir Pashev
 */
public class GhostBehaviour extends Behaviour {

	/**
	 * Instantiates a new default behavior.
	 *
	 * @param map the map
	 * @param entity the controlled entity
	 * @param speed the speed
	 * @param stash the inventory
	 */
	public GhostBehaviour(Map map, Entity entity, int speed, Inventory stash, Type type) {
		super(map, entity, speed, stash, type);
	}
	
	/** 
	 * @see teamproject.gamelogic.domain.Behaviour#pickTarget()
	 */
	@Override
	public Position pickTarget(){
		return pickRandomTarget();
	}

}
