package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.World;

/**
 * The Class DefaultBehavior.
 *
 * @author Lyubomir Pashev
 */
public class DefaultBehaviour extends Behaviour {
	
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
	public DefaultBehaviour(final World world, final Entity entity, final Type type) {
		super(world, entity, new LocalSkillSet(), type);
	}
}
