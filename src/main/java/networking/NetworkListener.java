package teamproject.networking;

/**
 * Represents an object which may listen to a network socket for events
 * from the server..
 * 
 * @author Tom Galvin
 */
public interface NetworkListener {
	/**
	 * Called when data is received by the socket from the server.
	 * 
	 * @param receivedData The received data.
	 */
	public void receive(byte[] receivedData);
}
