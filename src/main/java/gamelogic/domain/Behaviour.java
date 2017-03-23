package main.java.gamelogic.domain;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import main.java.ai.AStar;
import main.java.ai.Target;
import main.java.constants.CellState;
import main.java.event.Event;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.listener.EntityMovedListener;

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
public abstract class Behaviour {

	/**
	 * Different types of behaviour.
	 *
	 */
	public enum Type {
		DEFAULT, AGGRESSIVE, DEFENSIVE, GHOST
	}

	/** The type of the behavior. */
	private Type type;

	/** The astar. */
	private AStar astar;

	/** The current position of the ai. */
	public Entity entity;

	/**
	 * The focus variable determines how long it takes before the AI gets bored
	 * of chasing something or how many consecutive random moves it makes before
	 * it decides to do something else. Provides some efficiency, since the A*
	 * algorithm has to be run far less often.
	 */
	private int focus;

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

	/** The inventory. */
	private SkillSet skills;

	/** The current path. */
	private ArrayList<Position> currentPath;


	private int counter;
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
		focus = rng.nextInt(4) + 1;
		this.skills = skills;
		this.type = type;
		lastPos = entity.getPosition();
		onEntityMoved = entity.getOnMovedEvent();
		counter = 0;
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

		//final ArrayList<Position> enemies = scanEnemies();
		
		int minDistance = Integer.MAX_VALUE;
		tarType=Target.RANDOM;
		Position target = pickRandomTarget();
		
		int row=entity.getPosition().getRow();
		int col=entity.getPosition().getColumn();
		
		int colStart,rowStart,rowEnd,colEnd;
		
		if(row+3>world.getMap().getMapSize()){
			rowEnd = world.getMap().getMapSize();
		}
		else{
			rowEnd = row + 3;
		}
		
		if(col+5>world.getMap().getMapSize()){
			colEnd = world.getMap().getMapSize();
		}
		else{
			colEnd = col + 3;
		}
		if(col-3<0){
			colStart = 0;
		}
		else{
			colStart = col - 3;
		}
		if(row-3<0){
			rowStart = 0;
		}
		else{
			rowStart = row - 3;
		}
		
		for(int i = rowStart;i<rowEnd;i++){
			for(int j = colStart;j<colEnd;j++){
				int distance = manhattanDistance(entity.getPosition(),world.getMap().getCells()[i][j].getPosition());
				if(world.getMap().getCells()[i][j].getState() == CellState.FOOD && minDistance>distance && (i!=row || j!=col)){
					minDistance = distance;
					target = world.getMap().getCells()[i][j].getPosition();
					tarType=Target.STATIONARY;
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
	 * Scan for enemies.
	 *
	 * @return the array list
	 */
	private ArrayList<Position> scanEnemies() {
		final ArrayList<Position> enemies = new ArrayList<Position>();
		for (final Ghost g : world.getEntities(Ghost.class)) {
			enemies.add(g.getPosition());
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

		currentPath = astar.AStarAlg(current, target);
		// path is backwards
		
		//Collections.reverse(currentPath);
		//currentPath.remove(0);

		return currentPath;
	}

	/**
	 * Run the behavior.
	 */
	public void run() {
		
		lockedTarget = pickTarget();

		switch (tarType) {
		case RANDOM: {
			
				lastPos = entity.getPosition();
				entity.setPosition(lockedTarget);

		}
			break;
			
		case STATIONARY: {

			
			currentPath = genPath(entity.getPosition(),lockedTarget);
			
			entity.setPosition(currentPath.get(0));
			
			currentPath.remove(0);

		}
			break;
			
		case ENEMY: {


			currentPath = genPath(entity.getPosition(),lockedTarget);
			
			entity.setPosition(currentPath.get(0));

			currentPath.remove(0);
		}
			break;
		}
	}
}
