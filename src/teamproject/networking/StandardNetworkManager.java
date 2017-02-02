package teamproject.networking;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamproject.networking.data.Packet;
import teamproject.networking.event.NetworkTrigger;

/**
 * Handles received data from the network, converts the data received
 * into game events, and sends the events to the appropriate places.
 * Also handles sending data to the network, by capturing specified
 * events, converting their data to a network-transmissible form, and
 * returning that as binary data (ie. a byte array).
 * 
 * @author Tom Galvin
 */
public class StandardNetworkManager implements NetworkManager, NetworkSocketListener {
	private NetworkSocket socket;
	private Map<String, NetworkTrigger> triggers;
	
	/**
	 * Initialize a new {@code NetworkManager} with the given underlying socket.
	 * 
	 * @param socket The socket to use for sending and receiving events.
	 */
	public StandardNetworkManager(NetworkSocket socket) {
		this.socket = socket;
		this.socket.getReceiveEvent().addListener(this);
		triggers = new HashMap<String, NetworkTrigger>();
	}
	
	@Override
	public void addTrigger(NetworkTrigger trigger, String... packetNames) {
		for(String packetName : packetNames) {
			if(!triggers.containsKey(packetName)) {
				triggers.put(packetName, trigger);
			} else {
				throw new IllegalArgumentException(
						String.format(
								"Cannot add more than one trigger for packet name \"%s\".",
								packetName));
			}
		}
	}
	
	@Override
	public void receive(byte[] receivedData) {
		String receivedString = new String(receivedData, StandardCharsets.UTF_8);
		Packet receivedPacket = Packet.fromString(receivedString);
		NetworkTrigger trigger = triggers.get(receivedPacket.getPacketName());
		
		if(trigger != null) {
			trigger.trigger(receivedPacket);
		} else {
			throw new RuntimeException(
					String.format(
							"Received packet type \"%s\" with no registered packet trigger.",
							receivedPacket.getPacketName()));
		}
	}

	@Override
	public void dispatch(Packet packet) {
		String stringToSend = packet.toString();
		byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		socket.send(dataToSend);
	}
}
