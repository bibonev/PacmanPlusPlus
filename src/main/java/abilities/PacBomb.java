/**
 * 
 */
package teamproject.abilities;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.World;

/**
 * A bomb that kills all players in a given range.
 * @author Lyubomir Pashev
 *
 */
public class PacBomb extends Ability {
	
	final int range = 4;

	public PacBomb() {
		super("PacBomb");
	}

	/**
	 * Detonates the bomb, killing all player within a given radius.
	 * The bomb is placed in the world, after 2 seconds, it explodes and is removed again.
	 */
	@Override
	public void activate() {
		World world = owner.getWorld();
		
		Collection<Entity> entities = world.getEntities();
		
		int len = world.getMap().getMapSize();
		
		int row = owner.getPosition().getRow();
		int col = owner.getPosition().getColumn();
		
		int rowend = row+range;
		int rowstart = row-range;
		
		int colend = col+range;
		int colstart = col-range;
		
		if(rowstart > len){
			rowstart = len-1;
		}
		if(colstart > len){
			colstart = len-1;
		}
		if(rowend < 0){
			rowend = 0;
		}
		if(colend < 0){
			colend = 0;
		}
		
		//2 second detonation delay
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			System.out.println("This was the PacBomb activation delay. Something interrupted the sleep");
		}
		
		for(Entity entity:entities){
			int enrow = entity.getPosition().getRow();
			int encol = entity.getPosition().getColumn();
			if(enrow>=rowstart && enrow<=rowend && encol>=colstart && encol<=colend){
				world.removeEntity(entity.getID());
			}	
		}
	}
}
