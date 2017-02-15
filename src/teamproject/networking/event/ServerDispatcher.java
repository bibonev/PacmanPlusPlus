package teamproject.networking.event;

import teamproject.Pair;
import teamproject.event.Event;
import teamproject.networking.ServerManager;
import teamproject.networking.data.Packet;

/**
 * A class which listens to certain relevant game events, converts their
 * data to packets, and fires an event which informs the network manager
 * to dispatch the packet over the network.
 * 
 * @author Tom Galvin
 */
public class ServerDispatcher implements ClientConnectedListener {
	private Event<ServerManager, Pair<Integer, Packet>> dispatchEvent = new Event<>((m, p) -> m.dispatch(p.getLeft(), p.getRight()));
	
	/**
	 * Gets the event to fire when packets are to be dispatched on the network.
	 * 
	 * @return The packet dispatch event.
	 */
	public Event<ServerManager, Pair<Integer, Packet>> getDispatchEvent() {
		return dispatchEvent;
	}
	
	@Override
	public void onClientConnected(int clientID) {
		Packet p = new Packet("server-handshake");
		p.setInteger("client-id", clientID);
		this.getDispatchEvent().fire(new Pair<>(clientID, p));
	}
}
