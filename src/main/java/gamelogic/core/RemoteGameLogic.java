package main.java.gamelogic.core;

import java.util.HashSet;
import java.util.Set;

import main.java.constants.GameOutcome;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.arguments.GameDisplayInvalidatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.LocalPlayerDespawnEventArgs;
import main.java.event.arguments.LocalPlayerSpawnEventArgs;
import main.java.event.arguments.RemoteGameEndedEventArgs;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityMovedListener;
import main.java.event.listener.EntityRemovingListener;
import main.java.event.listener.RemoteGameEndedListener;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.Spawner;

public class RemoteGameLogic extends GameLogic implements EntityAddedListener, EntityRemovingListener, EntityMovedListener,
		RemoteGameEndedListener {

	private Game game;

	public RemoteGameLogic(final Game game) {
		super(game);
		this.game = game;
		
		this.game.getWorld().getOnEntityAddedEvent().addListener(this);
		this.game.getWorld().getOnEntityRemovingEvent().addListener(this);
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void gameStep(final int period) {
		game.getWorld().gameStep(game);
		Set<Integer> toRemove = new HashSet<Integer>();
		for(Spawner spawner : game.getWorld().getEntities(Spawner.class)) {
			if(spawner.isExpired()) toRemove.add(spawner.getID());
		}
		for(int id : toRemove) {
			game.getWorld().removeEntity(id);
		}
		invalidateDisplay();
	}

	private void invalidateDisplay() {
		getOnGameDisplayInvalidated().fire(new GameDisplayInvalidatedEventArgs(this));
	}

	@Override
	public void onEntityRemoving(final EntityChangedEventArgs args) {
		Entity e = game.getWorld().getEntity(args.getEntityID());
		e.getOnMovedEvent().removeListener(this);
		
		if(e instanceof ControlledPlayer) {
			getOnLocalPlayerDespawn().fire(new LocalPlayerDespawnEventArgs(
					((ControlledPlayer) e).canRespawn(),
					((ControlledPlayer) e).getDeathReason()));
		}
	}

	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		Entity e = game.getWorld().getEntity(args.getEntityID());
		e.getOnMovedEvent().addListener(this);
		if(e instanceof ControlledPlayer) {
			getOnLocalPlayerSpawn().fire(new LocalPlayerSpawnEventArgs((ControlledPlayer) e));
		}
	}

	@Override
	public void onEntityMoved(final EntityMovedEventArgs args) {
		invalidateDisplay();
	}

	private void onGameEnded(final GameOutcome outcome) {
		game.setEnded();
		getOnGameEnded().fire(new GameEndedEventArgs(this, outcome));
	}

	@Override
	public void onRemoteGameEnded(final RemoteGameEndedEventArgs args) {
		onGameEnded(args.getOutcome());
	}
}
