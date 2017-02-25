package teamproject.gamelogic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import teamproject.ai.AIGhost;
import teamproject.event.Event;
import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;

public class World {
	private RuleEnforcer ruleEnforcer;
	private Map map;
	private HashMap<Integer, Entity> entities;
	private int latestEntityID = 1000;
	private Event<EntityAddedListener, EntityChangedEventArgs> onEntityAdded;
	private Event<EntityRemovingListener, EntityChangedEventArgs> onEntityRemoving;

	public World(final RuleEnforcer ruleEnforcer, final Map map) {
		this.entities = new HashMap<>();
		this.ruleEnforcer = ruleEnforcer;
		this.map = map;

		this.onEntityAdded = new Event<>((l, c) -> l.onEntityAdded(c));
		this.onEntityRemoving = new Event<>((l, c) -> l.onEntityRemoving(c));
	}
	
	public Event<EntityAddedListener, EntityChangedEventArgs> getOnEntityAddedEvent() {
		return onEntityAdded;
	}
	
	public Event<EntityRemovingListener, EntityChangedEventArgs> getOnEntityRemovingEvent() {
		return onEntityRemoving;
	}

	public RuleEnforcer getRuleEnforcer() {
		return ruleEnforcer;
	}

	public Map getMap() {
		return map;
	}
	
	public int addPlayer(Player player) {
		int entityID = player.getID();
		entities.put(entityID, player);
		player.setID(entityID);
		
		return entityID;
	}
	
	public int addEntity(Entity entity) {
		int id;
		if(entity instanceof Player) {
			id = addPlayer((Player)entity);
		} else {
			int entityID = latestEntityID++;
			entities.put(entityID, entity);
			entity.setID(entityID);
			id = entityID;
		}
		getOnEntityAddedEvent().fire(new EntityChangedEventArgs(id, this));
		return id;
	}
	
	public Entity getEntity(int entityID) {
		return entities.get(entityID);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> Collection<T> getEntities(Class<T> cls) {
		ArrayList<T> list = new ArrayList<T>();
		
		for(Entity e : getEntities()) {
			if(cls.isInstance(e)) {
				list.add((T)e);
			}
		}

		return list;
	}
	
	public Collection<Entity> getEntities() {
		return entities.values();
	}
	
	public Collection<Player> getPlayers() {
		return getEntities(Player.class);
	}
	
	public Collection<Ghost> getGhosts() {
		return getEntities(Ghost.class);
	}

	public void setRuleEnforcer(final RuleEnforcer ruleEnforcer) {
		this.ruleEnforcer = ruleEnforcer;
	}

	public void setMap(final Map map) {
		this.map = map;
	}

	public void removeEntity(int entityID) {
		if(entities.containsKey(entityID)) {
			getOnEntityRemovingEvent().fire(new EntityChangedEventArgs(entityID, this));
			entities.remove(entityID);
		} else {
			throw new IllegalArgumentException("No such entity with ID " + entityID);
		}
	}
}
