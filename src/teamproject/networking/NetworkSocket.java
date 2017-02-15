package teamproject.networking;

import teamproject.event.Event;
import teamproject.networking.event.ClientDisconnectedListener;

/**
 * Represents a basic socket which can send data to, or receive data from,
 * the remote socket.
 * 
 * @author Tom Galvin
 */
public interface NetworkSocket {
	/**
	 * Sends data to the remotely connected socket.
	 * 
	 * @param data The data to send.
	 */
	public void send(byte[] data);
	
	/**
	 * Gets the event which is fired when data is received from the remote socket.
	 * 
	 * @return The data deception event.
	 */
	public Event<NetworkListener, byte[]> getReceiveEvent();
	
	/**
	 * Gets the event which is fired when the connection maintained by this socket
	 * is closed, either due to network connections, or either end of the connection
	 * terminating the socket.
	 * 
	 * @return The disconnection event.
	 */
	public Event<ClientDisconnectedListener, Integer> getDisconnectedEvent();
	
	/**
	 * Kills the network connection.
	 */
	public void die();
	
	/**
	 * Determines if the socket is alive or not.
	 * 
	 * @return Returns whether this socket is alive or not.
	 */
	public boolean isAlive();
}
