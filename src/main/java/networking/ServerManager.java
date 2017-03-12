package main.java.networking;

import main.java.networking.data.Packet;
import main.java.networking.event.ServerTrigger;

/**
 * Represents an object which dispatches events when data is received over the
 * network.
 *
 * @author Tom Galvin
 */
public interface ServerManager {
	/**
	 * Sets the trigger used when packets are received over the network.
	 *
	 * @param trigger
	 *            The trigger to use.
	 */
	public void setTrigger(ServerTrigger trigger);

	/**
	 * Gets the trigger used when packets are received over the network.
	 *
	 * @return The currently used server trigger.
	 */
	public ServerTrigger getTrigger();

	/**
	 * Dispatch a packet to a specific client's socket.
	 *
	 * @param clientID
	 *            the Id of the client to send to.
	 * @param packet
	 *            The packet to send.
	 */
	public void dispatch(int clientID, Packet packet);

	/**
	 * Dispatch a packet to the socket of every connected client.
	 *
	 * @param packet
	 *            The packet to send.
	 */
	public void dispatchAll(Packet packet);

	/**
	 * Dispatch a packet to the socket of every connected client, except the one
	 * with the specified client IDs.
	 *
	 * @param packet
	 *            The packet to send.
	 * @param clientID
	 *            the IDs of the client to not send the packet to.
	 */
	public void dispatchAllExcept(Packet packet, int... clientID);
}
