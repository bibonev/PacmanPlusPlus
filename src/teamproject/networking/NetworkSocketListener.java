package teamproject.networking;

/**
 * Represents an object which may listen to a network socket for events.
 * 
 * @author Tom Galvin
 */
public interface NetworkSocketListener {
	/**
	 * Called when data is received by the socket.
	 * 
	 * @param receivedData The received data.
	 */
	public void receive(byte[] receivedData);
}
