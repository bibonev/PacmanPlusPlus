package main.java.gamelogic.domain;

import java.util.ArrayList;
import java.util.Random;

import main.java.ai.AStar;
import main.java.ai.Target;
import main.java.constants.CellState;
import main.java.event.Event;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.listener.EntityMovedListener;

/**
 * The default AI behaviour, prioritizes food while it tries to constantly protect itself.
 * If there are nearby food nodes the AI moves towards then, if not them it moves at random
 * If there is a nearby ghost it activates its shield
 * If there is a ghost a few nodes in front of it it activates its laser.
 *
 * @author Lyubomir Pashev
 */
public abstract class Behaviour {

	/**
	 * Different types of behaviour.
	 *
	 */
	public enum Type {
		DEFAULT, GHOST
	}

	/** The type of the behavior. */
	private Type type;

	/** The astar algorithm. */
	private AStar astar;

	/** The current position of the ai. */
	public Entity entity;

	/** A random number generator. */
	private Random rng = new Random();

	/** The locked target of the ai. */
	protected Position lockedTarget;

	/** The target type. Determines what kind of enemy the ai is following */
	private Target tarType;

	/** The cells. */
	private Cell[][] cells;

	/** The map size. */
	private int mapSize;

	protected Position lastPos;

	/** The current path. */
	private ArrayList<Position> currentPath;

	private World world;

	protected Event<EntityMovedListener, EntityMovedEventArgs> onEntityMoved;

	/**
	 * Instantiates a new behavior.
	 *
	 * @param map
	 *            the map
	 * @param entity
	 *            the entity which this behaviour is controlling
	 * @param speed
	 *            the speed of the ai
	 * @param stash
	 *            the inventory
	 * @param type
	 *            the type
	 */
	public Behaviour(final World world, final Entity entity, final SkillSet skills, final Type type) {
		this.world = world;
		mapSize = this.world.getMap().getMapSize();
		this.entity = entity;
		cells = this.world.getMap().getCells();
		astar = new AStar(world.getMap());
		rng = new Random();
		this.type = type;
		lastPos = entity.getPosition();
		onEntityMoved = entity.getOnMovedEvent();
		currentPath = new ArrayList<Position>();
	}

	public Event<EntityMovedListener, EntityMovedEventArgs> getOnMovedEvent() {
		return onEntityMoved;
	}

	/**
	 * Pick a target for the A* algorithm. Default implementation: picks the
	 * nearest player and follows them if there are no players, moves at random
	 *
	 * @return the position
	 */
	public Position pickTarget() {

		// final ArrayList<Position> enemies = scanEnemies();

		int minDistance = Integer.MAX_VALUE;
		tarType = Target.RANDOM;
		Position target = pickRandomTarget();

		int row = entity.getPosition().getRow();
		int col = entity.getPosition().getColumn();

		int colStart, rowStart, rowEnd, colEnd;

		if (row + 3 > world.getMap().getMapSize()) {
			rowEnd = world.getMap().getMapSize();
		} else {
			rowEnd = row + 3;
		}

		if (col + 5 > world.getMap().getMapSize()) {
			colEnd = world.getMap().getMapSize();
		} else {
			colEnd = col + 3;
		}
		if (col - 3 < 0) {
			colStart = 0;
		} else {
			colStart = col - 3;
		}
		if (row - 3 < 0) {
			rowStart = 0;
		} else {
			rowStart = row - 3;
		}

		for (int i = rowStart; i < rowEnd; i++) {
			for (int j = colStart; j < colEnd; j++) {
				int distance = manhattanDistance(entity.getPosition(), world.getMap().getCells()[i][j].getPosition());
				if (world.getMap().getCells()[i][j].getState() == CellState.FOOD && minDistance > distance
						&& (i != row || j != col)) {
					minDistance = distance;
					target = world.getMap().getCells()[i][j].getPosition();
					tarType = Target.STATIONARY;
				}
			}
		}
		return target;
	}

	/**
	 * Manhattan distance.
	 *
	 * @param ai
	 *            the ai position
	 * @param target
	 *            the target position
	 * @return the heuristic distance
	 */
	private int manhattanDistance(final Position ai, final Position target) {
		return Math.abs(ai.getRow() - target.getRow()) + Math.abs(ai.getColumn() - target.getColumn());
	}

	/**
	 * Pick random adjacent cell to move to.
	 * The entity's last position has the lowest priority.
	 *
	 * @return the position
	 */
	protected Position pickRandomTarget() {

		final int row = entity.getPosition().getRow();
		final int column = entity.getPosition().getColumn();
		final ArrayList<Position> availableCells = new ArrayList<Position>();

		if (row > 0 && RuleChecker.checkCellValidity(cells[row - 1][column])
				&& cells[row - 1][column].getPosition().equals(lastPos) == false) {
			availableCells.add(new Position(row - 1, column));
		}
		if (row < mapSize - 1 && RuleChecker.checkCellValidity(cells[row + 1][column])
				&& cells[row + 1][column].getPosition().equals(lastPos) == false) {
			availableCells.add(new Position(row + 1, column));
		}
		if (column > 0 && RuleChecker.checkCellValidity(cells[row][column - 1])
				&& cells[row][column - 1].getPosition().equals(lastPos) == false) {
			availableCells.add(new Position(row, column - 1));
		}
		if (column < mapSize - 1 && RuleChecker.checkCellValidity(cells[row][column + 1])
				&& cells[row][column + 1].getPosition().equals(lastPos) == false) {
			availableCells.add(new Position(row, column + 1));
		}
		tarType = Target.RANDOM;
		final int size = availableCells.size();
		if (size == 0) {
			return lastPos;
		} else {
			return availableCells.get(rng.nextInt(size));
		}

	}

