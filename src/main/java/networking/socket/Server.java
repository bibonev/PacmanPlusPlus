package main.java.networking.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.java.event.Event;
import main.java.networking.NetworkServer;
import main.java.networking.NetworkSocket;
import main.java.networking.event.ClientConnectedListener;
import main.java.networking.event.ClientDisconnectedListener;

/**
 * Represents running a server connecting and adding each client and sets up the
 * listners for each event
 *
 * @author Simeon Kostadinov
 */

public class Server implements NetworkServer, ClientDisconnectedListener, Runnable {
	private ServerSocket serverSocket = null;
	private boolean alive = true;
	private int serverPort;
	private int currentClientNumber = 0;
	private Map<Integer, Client> clients;
	private Event<ClientConnectedListener, Integer> clientConnectedEvent;
	private Event<ClientDisconnectedListener, Integer> clientDisconnectedEvent;

	/**
	 * Initialise Server object and attaching listeners to the events for
	 * connect and disconnect
	 */
	public Server() {
		serverPort = Port.number;
		clients = new HashMap<Integer, Client>();

		clientConnectedEvent = new Event<>((l, i) -> l.onClientConnected(i));
		clientDisconnectedEvent = new Event<>((l, i) -> l.onClientDisconnected(i));
	}

	public void start() {
		new Thread(this).start();
	}

	/**
	 * Start the thread by oppening a socket and starting each client
	 */
	@Override
	public void run() {
		serverSocket = openServerSocket();

		try {
			serverSocket.setSoTimeout(500);
			while (alive) {
				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
					final int clientID = currentClientNumber++;
					final Client client = new Client(clientSocket, clientID);
					client.start();
					clients.put(clientID, client);
					clientConnectedEvent.fire(clientID);
					client.getDisconnectedEvent().addListener(this);
				} catch(SocketTimeoutException e) {
					// do nothing
				} catch (final IOException e) {
					if (alive) {
						throw new RuntimeException("Error accepting client.", e);
					} else {
						return;
						// server stopped, just exit
					}
				}
			}
		} catch(IOException e) {
			throw new RuntimeException("Could not set socket acceptance timeout.", e);
		} finally {
			die();
		}
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public Event<ClientConnectedListener, Integer> getClientConnectedEvent() {
		return clientConnectedEvent;
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getClientDisconnectedEvent() {
		return clientDisconnectedEvent;
	}

	@Override
	public void die() {
		if (alive) {
			alive = false;
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (final IOException e) {
					throw new RuntimeException("Couldn't kill server.", e);
				}
			}
		}
	}

	private ServerSocket openServerSocket() {
		try {
			return new ServerSocket(serverPort);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot open port " + serverPort + ":", e);
		}
	}

	@Override
	public Set<Integer> getConnectedClients() {
		if (alive) {
			return clients.keySet();
		} else {
			return new HashSet<Integer>(0);
		}
	}

	@Override
	public NetworkSocket getClient(final int clientID) {
		if (clients.containsKey(clientID)) {
			return clients.get(clientID);
		} else {
			throw new IllegalArgumentException("No client with ID " + clientID);
		}
	}

	/**
	 * Listener implementation for disconnecting a client
	 */
	@Override
	public void onClientDisconnected(final int clientID) {
		clientDisconnectedEvent.fire(clientID);
		clients.get(clientID).getDisconnectedEvent().removeListener(this);
		clients.remove(clientID);
	}
}
