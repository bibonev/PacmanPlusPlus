package teamproject.networking.event;

/**
 * Represents an object which receives events whenever a client disonnects
 * from the server.
 * 
 * @author Tom Galvin
 *
 */
public interface ClientDisconnectedListener {
	/**
	 * Called when a client disconnects from the server.
	 * 
	 * @param clientID The ID of the client which disconnects from the server.
	 */
	public void onClientDisconnected(int clientID);
}
