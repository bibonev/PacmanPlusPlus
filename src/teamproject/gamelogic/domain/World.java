package teamproject.gamelogic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import teamproject.ai.AIGhost;
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

	public World(final RuleChecker ruleEnforcer, final Map map) {
		entities = new HashMap<>();
		this.ruleEnforcer = ruleEnforcer;
		this.map = map;

		onEntityAdded = new Event<>((l, c) -> l.onEntityAdded(c));
		onEntityRemoving = new Event<>((l, c) -> l.onEntityRemoving(c));
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
	 * Add a new player in the world
	 *
	 * @param player
	 *            the player to be added
	 * @return the id of the newly added player
	 */
	public int addPlayer(final Player player) {
		final int entityID = player.getID();
		entities.put(entityID, player);
		player.setID(entityID);

		return entityID;
	}

	/**
	 * Add a new entity in the world
	 *
	 * @param entity
	 *            the entity to be added
	 * @return the id of the newly added player
	 */
	public int addEntity(final Entity entity) {
		int id;
		if (entity instanceof Player) {
			id = addPlayer((Player) entity);
		} else {
			final int entityID = latestEntityID++;
			entities.put(entityID, entity);
			entity.setID(entityID);
			id = entityID;
		}
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
}
