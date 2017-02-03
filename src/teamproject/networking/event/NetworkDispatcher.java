package teamproject.networking.event;

import teamproject.event.Event;
import teamproject.networking.NetworkManager;
import teamproject.networking.data.Packet;

/**
 * A class which listens to certain relevant game events, converts their
 * data to packets, and fires an event which informs the network manager
 * to dispatch the packet over the network.
 * 
 * @author Tom Galvin
 */
public class NetworkDispatcher {
	private Event<NetworkManager, Packet> dispatchEvent = new Event<>((m, p) -> m.dispatch(p));
	
	/**
	 * Gets the event to fire when packets are to be dispatched on the network.
	 * 
	 * @return The packet dispatch event.
	 */
	public Event<NetworkManager, Packet> getDispatchEvent() {
		return dispatchEvent;
	}
}
