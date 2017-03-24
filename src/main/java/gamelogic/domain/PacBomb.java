/**
 *
 */
package main.java.gamelogic.domain;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * A bomb that kills all players in a given range.
 *
 * @author Lyubomir Pashev
 *
 */
public class PacBomb extends Ability {

	final int range = 4;

	public PacBomb() {
		super("PacBomb");
	}

	/**
	 * Detonates the bomb, killing all player within a given radius. The bomb is
	 * placed in the world, after 2 seconds, it explodes and is removed again.
	 */
	@Override
	public void activate() {
		final World world = owner.getWorld();

		final int len = world.getMap().getMapSize();

		final int row = owner.getPosition().getRow();
		final int col = owner.getPosition().getColumn();

		int rowend = row + range;
		int rowstart = row - range;

		int colend = col + range;
		int colstart = col - range;

		if (rowstart > len) {
			rowstart = len - 1;
		}
		if (colstart > len) {
			colstart = len - 1;
		}
		if (rowend < 0) {
			rowend = 0;
		}
		if (colend < 0) {
			colend = 0;
		}


		for (final Entity entity : world.getEntities()) {
			final int enrow = entity.getPosition().getRow();
			final int encol = entity.getPosition().getColumn();
			if (enrow >= rowstart && enrow <= rowend && encol >= colstart && encol <= colend) {
				world.removeEntity(entity.getID());
			}
		}
	}

	@Override
	public boolean incrementCooldown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reduceShieldValue() {
		// no
	}
}
