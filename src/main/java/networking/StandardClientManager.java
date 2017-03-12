package main.java.networking;

import java.nio.charset.StandardCharsets;

import main.java.networking.data.Packet;
import main.java.networking.event.ClientTrigger;

/**
 * Handles received data from the network, converts the data received into game
 * events, and sends the events to the appropriate places. Also handles sending
 * data to the network, by capturing specified events, converting their data to
 * a network-transmissible form, and returning that as binary data (ie. a byte
 * array).
 *
 * @author Tom Galvin
 */
public class StandardClientManager implements ClientManager, NetworkListener {
	private NetworkSocket socket;
	private ClientTrigger trigger;

	/**
	 * Initialize a new {@code ClientNetworkManager} with the given underlying
	 * socket.
	 *
	 * @param socket
	 *            The socket to use for sending and receiving events.
	 */
	public StandardClientManager(final NetworkSocket socket) {
		this.socket = socket;
		this.socket.getReceiveEvent().addListener(this);
		trigger = null;
	}

	@Override
	public void setTrigger(final ClientTrigger trigger) {
		this.trigger = trigger;
	}

	@Override
	public ClientTrigger getTrigger() {
		return trigger;
	}

	@Override
	public void receive(final byte[] receivedData) {
		final String receivedString = new String(receivedData, StandardCharsets.UTF_8);
		final Packet receivedPacket = Packet.fromString(receivedString);

		if (trigger != null) {
			trigger.trigger(receivedPacket);
		} else {
			throw new RuntimeException("No trigger is currently set.");
		}
	}

	@Override
	public void dispatch(final Packet packet) {
		final String stringToSend = packet.toString();
		final byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		socket.send(dataToSend);
	}
}
