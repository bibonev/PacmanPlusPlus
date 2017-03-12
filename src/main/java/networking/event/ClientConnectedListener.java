package main.java.networking.event;

/**
 * Represents an object which receives events whenever a new client connects to
 * the server.
 *
 * @author Tom Galvin
 *
 */
public interface ClientConnectedListener {
	/**
	 * Called when a new client connects to the server.
	 *
	 * @param clientID
	 *            The ID of the client which connects to the server.
	 */
	public void onClientConnected(int clientID);
}
