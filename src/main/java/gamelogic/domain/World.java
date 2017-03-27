package main.java.gamelogic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.java.constants.CellState;
import main.java.event.Event;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityRemovingListener;

/**
 * Represent a game's world
 *
 * @author aml
 *
 */
public class World {
	private RuleChecker ruleEnforcer;
	private Map map;
	private HashMap<Integer, Entity> entities;
	private volatile int latestEntityID = 1000;
	private Event<EntityAddedListener, EntityChangedEventArgs> onEntityAdded;
	private Event<EntityRemovingListener, EntityChangedEventArgs> onEntityRemoving;
	private boolean remote;
	private Object addObjectSync = new Object();

	public World(final RuleChecker ruleEnforcer, final Map map, final boolean remote) {
		entities = new HashMap<>();
		this.ruleEnforcer = ruleEnforcer;
		this.map = map;
		this.remote = remote;

		onEntityAdded = new Event<>((l, c) -> l.onEntityAdded(c));
		onEntityRemoving = new Event<>((l, c) -> l.onEntityRemoving(c));
	}

	/**
	 * Determines if this world is remote or not. A world is remote if you are a
	 * client connected to a server (including if you are the host - ie.
	 * connected to the server running on your own machine).
	 *
	 * A world is <em>not</em> remote when you are playing singleplayer
	 * <em>or</em> the game is running on the server.
	 *
	 * If the world is a remote world, then the map state and AI should
	 * <em>not</em> be updated locally - the server will run that logic and send
	 * it to the client over the network.
	 *
	 * @return Returns whether the game is remote or not.
	 */
	public boolean isRemote() {
		return remote;
	}

	/**
	 * Fetch the on entity added event
	 *
	 * @return an event
	 */
	public Event<EntityAddedListener, EntityChangedEventArgs> getOnEntityAddedEvent() {
		return onEntityAdded;
	}

	/**
	 * Fetch the on entity removed event
	 *
	 * @return an event
	 */
	public Event<EntityRemovingListener, EntityChangedEventArgs> getOnEntityRemovingEvent() {
		return onEntityRemoving;
	}

	/**
	 * Fetch the world's rule checker
	 *
	 * @return the checker object
	 */
	public RuleChecker getRuleEnforcer() {
		return ruleEnforcer;
	}

	/**
	 * Fetch the map
	 *
	 * @return a map object
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Add a new entity in the world
	 *
	 * @param entity
	 *            the entity to be added
	 * @return the id of the newly added entity
	 */
	public int addEntity(final Entity entity) {
		synchronized (addObjectSync) {
			int id;
			if (entity.getID() > -1) {
				id = entity.getID();
			} else {
				id = latestEntityID++;
				entity.setID(id);
			}
			entities.put(id, entity);
			entity.setWorld(this);
			getOnEntityAddedEvent().fire(new EntityChangedEventArgs(id, this));
			return id;	
		}
	}

	/**
	 * Fetch the entity with given id
	 *
	 * @param entityID
	 * @return
	 */
	public Entity getEntity(final int entityID) {
		return entities.get(entityID);
	}

	public boolean isGhostAt(final Position position) {
		for (final Entity e : getEntities()) {
			if (e instanceof Ghost && e.getPosition().equals(position)) {
				return true;
			}
		}

		return false;
	}

	public Set<Entity> getEntitiesAt(final Position position) {
		final HashSet<Entity> s = new HashSet<Entity>();

		for (final Entity e : getEntities()) {
			if (e.getPosition().equals(position)) {
				s.add(e);
			}
		}

		return s;
	}

	/**
	 * Fetch all the entities with the given class
	 *
	 * @param cls
	 *            the class
	 * @return collection of entities
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> Collection<T> getEntities(final Class<T> cls) {
		final ArrayList<T> list = new ArrayList<T>();

		for (final Entity e : getEntities()) {
			if (cls.isInstance(e)) {
				list.add((T) e);
			}
		}

		return list;
	}

	/**
	 * Get all the entities
	 *
	 * @return collection of entities
	 */
	public Collection<Entity> getEntities() {
		return entities.values();
	}

	/**
	 * Get the players
	 *
	 * @return colleciton of players
	 */
	public Collection<Player> getPlayers() {
		return getEntities(Player.class);
	}

	public Collection<Ghost> getGhosts() {
		return getEntities(Ghost.class);
	}

	/**
	 * Update the rule checker
	 *
	 * @param ruleEnforcer
	 *            the new checker
	 */
	public void setRuleEnforcer(final RuleChecker ruleEnforcer) {
		this.ruleEnforcer = ruleEnforcer;
	}

	/**
	 * Update the map
	 *
	 * @param map
	 *            the new map
	 */
	public void setMap(final Map map) {
		this.map = map;
	}

	public void gameStep(final Game game) {
		map.gameStep(game);
		for (final Entity entity : entities.values()) {
			entity.gameStep(game);
		}
	}

	/**
	 * Remove an entity
	 *
	 * @param entityID
	 *            the id of the entity to be removed
	 */
	public void removeEntity(final int entityID) {
		if (entities.containsKey(entityID)) {
			Entity e = entities.get(entityID);
			getOnEntityRemovingEvent().fire(new EntityChangedEventArgs(entityID, this));
			entities.remove(entityID);
		} else {
			throw new IllegalArgumentException("No such entity with ID " + entityID);
		}
	}

	public boolean isOccupiable(final Position position) {
		return !RuleChecker.isOutOfBounds(position.getRow(), position.getColumn())
				&& map.getCell(position).getState() != CellState.OBSTACLE;

	}

	
	public double getSpawnPositionRating(Position p) {
		CellState cellState = getMap().getCell(p).getState();
		if(cellState == CellState.OBSTACLE || RuleChecker.isOutOfBounds(p.getRow(), p.getColumn())) return 0;
		
		double rating = 1;
		double aggregateDistance = 0;
		int entityCount = 0;
		for(Entity e : getEntities()) {
			double distance = Math.abs(e.getPosition().getRow() - p.getRow()) +
					Math.abs(e.getPosition().getColumn() - p.getColumn());
			
			if(e instanceof Spawner) e = ((Spawner) e).getEntity();
			if(e instanceof Player) {
				aggregateDistance += Math.log(distance);
			}
			if(e instanceof Ghost) {
				aggregateDistance += 2 * Math.log(distance);
			}
			entityCount += 1;
		}
		if(entityCount > 0) rating += aggregateDistance / entityCount;
		if(cellState == CellState.FOOD) rating *= 0.75;
		
		return rating;
	}
	
	public Position getCandidateSpawnPosition() {
		Position bestPosition = null;
		double bestRating = 0;
		int mapSize = getMap().getMapSize();
		
		for(int row = 0; row < mapSize; row++) {
			for(int col = 0; col < mapSize; col++) {
				Position position = new Position(row, col);
				double rating = getSpawnPositionRating(position);
				
				if(rating > bestRating) {
					bestRating = rating;
					bestPosition = position;
				}
			}
		}
		
		return bestPosition;
	}
}
