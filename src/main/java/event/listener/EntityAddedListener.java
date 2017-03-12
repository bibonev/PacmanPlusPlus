package teamproject.event.listener;

import teamproject.event.arguments.EntityChangedEventArgs;

public interface EntityAddedListener {
	public void onEntityAdded(EntityChangedEventArgs args);
}
