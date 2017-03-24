package main.java.networking.socket;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClientSender thread is used to send a packet byte array to the server.
 * 
 * Packets are sent first as a big-endian 32-bit integer {@code l} describing
 * the length of the following packet, followed by exactly {@code l} further
 * bytes. 
 */
public class ClientSender extends Thread {
	private volatile boolean alive = true;
	private DataOutputStream out = null;
	private BlockingQueue<byte[]> packets;

	/**
	 * Initialize a new object used to send data to a socket in a
	 * threaded way.
	 * 
	 * @param out The data output stream to write packets to.
	 */
	public ClientSender(final DataOutputStream out) {
		this.out = out;
		packets = new LinkedBlockingQueue<byte[]>();
	}

	@Override
	public void run() {
		try {
			while (alive) {
				final byte[] packet = packets.take();
				if (packet.length > 0) {
					out.writeInt(packet.length);
					out.write(packet);
				} else {
					// sender died inside loop on another thread
				}
			}
		} catch (final EOFException e) {
			e.printStackTrace();
			// connection dropped
			return;
		} catch (final IOException e) {
			if (alive) {
				throw new RuntimeException("Could not send packet.", e);
			} else {
				// connection already dropped, just close thread
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException("Client thread interrupted.", e);
		} finally {
			alive = false;
		}
	}

	/**
	 * Kills the sender object. This will make the blocking {@link ClientSender#run()}
	 * method immediately terminate.
	 */
	public void die() {
		if (alive) {
			alive = false;
			packets.add(new byte[0]);
		}
	}

	/**
	 * Adds the given packet buffer to the queue of packets to send.
	 * 
	 * @param packet The packet to add to the queue of packets to send.
	 */
	public void send(final byte[] packet) {
		if (alive) {
			if(packet.length > 0) {
				packets.add(packet);
			} else {
				throw new IllegalArgumentException("Cannot send packet of length zero.");
			}
		}
	}
}