package main.java.networking;

import main.java.networking.data.Packet;
import main.java.networking.event.ClientTrigger;

/**
 * Represents an object which dispatches events when data is received over the
 * network.
 *
 * @author Tom Galvin
 */
public interface ClientManager {
	/**
	 * Sets the trigger used when packets are received over the network.
	 *
	 * @param trigger
	 *            The trigger to use.
	 */
	public void setTrigger(ClientTrigger trigger);

	/**
	 * Gets the trigger used when packets are received over the network.
	 *
	 * @return The currently used client trigger.
	 */
	public ClientTrigger getTrigger();

	/**
	 * Dispatch a packet to the socket to be sent.
	 *
	 * @param packet
	 *            The packet to send.
	 */
	public void dispatch(Packet packet);
}