	/**
	 * @param range - how far away the entity scans for enemies
	 * @param scanForShieldActivation - if true it checks whether or not it is in danger 
	 * 								    and uses PacShield accordingly
	 * 									if false it checks whether or not there's a ghost nearby 
	 * 									in front of it and uses PacLaser accordingly
	 *
	 * @return the array list
	 */
	private boolean scanEnemies(int range, boolean scanForShieldActivation) {

		int row = entity.getPosition().getRow();
		int col = entity.getPosition().getColumn();

		if (scanForShieldActivation) {

			int colStart, rowStart, rowEnd, colEnd;

			if (row + range > world.getMap().getMapSize()) {
				rowEnd = world.getMap().getMapSize();
			} else {
				rowEnd = row + range;
			}

			if (col + range > world.getMap().getMapSize()) {
				colEnd = world.getMap().getMapSize();
			} else {
				colEnd = col + range;
			}
			if (col - range < 0) {
				colStart = 0;
			} else {
				colStart = col - range;
			}
			if (row - range < 0) {
				rowStart = 0;
			} else {
				rowStart = row - range;
			}

			for (final Ghost g : world.getEntities(Ghost.class)) {
				int grow = g.getPosition().getRow();
				int gcol = g.getPosition().getColumn();
				if ((grow <= rowEnd && grow >= rowStart) && (gcol <= colEnd && gcol >= colStart)) {
					return true;
				}
			}
		} else {

			double angle = ((Player) entity).getAngle();
			if (angle == 0.0) {

				for (final Ghost g : world.getEntities(Ghost.class)) {
					final int enrow = g.getPosition().getRow();
					final int encol = g.getPosition().getColumn();
					if (enrow == row && encol > col && encol <= (col + range)) {
						return true;
					}
				}
			}
			if (angle == 90.0) {

				for (final Ghost g : world.getEntities(Ghost.class)) {
					final int enrow = g.getPosition().getRow();
					final int encol = g.getPosition().getColumn();
					if (enrow <= (row + range) && enrow > row && encol == col) {
						return true;
					}
				}
			}
			if (angle == -90.0) {

				for (final Ghost g : world.getEntities(Ghost.class)) {
					final int enrow = g.getPosition().getRow();
					final int encol = g.getPosition().getColumn();
					if (enrow >= (row - range) && enrow < row && encol == col) {
						return true;
					}
				}
			}
			if (angle == 180.0) {
				for (final Ghost g : world.getEntities(Ghost.class)) {
					final int enrow = g.getPosition().getRow();
					final int encol = g.getPosition().getColumn();
					if (enrow == row && encol < col && encol >= (col - range)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Receives an updated map.
	 *
	 * @param map
	 *            the map
	 */
	public void updateMap(final Map map) {
		cells = map.getCells();
	}

	/**
	 * Receives an updated position.
	 *
	 * @param pos
	 *            the position
	 */
	public void updatePosition(final Position pos) {
		entity.setPosition(pos);
	}

	/**
	 * Gets the type of the behavior.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Generate path, reverse it and remove starting position.
	 *
	 * @param current
	 *            the start position
	 * @param target
	 *            the goal
	 * @return the array list
	 */
	private ArrayList<Position> genPath(final Position current, final Position target) {

		return astar.AStarAlg(current, target);
	}

	/**
	 * Determines which abilities the entity should use and when
	 * see scanEnemies()
	 * 
	 */
	private void selfPreserve() {

		//shield
		if (scanEnemies(2, true)) {
			((Player) entity).getSkillSet().activateW();
		}
		//laser
		if (scanEnemies(4, false)) {
			((Player) entity).getSkillSet().activateQ();
		}

	}

	/**
	 * Run the behavior.
	 */
	public void run() {

		//ability usage
		selfPreserve();
		
		//picks target
		lockedTarget = pickTarget();

		switch (tarType) {
		
		//make random move
		case RANDOM: {

			lastPos = entity.getPosition();
			entity.setPosition(lockedTarget);

		}
			break;

		//follow certain path to food node
		case STATIONARY: {

			if (currentPath.size() == 0 && manhattanDistance(entity.getPosition(), lockedTarget) == 1
					&& RuleChecker.checkCellValidity(world.getMap().getCell(lockedTarget))) {

				lockedTarget = pickTarget();
				lastPos = entity.getPosition();
				entity.setPosition(lockedTarget);

			} else if (currentPath.size() == 0) {
				currentPath = genPath(entity.getPosition(), lockedTarget);
				currentPath.remove(currentPath.size() - 1);
				if (currentPath.size() == 0)
					entity.setPosition(currentPath.get(0));
				else
					entity.setPosition(currentPath.get(currentPath.size() - 1));
				lastPos = entity.getPosition();
				currentPath.remove(currentPath.size() - 1);
			} else {
				entity.setPosition(currentPath.get(currentPath.size() - 1));
				lastPos = entity.getPosition();
				currentPath.remove(currentPath.size() - 1);
			}
		}
		}
	}
}
