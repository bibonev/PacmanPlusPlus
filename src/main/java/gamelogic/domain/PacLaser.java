package main.java.gamelogic.domain;

import java.util.Collection;

/**
 * The PacLaser skill. Shoots 4 lasers out of the pacman, one in each direction, 
 * killing anyone caught in their path.
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public class PacLaser extends Ability {
	public static final int LASER_COOLDOWN = 20;

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
		if(getCD() == 20){
		    shoot();
		    setCD(0);
        }

	}

	private void shoot(){
        final World world = owner.getWorld();

        final Collection<Entity> entities = world.getEntities();

        final int row = owner.getPosition().getRow();
        final int col = owner.getPosition().getColumn();
        final double angle = owner.getAngle();

        if(angle == 0.0){

            for(Entity entity:entities){
                final int enrow = entity.getPosition().getRow();
                final int encol = entity.getPosition().getColumn();
                if(enrow==row && encol>col){
                    entity.setIsKilled(true);
                }
            }
            return;

        }
        if(angle == 90.0){

            for(Entity entity:entities){
                final int enrow = entity.getPosition().getRow();
                final int encol = entity.getPosition().getColumn();
                if(enrow>row && encol==col){
                    entity.setIsKilled(true);
                }
            }
            return;
        }
        if(angle == -90.0){

            for(Entity entity:entities){
                final int enrow = entity.getPosition().getRow();
                final int encol = entity.getPosition().getColumn();
                if(enrow<row && encol==col){
                    entity.setIsKilled(true);
                }
            }
            return;
        }
        if(angle == 180.0){
            for(Entity entity:entities){
                final int enrow = entity.getPosition().getRow();
                final int encol = entity.getPosition().getColumn();
                if(enrow==row && encol<col){
                    entity.setIsKilled(true);
                }
            }
            return;
        }
    }

	@Override
	public void incrementCooldown() {
		int cooldown = getCD();
		
		if(cooldown < LASER_COOLDOWN) {
			setCD(cooldown + 1);
		}
	}
}
