package teamproject.networking;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.ClientTrigger;
import teamproject.networking.event.ServerTrigger;

/**
 * Handles received data from the network, converts the data received
 * into game events, and sends the events to the appropriate places.
 * Also handles sending data to the network, by capturing specified
 * events, converting their data to a network-transmissible form, and
 * returning that as binary data (ie. a byte array).
 * 
 * @author Tom Galvin
 */
public class StandardServerManager
		implements ServerManager, ClientConnectedListener, ClientDisconnectedListener {
	private NetworkServer server;
	private Map<String, ServerTrigger> triggers;
	private Map<Integer, NetworkListener> clientListeners;
	
	/**
	 * Initialize a new {@code ServerNetworkManager} with the given underlying server.
	 * 
	 * @param server The server object to use for sending and receiving events.
	 */
	public StandardServerManager(NetworkServer server) {
		this.server = server;
		this.server.getClientConnectedEvent().addListener(this);
		this.server.getClientDisconnectedEvent().addListener(this);
		triggers = new HashMap<String, ServerTrigger>();
		clientListeners = new HashMap<Integer, NetworkListener>();
	}
	
	@Override
	public void addTrigger(ServerTrigger trigger, String... packetNames) {
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
	
	public void receive(int clientID, byte[] receivedData) {
		String receivedString = new String(receivedData, StandardCharsets.UTF_8);
		Packet receivedPacket = Packet.fromString(receivedString);
		ServerTrigger trigger = triggers.get(receivedPacket.getPacketName());
		
		if(trigger != null) {
			trigger.trigger(clientID, receivedPacket);
		} else {
			throw new RuntimeException(
					String.format(
							"Received packet type \"%s\" with no registered packet trigger.",
							receivedPacket.getPacketName()));
		}
	}

	@Override
	public void dispatch(int recipientID, Packet packet) {
		String stringToSend = packet.toString();
		byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		if(recipientID == -1) {
<<<<<<< HEAD
			for(int id : server.getConnectedClients()) {
				server.getClient(id).send(dataToSend);
			}
		} else {
			server.getClient(recipientID).send(dataToSend);
=======
			server.getClient(recipientID).send(dataToSend);
		} else {
			for(int id : server.getConnectedClients()) {
				server.getClient(id).send(dataToSend);
			}
>>>>>>> 0dce7173da1c11929c5ec5f6e3102e764a072efa
		}
	}

	@Override
	public void onClientDisconnected(int clientID) {
		clientListeners.remove(clientID);
	}

	@Override
	public void onClientConnected(int clientID) {
		final StandardServerManager thisManager = this;
		NetworkListener l = new NetworkListener() {
			@Override
			public void receive(byte[] receivedData) {
				thisManager.receive(clientID, receivedData);
			}
		};
		server.getClient(clientID).getReceiveEvent().addListener(l);
		clientListeners.put(clientID, l);
	}
}
