package teamproject.networking.event;

/**
 * Represents an object which listens for handshake packets from
 * the server. The handshake packet contains the ID of the client
 * such that each client is aware of the ID which the server has
 * assigned to it.
 * 
 * @author Tom Galvin
 */
public interface HandshakeListener {
	/**
	 * Called when the server sends the handshake packet to the client.
	 * 
	 * @param clientID The ID which the server has assigned to the client.
	 */
	public void onServerHandshake(int clientID);
}
