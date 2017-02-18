package teamproject.gamelogic.domain;

import teamproject.event.Event;
import teamproject.event.listener.LocalPlayerMovedListener;
import teamproject.event.arguments.LocalPlayerMovedEventArgs;

public class LocalPlayer extends Player {
	private Event<LocalPlayerMovedListener, LocalPlayerMovedEventArgs> onMoved;
	
	public LocalPlayer(String name) {
		super(name);
		onMoved = new Event<>((l, p) -> l.onLocalPlayerMoved(p));
	}
	
	public Event<LocalPlayerMovedListener, LocalPlayerMovedEventArgs> getOnMovedEvent() {
		return onMoved;
	}
}
