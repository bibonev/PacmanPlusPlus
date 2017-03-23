package main.java.networking.socket;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClientSender thread is used to send a packet byte array to the server
 */
public class ClientSender extends Thread {
	private volatile boolean alive = true;
	private DataOutputStream out = null;
	private BlockingQueue<byte[]> packets;

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

	public void die() {
		if (alive) {
			alive = false;
			packets.add(new byte[0]);
		}
	}

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