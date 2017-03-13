package main.java.event.listener;

import main.java.event.arguments.EntityChangedEventArgs;

public interface EntityAddedListener {
	public void onEntityAdded(EntityChangedEventArgs args);
}
