package main.java.gamelogic.domain;

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
	public boolean setPosition(final Position newPosition) {
		final World world = getWorld();
		if (world != null) {
			if (canSetPosition(newPosition)) {
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

	private double getMoveAngle(final Position oldPosition, final Position newPosition, final double oldAngle) {
		if (oldPosition.equals(newPosition)) {
			return oldAngle;
		} else {
			return Math.atan2(newPosition.getRow() - oldPosition.getRow(),
					newPosition.getColumn() - oldPosition.getColumn()) * 180.0 / Math.PI;
		}
	}
}
