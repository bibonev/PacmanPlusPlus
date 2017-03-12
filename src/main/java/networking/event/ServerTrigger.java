package main.java.networking.event;

import main.java.networking.data.Packet;

/**
 * Represents a trigger which handles a received packet and triggers the
 * relevant events.
 *
 * @author Tom Galvin
 */
public interface ServerTrigger {
	/**
	 * Call the relevant events, passing event arguments to the events which are
	 * described by the data in packet {@code p};
	 *
	 * @param sender
	 *            The client who sent the packet.
	 * @param p
	 *            The packet received.
	 */
	public void trigger(int sender, Packet p);
}
