package teamproject.networking;

import teamproject.event.Event;

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
}
