package main.java.gamelogic.domain;

import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityRemovingListener;
import main.java.event.listener.ServerEntityUpdatedListener;

public class ServerEntityTracker implements EntityAddedListener, EntityRemovingListener {
	private ServerEntityUpdatedListener entityListener;

	public ServerEntityTracker(final ServerEntityUpdatedListener listener) {
		entityListener = listener;
	}

	@Override
	public void onEntityRemoving(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof Player) {
			((Player) e).getOnMovedEvent().removeListener(entityListener);
		}
		if (e instanceof Ghost) {
			((Ghost) e).getOnMovedEvent().removeListener(entityListener);
		}
	}

	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof Player) {
			((Player) e).getOnMovedEvent().addListener(entityListener);
		}
		if (e instanceof Ghost) {
			((Ghost) e).getOnMovedEvent().addListener(entityListener);
		}
	}
}
