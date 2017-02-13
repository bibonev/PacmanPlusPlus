package teamproject.networking;

import java.util.Set;

import teamproject.event.Event;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;

/**
 * Represents a server object which can manage many connected
 * clients.
 * 
 * @author Tom Galvin
 *
 */
public interface NetworkServer {
	/**
	 * Gets the IDs of all clients currently connected to the server.
	 * 
	 * @return A set of client IDs currently connected.
	 */
	public Set<Integer> getConnectedClients();
	
	/**
	 * Gets the client with the specified ID.
	 * @param clientID The ID of the client to get.
	 * @return The client with the specified ID.
	 * @throws IllegalArgumentException Thrown when no client with the
	 * given {@code clientID} is connected. 
	 */
	public NetworkSocket getClient(int clientID);
	
	/**
	 * Gets the event which is fired when a client connects to the server.
	 * 
	 * @return The client connected event.
	 */
	public Event<ClientConnectedListener, Integer> getClientConnectedEvent();
	
	/**
	 * Gets the event which is firde when a client disconnects from the server.
	 * 
	 * @return The client disconnected event.
	 */
	public Event<ClientDisconnectedListener, Integer> getClientDisconnectedEvent();
}
