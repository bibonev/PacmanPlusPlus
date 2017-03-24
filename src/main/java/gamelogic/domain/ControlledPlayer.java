package main.java.gamelogic.domain;

import main.java.constants.MovementDirection;

/**
 * Represent a controlled player
 *
 * @author aml
 *
 */
public class ControlledPlayer extends LocalPlayer {
	private MovementDirection direction;
	
	private boolean canRespawn;

	public ControlledPlayer(final int id, final String name) {
		super(name);
		setID(id);
		direction = MovementDirection.RIGHT;
	}

	public void moveUp() {
		final Position newPosition = getPosition().add(-1, 0);
		if (getWorld().isOccupiable(newPosition)) {
			direction = MovementDirection.UP;
		}
	}

	public void moveDown() {
		final Position newPosition = getPosition().add(1, 0);
		if (getWorld().isOccupiable(newPosition)) {
			direction = MovementDirection.DOWN;
		}
	}

	public void moveLeft() {
		final Position newPosition = getPosition().add(0, -1);
		if (getWorld().isOccupiable(newPosition)) {
			direction = MovementDirection.LEFT;
		}
	}

	public void moveRight() {
		final Position newPosition = getPosition().add(0, 1);
		if (getWorld().isOccupiable(newPosition)) {
			direction = MovementDirection.RIGHT;
		}
	}

	private void move() {
		Position newPosition;
		switch (direction) {
		case UP:
			newPosition = getPosition().add(-1, 0);
			break;
		case DOWN:
			newPosition = getPosition().add(1, 0);
			break;
		case LEFT:
			newPosition = getPosition().add(0, -1);
			break;
		case RIGHT:
			newPosition = getPosition().add(0, 1);
			break;
		default:
			throw new IllegalStateException("Unknown direction: " + direction.name());
		}
		if (getWorld().isOccupiable(newPosition)) {
			setPosition(newPosition);
		}
	}

	@Override
	public void gameStep(final Game game) {
		move();
	}

	public boolean canRespawn() {
		return canRespawn;
	}

	public void setCanRespawn(boolean canRespawn) {
		this.canRespawn = canRespawn;
	}
}
