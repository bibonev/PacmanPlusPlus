package teamproject.gamelogic.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import teamproject.ai.AStar;
import teamproject.ai.Target;
import teamproject.constants.CellState;

/**
 * The default behavior, which picks the closes enemy or moves at random if
 * there are no enemies on the map. There will be several behaviors that extend
 * this class. Each behavior will be able to dynamically prioritize targets on
 * the map (items, players etc.) based on the nature of the behavior. It is
 * responsible for sending move events. Constantly gets an updated version of
 * the game map.
 *
 * @author Lyubomir Pashev
 */
public abstract class Behaviour extends Thread {

	/**
	 * Different types of behaviors.
	 *
	 * @author User
	 *
	 */
	public enum Type {
		DEFAULT, AGGRESSIVE, DEFENSIVE, GHOST
	}

	public Behaviour(final Type type) {
		this.type = type;
	}

	/** The type of the behavior. */
	private Type type;

	/** The astar. */
	private AStar astar;

	/** The current position of the ai. */
	public Position currentPos;

	/** The run condition. */
	private boolean run = true;

	/**
	 * The focus variable determines how long it takes before the AI gets bored
	 * of chasing something or how many consecutive random moves it makes before
	 * it decides to do something else. Provides some efficiency, since the A*
	 * algorithm has to be run far less often.
	 */
	private int focus;

	/** A random number generator. */
	private Random rng = new Random();

	/** The speed. */
	private int speed;

	/** The locked target of the ai. */
	private Position lockedTarget;

	/** The target type. Determines what kind of enemy the ai is following */
	private Target tarType;

	/** The cells. */
	private Cell[][] cells;

	/** The map size. */
	private int mapSize;

	/** The inventory. */
	private Inventory stash;

	/** The current path. */
	private ArrayList<Position> currentPath;

	/** The priority targets. */
	// to be used in more complex behaviors
	private PriorityQueue<Item> priorityTargets;

	/**
	 * Instantiates a new behavior.
	 *
	 * @param map
	 *            the map
	 * @param startPos
	 *            the start position of the ai
	 * @param speed
	 *            the speed of the ai
	 * @param stash
	 *            the inventory
	 */
	public Behaviour(final Map map, final Position startPos, final int speed, final Inventory stash) {
		mapSize = map.getMapSize();
		currentPos = startPos;
		cells = map.getCells();
		astar = new AStar(map);
		rng = new Random();
		focus = rng.nextInt(4) + 1;
		this.stash = stash;
		this.speed = speed;
	}

