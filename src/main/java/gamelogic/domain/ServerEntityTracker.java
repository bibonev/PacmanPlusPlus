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

		e.getOnMovedEvent().removeListener(entityListener);
	}

	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		e.getOnMovedEvent().addListener(entityListener);
	}
}
