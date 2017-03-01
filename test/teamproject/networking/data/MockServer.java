package teamproject.networking.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import teamproject.event.Event;
import teamproject.networking.NetworkServer;
import teamproject.networking.NetworkSocket;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;

public class MockServer implements NetworkServer, ClientDisconnectedListener {
	private Event<ClientConnectedListener, Integer> connectedEvent = new Event<>((l, i) -> l.onClientConnected(i));
	private Event<ClientDisconnectedListener, Integer> disconnectedEvent = new Event<>((l, i) -> l.onClientDisconnected(i));
	private Map<Integer, MockSocket> sockets = new HashMap<Integer, MockSocket>();
	private boolean alive = true;
	private int currentID = 0;

	@Override
	public Set<Integer> getConnectedClients() {
		if(alive) {
			return sockets.keySet();
		} else {
			return new HashSet<Integer>(0);
		}
	}

	@Override
	public NetworkSocket getClient(int clientID) {
		if(alive) {
			if(sockets.containsKey(clientID)) {
				return sockets.get(clientID);
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalStateException();
		}
	}
	
	public int addClient() {
		int id = currentID++;
		MockSocket s = new MockSocket(id);
		s.getDisconnectedEvent().addListener(this);
		return id;
	}

	@Override
	public Event<ClientConnectedListener, Integer> getClientConnectedEvent() {
		return connectedEvent;
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getClientDisconnectedEvent() {
		return disconnectedEvent;
	}

	@Override
	public void die() {
		alive = false;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void onClientDisconnected(int clientID) {
		sockets.remove(clientID);
	}
	
}