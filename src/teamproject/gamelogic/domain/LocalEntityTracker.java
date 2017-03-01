package teamproject.gamelogic.domain;

import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.LocalEntityUpdatedListener;

/**
 * Track the local entities (players, ghosts)
 *
 * @author aml
 *
 */
public class LocalEntityTracker implements EntityAddedListener, EntityRemovingListener {
	private LocalEntityUpdatedListener entityListener;

	public LocalEntityTracker(final LocalEntityUpdatedListener listener) {
		entityListener = listener;
	}

	/**
	 * Track a removed entity
	 */
	@Override
	public void onEntityRemoving(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof LocalPlayer) {
			((LocalPlayer) e).getOnMovedEvent().removeListener(entityListener);
		}
		if (e instanceof LocalGhost) {
			((LocalGhost) e).getOnMovedEvent().removeListener(entityListener);
		}
	}

	/**
	 * Track an added entity
	 */
	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof LocalPlayer) {
			((LocalPlayer) e).getOnMovedEvent().addListener(entityListener);
		}
		if (e instanceof LocalGhost) {
			((LocalGhost) e).getOnMovedEvent().addListener(entityListener);
		}
	}
}
