package teamproject.ai;

import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.World;


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
	public GhostBehaviour(World world, Entity entity, int speed, Inventory stash, Type type) {
		super(world, entity, speed, stash, type);
	}
	
	/** 
	 * @see teamproject.gamelogic.domain.Behaviour#pickTarget()
	 */
	@Override
	public Position pickTarget(){
		return pickRandomTarget();
	}
	@Override
	public void run() {

		lockedTarget = pickTarget();

		lastPos=entity.getPosition();
		entity.setPosition(lockedTarget);
	}
}