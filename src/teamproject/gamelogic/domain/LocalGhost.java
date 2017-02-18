package teamproject.gamelogic.domain;

import teamproject.event.Event;
import teamproject.event.arguments.LocalGhostMovedEventArgs;
import teamproject.event.listener.LocalGhostMovedListener;

public class LocalGhost extends Ghost {
	private Event<LocalGhostMovedListener, LocalGhostMovedEventArgs> onMoved;
	
	public LocalGhost() {
		super();
		onMoved = new Event<>((l, p) -> l.onLocalGhostMoved(p));
	}
	
	public Event<LocalGhostMovedListener, LocalGhostMovedEventArgs> getOnMovedEvent() {
		return onMoved;
	}
}
