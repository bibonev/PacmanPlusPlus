package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

/**
 * Represent a local player (used for singleplayer games)
 * 
 * @author aml
 *
 */
public class LocalPlayer extends Player {
	public LocalPlayer(final String name) {
		super(name);
	}

	@Override
	public boolean setPosition(Position newPosition) {
		World world = getWorld();
		if(world != null) {
			if(canSetPosition(newPosition)) {
				setAngle(getMoveAngle(getPosition(), newPosition, getAngle()));
				super.setPosition(newPosition);
		
		        return true;
			} else {
				return false;
			}
		} else {
			return super.setPosition(newPosition);
		}
	}
	
	private double getMoveAngle(Position oldPosition, Position newPosition, double oldAngle) {
		if(oldPosition.equals(newPosition)) {
			return oldAngle;
		} else {
			return Math.atan2(newPosition.getRow() - oldPosition.getRow(),
					newPosition.getColumn() - oldPosition.getColumn()) * 180.0 / Math.PI;
		}
	}
}
