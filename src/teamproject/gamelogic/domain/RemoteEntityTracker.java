package teamproject.gamelogic.domain;

import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.RemoteEntityUpdatedListener;

public class RemoteEntityTracker implements EntityAddedListener, EntityRemovingListener {
	private RemoteEntityUpdatedListener entityListener;

	public RemoteEntityTracker(RemoteEntityUpdatedListener listener) {
		this.entityListener = listener;
	}
	
	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof RemotePlayer) {
			((RemotePlayer)e).getOnMovedEvent().removeListener(entityListener);
		}
		if(e instanceof RemoteGhost) {
			((RemoteGhost)e).getOnMovedEvent().removeListener(entityListener);
		}
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof RemotePlayer) {
			((RemotePlayer)e).getOnMovedEvent().addListener(entityListener);
		}
		if(e instanceof RemoteGhost) {
			((RemoteGhost)e).getOnMovedEvent().addListener(entityListener);
		}
	}
}
