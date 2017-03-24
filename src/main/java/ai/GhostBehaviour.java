package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.World;

/**
 * The Ghost behaviour. It just does random moves.
 *
 * @author Lyubomir Pashev
 */
public class GhostBehaviour extends Behaviour {

	/**
	 * Instantiates a new default behavior.
	 *
	 * @param world the world
	 * @param entity            the controlled entity
	 * @param type the type
	 */
	public GhostBehaviour(final World world, final Entity entity, final Type type) {
		super(world, entity, new LocalSkillSet(), type);
	}

	/**
	 * Pick target.
	 *
	 * @return the position
	 * @see java.gamelogic.domain.Behaviour#pickTarget()
	 */
	@Override
	public Position pickTarget() {
		return pickRandomTarget();
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.Behaviour#run()
	 */
	@Override
	public void run() {

		lockedTarget = pickTarget();

		lastPos = entity.getPosition();
		entity.setPosition(lockedTarget);
	}
}