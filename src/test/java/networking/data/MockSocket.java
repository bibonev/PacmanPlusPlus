package test.java.networking.data;

import main.java.event.Event;
import main.java.networking.NetworkListener;
import main.java.networking.NetworkSocket;
import main.java.networking.event.ClientDisconnectedListener;

public class MockSocket implements NetworkSocket {
	private Event<NetworkListener, byte[]> recvEvent = new Event<>((l, d) -> l.receive(d));
	private Event<MockSendListener, byte[]> mockSendEvent = new Event<>((l, d) -> l.onDataSent(d));
	private Event<ClientDisconnectedListener, Integer> discEvent = new Event<>((l, d) -> l.onClientDisconnected(d));
	private boolean alive = true;
	private int clientID;

	public MockSocket(final int clientID) {
		this.clientID = clientID;
	}

	@Override
	public void send(final byte[] data) {
		if (alive) {
			mockSendEvent.fire(data);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public Event<NetworkListener, byte[]> getReceiveEvent() {
		return recvEvent;
	}

	/**
	 * Gets the mock event which is fired when data is received over the
	 * network.
	 *
	 * @return The mock data sending event.
	 */
	public Event<MockSendListener, byte[]> getMockSendListener() {
		return mockSendEvent;
	}

	/**
	 * Instructs the mock socket object to act as if the data contained in
	 * {@code data} was received over the network.
	 *
	 * @param data
	 *            The fake data to receive.
	 */
	public void receive(final byte[] data) {
		if (alive) {
			recvEvent.fire(data);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getDisconnectedEvent() {
		return discEvent;
	}

	@Override
	public void die() {
		if (alive) {
			alive = false;
			discEvent.fire(clientID);
		}
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	public static interface MockSendListener {
		public void onDataSent(byte[] data);
	}
}