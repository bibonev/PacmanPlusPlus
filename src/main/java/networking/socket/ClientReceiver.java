package main.java.networking.socket;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * ClientReceiver thread is used to recieve the packet byte array from the
 * server
 */
public class ClientReceiver extends Thread {
	private boolean alive = true;
	private DataInputStream in = null;
	private Consumer<byte[]> onReceive;

	public ClientReceiver(final DataInputStream in, final Consumer<byte[]> onReceive) {
		this.in = in;
		this.onReceive = onReceive;
	}

	@Override
	public void run() {
		while (alive) {
			try {
				final int length = in.readInt();

				if (length > 0) {
					final byte[] message = new byte[length];
					in.read(message);
					onReceive.accept(message);
				} else {
					throw new IllegalStateException("Received packet of zero length.");
				}
			} catch (final EOFException e) {
				return;
			} catch (final IOException e) {
				if (alive) {
					throw new RuntimeException(e);
				} else {
					// connection already ended, just close thread
				}
			}
		}
	}

	public void die() {
		if (alive) {
			alive = false;
		}
	}
}