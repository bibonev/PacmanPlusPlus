package teamproject.networking;

import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientTrigger;

/**
 * Represents an object which dispatches events when data is received over
 * the network.
 * 
 * @author Tom Galvin
 */
public interface ClientManager {
	/**
	 * Adds a network trigger for the given packet type. When data is received from
	 * the server, after it is converted to a readable packet, the appropriate
	 * network trigger is looked up. This handles the job of turning the packet
	 * data into useful event data, and firing the relevant event to inform the
	 * game logic that info has been received from the server.
	 * 
	 * @param trigger The trigger which is to handle packets received from the server.
	 * @param packetNames The name(s) of the packet which {@code trigger} is to handle.
	 * One trigger may handle multiple packet types. However, each packet type may only
	 * be handled by one trigger.
	 */
	public void addTrigger(ClientTrigger trigger, String... packetNames);
	
	/**
	 * Dispatch a packet to the socket to be sent.
	 * 
	 * @param packet The packet to send.
	 */
	public void dispatch(Packet packet);
}
