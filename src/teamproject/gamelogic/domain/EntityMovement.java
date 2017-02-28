package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

/**
 * Created by Boyan Bonev on 23/02/2017.
 */
public class EntityMovement {
	private Entity entity;
	private World world;

	/**
	 * Initialization of the Movement
	 *
	 * @param entity
	 * @param world
	 */
	public EntityMovement(final Entity entity, final World world) {
		this.entity = entity;
		this.world = world;
	}

	/**
	 * Move the entity
	 *
	 * @param row
	 * @param column
	 * @param angle
	 * @return true/false whether the the move is legit
	 */
	public boolean moveTo(final int row, final int column, final double angle) {
		if (RuleChecker.isOutOfBounds(row, column)) {
			return false;
		}

		if (world.getMap().getCell(row, column).getState() == CellState.OBSTACLE) {
			return false;
		}
		entity.setPosition(new Position(row, column));
		if (entity instanceof ControlledPlayer) {
			((ControlledPlayer) entity).setAngle(angle);
		}

		if (world.getMap().getCell(row, column).getState() == CellState.FOOD) {
			world.getMap().getCell(row, column).setState(CellState.EMPTY);
		}

		return true;
	}

	/**
	 * Move the entity up
	 *
	 * @return true/false depending on whether the move is legit or not
	 */
	public boolean moveUp() {
		return moveTo(entity.getPosition().getRow() - 1, entity.getPosition().getColumn(), 270);
	}

	/**
	 * Move the entity down
	 *
	 * @return true/false depending on whether the move is legit or not
	 */
	public boolean moveDown() {
		return moveTo(entity.getPosition().getRow() + 1, entity.getPosition().getColumn(), 90);
	}

	/**
	 * Move the entity left
	 *
	 * @return true/false depending on whether the move is legit or not
	 */
	public boolean moveLeft() {
		return moveTo(entity.getPosition().getRow(), entity.getPosition().getColumn() - 1, -180);
	}

	/**
	 * Move the entity right
	 *
	 * @return true/false depending on whether the move is legit or not
	 */
	public boolean moveRight() {
		return moveTo(entity.getPosition().getRow(), entity.getPosition().getColumn() + 1, 0);
	}
}