	/**
	 * Pick a target for the A* algorithm. Default implementation: picks the
	 * nearest player and follows them if there are no players, moves at random
	 *
	 * @return the position
	 */
	public Position pickTarget() {

		final ArrayList<Position> enemies = scanEnemies();

		if (enemies.size() == 0) {
			return pickRandomTarget();
		}

		else {
			final int size = enemies.size();
			final int[] distances = new int[size];
			final HashMap<Integer, Position> targets = new HashMap<Integer, Position>();

			for (int i = 0; i < size; i++) {
				distances[i] = manhattanDistance(currentPos, enemies.get(i));
				targets.put(new Integer(distances[i]), enemies.get(i));

			}

			Arrays.sort(distances);
			tarType = Target.ENEMY;
			return targets.get(distances[0]);
		}
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
	 *
	 * @return the position
	 */
	private Position pickRandomTarget() {

		final int row = currentPos.getRow();
		final int column = currentPos.getColumn();
		final ArrayList<Cell> availableCells = new ArrayList<Cell>();

		if (RuleEnforcer.checkCellValidity(cells[row - 1][column])) {
			availableCells.add(cells[row - 1][column]);
		}
		if (RuleEnforcer.checkCellValidity(cells[row + 1][column])) {
			availableCells.add(cells[row + 1][column]);
		}
		if (RuleEnforcer.checkCellValidity(cells[row][column - 1])) {
			availableCells.add(cells[row][column - 1]);
		}
		if (RuleEnforcer.checkCellValidity(cells[row][column + 1])) {
			availableCells.add(cells[row][column + 1]);
		}

		tarType = Target.RANDOM;
		return availableCells.get(rng.nextInt(availableCells.size())).getPosition();
	}

	/**
	 * Scan for enemies.
	 *
	 * @return the array list
	 */
	private ArrayList<Position> scanEnemies() {
		final ArrayList<Position> enemies = new ArrayList<Position>();
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if (cells[i][j].getState() == CellState.ENEMY) {
					enemies.add(cells[i][j].getPosition());
				}
			}
		}
		return enemies;
	}

	/**
	 * Checks if target is still present.
	 *
	 * @param target
	 *            the target
	 * @return true, if target is present
	 */
	private boolean isTargetThere(final Position target) {
		return cells[target.getRow()][target.getColumn()].getState() != CellState.EMPTY;
	}

	/**
	 * Receives an updated map.
	 *
	 * @param map
	 *            the map
	 */
	// will most likely be substituted with event-based implementation
	public void updateMap(final Map map) {
		cells = map.getCells();
	}

	/**
	 * Receives an updated position.
	 *
	 * @param pos
	 *            the position
	 */
	// will most likely be substituted with event-based implementation
	public void updatePosition(final Position pos) {
		currentPos = pos;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Position getPosition() {
		return currentPos;
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
	 * Attempt to trace target in a 3x3 area.
	 *
	 * @param target
	 *            the target position
	 * @return true, if successful
	 */
	private boolean traceTarget(final Position target) {
		final int row = target.getRow();
		final int column = target.getColumn();

		if (cells[row][column].getState() == CellState.ENEMY) {
			return true;
		}
		if (cells[row - 1][column].getState() == CellState.ENEMY) {
			lockedTarget = cells[row - 1][column].getPosition();
			return true;
		}
		if (cells[row + 1][column].getState() == CellState.ENEMY) {
			lockedTarget = cells[row + 1][column].getPosition();
			return true;
		}
		if (cells[row][column - 1].getState() == CellState.ENEMY) {
			lockedTarget = cells[row][column - 1].getPosition();
			return true;
		}

		if (cells[row][column + 1].getState() == CellState.ENEMY) {
			lockedTarget = cells[row][column + 1].getPosition();
			return true;
		}
		return false;
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

		currentPath = astar.AStarAlg(current, target);

		// path is backwards
		Collections.reverse(currentPath);
		// path starts with current position so we remove it
		currentPath.remove(0);

		return currentPath;
	}

	/**
	 * Run the behavior thread.
	 */
	@Override
	public void run() {
		while (run) {

			lockedTarget = pickTarget();

			switch (tarType) {

			// if AI is forced to make a random move, it makes *focus number
			// of consecutive moves before it scans for new enemies
			case RANDOM: {

				int i = 1;
				while (i <= focus && run) {

					// TODO: send move event

					// waits for the move to be executed
					try {
						sleep(speed);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}

					lockedTarget = pickRandomTarget();
					i++;
				}
			}
				break;

			// if AI has a stationary target (item,food etc)
			// generates path and follows it until it reaches the target
			// if path becomes obstructed or item disappears ai chooses new
			// target
			case STATIONARY: {

				genPath(currentPos, lockedTarget);

				while (currentPath.size() > 0 && run && isTargetThere(lockedTarget)) {
					if (RuleEnforcer
							.checkCellValidity(cells[currentPath.get(0).getRow()][currentPath.get(0).getColumn()])) {
						// TODO: send move event
						currentPath.remove(0);

						// waits for the move to be executed
						try {
							sleep(speed);
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						currentPath.clear();
						break;
					}
				}
			}
				break;

			// if ai chases an enemy player
			// generates path to current enemy position
			// does *focus number of moves while attempting to trace the
			// target as it moves
			// after *focus number of moves it generates new path if target
			// is still locked
			// if target is lost the ai gives up and chooses a new target
			case ENEMY: {

				boolean targetLocked = true;

				while (targetLocked && run) {

					genPath(currentPos, lockedTarget);

					int i = 1;

					while (i <= focus && run) {
						if (RuleEnforcer.checkCellValidity(
								cells[currentPath.get(0).getRow()][currentPath.get(0).getColumn()])) {

							targetLocked = traceTarget(lockedTarget);

							// TODO: send move event

							currentPath.remove(0);

							// waits for the move to be executed
							try {
								sleep(speed);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							currentPath.clear();
							break;
						}
						i++;
					}
				}
			}
				break;
			}
		}
	}
}
