/**
 *
 */
package main.java.networking.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import main.java.event.Event;
import main.java.networking.NetworkListener;
import main.java.networking.NetworkSocket;
import main.java.networking.event.ClientDisconnectedListener;

/**
 * Represents connection between client and server. Handles sending and
 * receiving data to and from the server and calling the particular events
 *
 * @author Simeon Kostadinov
 */

public class Client implements NetworkSocket, Runnable {
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private String hostname = null;
	private ClientSender sender = null;
	private ClientReceiver receiver = null;
	private Event<NetworkListener, byte[]> receiveEvent;
	private int clientID = -1;
	private boolean serverSide;
	private Event<ClientDisconnectedListener, Integer> disconnectedEvent;
	private volatile boolean alive = false;

	/**
	 * Initialise Client object using a hostname
	 */
	public Client(final String hostname) {
		this();
		this.hostname = hostname;
		serverSide = false;
	}

	/**
	 * Initialise Client object using the created socket and clientID
	 */
	public Client(final Socket socket, final int clientID) {
		this();
		this.socket = socket;
		this.clientID = clientID;
		serverSide = true;
	}

	private Client() {
		receiveEvent = new Event<>((l, b) -> l.receive(b));
		disconnectedEvent = new Event<>((l, i) -> l.onClientDisconnected(i));
	}

	/**
	 * Method for killing a particular client and closing the socket
	 */
	@Override
	public void die() {
		if (alive) {
			try {
				alive = false;
				sender.die();
				receiver.die();
				socket.close();
			} catch (final IOException e) {
				throw new RuntimeException("Could not close client.", e);
			}
		}
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Starting the thread and establishing the InputStream and OutputStream
	 * connections
	 */
	public void start() {
		try {
			if (socket == null) {
				socket = new Socket(hostname, Port.number);
			}

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

		} catch (final UnknownHostException e) {
			throw new RuntimeException("Unknown host.", e);
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't get input and output streams for client.", e);
		}

		// objects ClientSender and ClientReceiver
		sender = new ClientSender(out);
		receiver = new ClientReceiver(in, b -> receiveEvent.fire(b));

		alive = true;
		new Thread(this).start();
	}

	/**
	 * Start both sender and receiver and fire disconnect event when they are
	 * closing
	 */
	@Override
	public void run() {
		// Run them in parallel:
		sender.start();
		receiver.start();

		// Wait for them to end and close sockets.
		try {
			receiver.join();
			sender.die();
			die();
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			disconnectedEvent.fire(clientID);
			disconnectedEvent.clearListeners();
			receiveEvent.clearListeners();
			alive = false;
		}
	}

	@Override
	public void send(final byte[] data) {
		if (alive) {
			sender.send(data);
		} else {
			throw new IllegalStateException("Cannot send data to a closed socket.");
		}
	}

	public int getClientID() {
		if (alive) {
			if (clientID == -1) {
				throw new RuntimeException("Not yet received client ID from server.");
			} else {
				return clientID;
			}
		} else {
			throw new IllegalStateException("Cannot get client ID of a closed socket.");
		}
	}

	@Override
	public Event<NetworkListener, byte[]> getReceiveEvent() {
		return receiveEvent;
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getDisconnectedEvent() {
		return disconnectedEvent;
	}

	public void setClientID(final int clientID) {
		if (!serverSide) {
			this.clientID = clientID;
		} else {
			throw new RuntimeException("Cannot set client ID of client on server once initialised.");
		}
	}
}
