package main.java.networking.event;

import main.java.networking.data.Packet;

/**
 * Represents a trigger which handles a received packet and triggers the
 * relevant events.
 *
 * @author Tom Galvin
 */
public interface ClientTrigger {
	/**
	 * Call the relevant events, passing event arguments to the events which are
	 * described by the data in packet {@code p};
	 *
	 * @param p
	 *            The packet received.
	 */
	public void trigger(Packet p);
}
