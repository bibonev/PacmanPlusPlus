package teamproject.gamelogic.domain;

import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.LocalEntityUpdatedListener;

public class LocalEntityTracker implements EntityAddedListener, EntityRemovingListener {
	private LocalEntityUpdatedListener entityListener;

	public LocalEntityTracker(LocalEntityUpdatedListener listener) {
		this.entityListener = listener;
	}
	
	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof LocalPlayer) {
			((LocalPlayer)e).getOnMovedEvent().removeListener(entityListener);
		}
		if(e instanceof LocalGhost) {
			((LocalGhost)e).getOnMovedEvent().removeListener(entityListener);
		}
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof LocalPlayer) {
			((LocalPlayer)e).getOnMovedEvent().addListener(entityListener);
		}
		if(e instanceof LocalGhost) {
			((LocalGhost)e).getOnMovedEvent().addListener(entityListener);
		}
	}
}
