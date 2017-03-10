package teamproject.networking;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;
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
	private ServerTrigger trigger;
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
		this.trigger = null;
		clientListeners = new HashMap<Integer, NetworkListener>();
	}
	
	@Override
	public void setTrigger(ServerTrigger trigger) {
		this.trigger = trigger;
	}
	
	@Override
	public ServerTrigger getTrigger() {
		return trigger;
	}
	
	public void receive(int clientID, byte[] receivedData) {
		String receivedString = new String(receivedData, StandardCharsets.UTF_8);
		Packet receivedPacket = Packet.fromString(receivedString);
		
		if(trigger != null) {
			trigger.trigger(clientID, receivedPacket);
		} else {
			throw new RuntimeException("No trigger currently set.");
		}
	}

	@Override
	public void dispatch(int recipientID, Packet packet) {
		String stringToSend = packet.toString();
		byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		
		server.getClient(recipientID).send(dataToSend);
	}

	@Override
	public void dispatchAll(Packet packet) {
		String stringToSend = packet.toString();
		byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		
		for(int id : server.getConnectedClients()) {
			server.getClient(id).send(dataToSend);
		}
	}

	@Override
	public void dispatchAllExcept(Packet packet, int... clientIDs) {
		String stringToSend = packet.toString();
		byte[] dataToSend = stringToSend.getBytes(StandardCharsets.UTF_8);
		
		outer: for(int id : server.getConnectedClients()) {
			for(int i = 0; i < clientIDs.length; i++) {
				if(id == clientIDs[i]) continue outer;
			}
			
			server.getClient(id).send(dataToSend);
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
