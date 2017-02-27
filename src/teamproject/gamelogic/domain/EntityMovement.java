package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

/**
 * Created by Boyan Bonev on 23/02/2017.
 */
public class EntityMovement {
    public Entity getEntity() {
        return entity;
    }

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

		if(entity instanceof Player){
			((Player) entity).setAngle(angle);
            world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.EMPTY);
            entity.setPosition(new Position(row, column));

			if (world.getMap().getCell(row, column).getState() == CellState.FOOD ||
                    world.getMap().getCell(row, column).getState() == CellState.EMPTY) {
				world.getMap().getCell(row, column).setState(CellState.PLAYER);
			}

			if (world.getMap().getCell(row, column).getState() == CellState.ENEMY ||
                    world.getMap().getCell(row, column).getState() == CellState.ENEMY_AND_FOOD) {
				world.getMap().getCell(row, column).setState(CellState.PLAYER_AND_ENEMY);
			}

		} else {
		    if(world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).getState() == CellState.ENEMY){
                world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.EMPTY);
            } else if(world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).getState() == CellState.ENEMY_AND_FOOD){
                world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.FOOD);
            }

            //world.getMap().getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.FOOD);
            entity.setPosition(new Position(row, column));

			if (world.getMap().getCell(row, column).getState() == CellState.PLAYER) {
				world.getMap().getCell(row, column).setState(CellState.PLAYER_AND_ENEMY);
			} else if (world.getMap().getCell(row, column).getState() == CellState.EMPTY) {
                world.getMap().getCell(row, column).setState(CellState.ENEMY);
            } else if (world.getMap().getCell(row, column).getState() == CellState.FOOD) {
                world.getMap().getCell(row, column).setState(CellState.ENEMY_AND_FOOD);
            }
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
