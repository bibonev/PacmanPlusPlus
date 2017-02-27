package teamproject.ai;

import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.gamelogic.domain.*;


/**
 * The Ghost behaviour. It just does random moves.
 * @author Lyubomir Pashev
 */
public class GhostBehaviour extends Behaviour {

    private EntityMovement ghostMovement;

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
		this.ghostMovement = new EntityMovement(entity, map);
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
		ghostMovement.moveTo(lockedTarget.getRow(), lockedTarget.getColumn(), 0);

		onEntityMoved.fire(new EntityMovedEventArgs(lockedTarget.getRow(), lockedTarget.getColumn(), 0, entity));
	}
}