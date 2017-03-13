package main.java.gamelogic.domain;

import java.util.Collection;

/**
 * The PacLaser skill. Shoots 4 lasers out of the pacman, one in each direction, 
 * killing anyone caught in their path.
 * 
 * @author Lyubomir Pashev
 */
public class PacLaser extends Ability {

	/**
	 * Instantiates a new paclaser.
	 */
	public PacLaser() {
		super("PacLaser");
	}

	/**
	 * Shoots instant lasers.
	 */
	@Override
	public void activate() {
		final World world = owner.getWorld();

		final Collection<Entity> entities = world.getEntities();

		final int row = owner.getPosition().getRow();
		final int col = owner.getPosition().getColumn();
		
		//for now just removes entities from the world
		//will probably be replaced with a more elegant solution
		for(Entity entity:entities){
			final int enrow = entity.getPosition().getRow();
			final int encol = entity.getPosition().getColumn();
			if(enrow==row || encol==col){
				world.removeEntity(entity.getID());
			}
					
		}


	}

}
