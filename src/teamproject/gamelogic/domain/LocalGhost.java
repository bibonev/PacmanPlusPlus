package teamproject.gamelogic.domain;

/**
 * Represent a local ghost (used for singleplayer games)
 * 
 * @author aml
 *
 */
public class LocalGhost extends Ghost {

	public LocalGhost() {
		super();
	}

	@Override
	public boolean setPosition(Position newPosition) {
		World world = getWorld();
		if(world != null) {
			if(canSetPosition(newPosition)) {
		        super.setPosition(newPosition);
	
		        return true;
			} else {
				return false;
			}
		} else {
			return super.setPosition(newPosition);
		}
	}
}
