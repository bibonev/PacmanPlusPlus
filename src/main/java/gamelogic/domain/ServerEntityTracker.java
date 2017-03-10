package teamproject.gamelogic.domain;

import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.ServerEntityUpdatedListener;

public class ServerEntityTracker implements EntityAddedListener, EntityRemovingListener {
	private ServerEntityUpdatedListener entityListener;

	public ServerEntityTracker(ServerEntityUpdatedListener listener) {
		this.entityListener = listener;
	}
	
	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			((Player)e).getOnMovedEvent().removeListener(entityListener);
		}
		if(e instanceof Ghost) {
			((Ghost)e).getOnMovedEvent().removeListener(entityListener);
		}
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			((Player)e).getOnMovedEvent().addListener(entityListener);
		}
		if(e instanceof Ghost) {
			((Ghost)e).getOnMovedEvent().addListener(entityListener);
		}
	}
}
