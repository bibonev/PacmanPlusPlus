package teamproject.gamelogic.domain;

import teamproject.constants.CellState;

/**
 * Created by Boyan Bonev on 23/02/2017.
 */
public class EntityMovement {
    private Entity entity;
	private Map map;

	/**
	 * Initialization of the Movement
	 *
	 * @param entity
	 * @param map
	 */
	public EntityMovement(final Entity entity, final Map map) {
		this.entity = entity;
		this.map = map;
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

		if (map.getCell(row, column).getState() == CellState.OBSTACLE) {
			return false;
		}

		if(entity instanceof Player){
            movePlayer(row,column,angle);
		} else {
		    moveGhost(row,column);
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
	
	private void movePlayer(final int row, final int column, final double angle){
        ((Player) entity).setAngle(angle);
        map.getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.EMPTY);
        entity.setPosition(new Position(row, column));

        if (map.getCell(row, column).getState() == CellState.FOOD ||
                map.getCell(row, column).getState() == CellState.EMPTY) {
            map.getCell(row, column).setState(CellState.PLAYER);
        }

        if (map.getCell(row, column).getState() == CellState.ENEMY ||
                map.getCell(row, column).getState() == CellState.ENEMY_AND_FOOD) {
            map.getCell(row, column).setState(CellState.PLAYER_AND_ENEMY);
        }
    }
    
    private void moveGhost(int row, int column){
        if(map.getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).getState() == CellState.ENEMY){
            map.getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.EMPTY);
        } else if(map.getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).getState() == CellState.ENEMY_AND_FOOD){
            map.getCell(entity.getPosition().getRow(), entity.getPosition().getColumn()).setState(CellState.FOOD);
        }

        entity.setPosition(new Position(row, column));

        if (map.getCell(row, column).getState() == CellState.PLAYER) {
            map.getCell(row, column).setState(CellState.PLAYER_AND_ENEMY);
        } else if (map.getCell(row, column).getState() == CellState.EMPTY) {
            map.getCell(row, column).setState(CellState.ENEMY);
        } else if (map.getCell(row, column).getState() == CellState.FOOD) {
            map.getCell(row, column).setState(CellState.ENEMY_AND_FOOD);
        }
    }
}
