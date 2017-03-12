package teamproject.event.listener;

import teamproject.event.arguments.EntityChangedEventArgs;

public interface EntityRemovingListener {
	public void onEntityRemoving(EntityChangedEventArgs args);
}
