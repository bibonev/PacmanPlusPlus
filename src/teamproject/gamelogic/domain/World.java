package teamproject.gamelogic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import teamproject.ai.AIGhost;
import teamproject.constants.CellState;
import teamproject.event.Event;
import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;

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
	private int latestEntityID = 1000;
	private Event<EntityAddedListener, EntityChangedEventArgs> onEntityAdded;
	private Event<EntityRemovingListener, EntityChangedEventArgs> onEntityRemoving;
	private boolean remote;

	public World(final RuleChecker ruleEnforcer, final Map map, final boolean remote) {
		entities = new HashMap<>();
		this.ruleEnforcer = ruleEnforcer;
		this.map = map;
		this.remote = remote;

		onEntityAdded = new Event<>((l, c) -> l.onEntityAdded(c));
		onEntityRemoving = new Event<>((l, c) -> l.onEntityRemoving(c));
	}
	
	/**
	 * Determines if this world is remote or not. A world is remote if you are a client
	 * connected to a server (including if you are the host - ie. connected to the server
	 * running on your own machine).
	 * 
	 * A world is <em>not</em> remote when you are playing singleplayer <em>or</em> the game
	 * is running on the server.
	 * 
	 * If the world is a remote world, then the map state and AI should <em>not</em> be
	 * updated locally - the server will run that logic and send it to the client over the
	 * network.
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
		int id;
		if (entity instanceof Player) {
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

	/**
	 * Fetch the entity with given id
	 *
	 * @param entityID
	 * @return
	 */
	public Entity getEntity(final int entityID) {
		return entities.get(entityID);
	}
	
	public boolean isGhostAt(Position position) {
		for(Entity e : getEntities()) {
			if(e instanceof Ghost && e.getPosition().equals(position)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Set<Entity> getEntitiesAt(Position position) {
		HashSet<Entity> s = new HashSet<Entity>();
		
		for(Entity e : getEntities()) {
			if(e.getPosition().equals(position)) {
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
	
	public void gameStep(Game game) {
		map.gameStep(game);
		for(Entity entity : entities.values()) {
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
			getOnEntityRemovingEvent().fire(new EntityChangedEventArgs(entityID, this));
			entities.remove(entityID);
		} else {
			throw new IllegalArgumentException("No such entity with ID " + entityID);
		}
	}
	
	public boolean isOccupiable(Position position) {
		if(RuleChecker.isOutOfBounds(position.getRow(), position.getColumn()))
			return false;
		
		if(map.getCell(position).getState() == CellState.OBSTACLE)
			return false;
		
		return true;
	}
}
