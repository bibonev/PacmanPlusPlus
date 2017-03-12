package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.World;

//TODO: to be implemented
public class AggressiveBehaviour extends Behaviour {

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
	public AggressiveBehaviour(final World world, final Entity entity, final int speed, final SkillSet stash,
			final Type type) {
		super(world, entity, speed, stash, type);
	}

	@Override
	public Position pickTarget() {
		return null;
	}

}
