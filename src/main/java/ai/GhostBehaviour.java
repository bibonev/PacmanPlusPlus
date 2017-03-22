package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.SkillSet;
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
	 * @param map
	 *            the map
	 * @param entity
	 *            the controlled entity
	 * @param speed
	 *            the speed
	 * @param stash
	 *            the inventory
	 */
	public GhostBehaviour(final World world, final Entity entity, final Type type) {
		super(world, entity, new LocalSkillSet(), type);
	}

	/**
	 * @see java.gamelogic.domain.Behaviour#pickTarget()
	 */
	@Override
	public Position pickTarget() {
		return pickRandomTarget();
	}

	@Override
	public void run() {

		lockedTarget = pickTarget();

		lastPos = entity.getPosition();
		entity.setPosition(lockedTarget);
	}
}