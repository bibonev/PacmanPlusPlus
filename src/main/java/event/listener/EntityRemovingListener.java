package main.java.event.listener;

import main.java.event.arguments.EntityChangedEventArgs;

public interface EntityRemovingListener {
	public void onEntityRemoving(EntityChangedEventArgs args);
}
