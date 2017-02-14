package teamproject.networking.socket;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import teamproject.event.Event;
import teamproject.networking.NetworkServer;
import teamproject.networking.NetworkSocket;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;

/**
<<<<<<< HEAD
 * Represents running a server connecting and adding each client 
 * and sets up the listners for each event
 * @author Simeon Kostadinov
 */

public class Server extends Thread implements NetworkServer, ClientDisconnectedListener, Runnable{
	private ServerSocket serverSocket = null;
	private boolean alive = true;
	private int serverPort;
	private int currentClientNumber = 0;
	private Map<Integer, Client> clients;
	private Event<ClientConnectedListener, Integer> clientConnectedEvent;
	private Event<ClientDisconnectedListener, Integer> clientDisconnectedEvent;

	/**
	 * Initialise Server object and attaching listeners to the events for connect and disconnect
	 */
	public Server() {
		this.serverPort = Port.number;
		this.clients = new HashMap<Integer, Client>();

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
		this.serverSocket = openServerSocket();

		while (alive) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
				int clientID = currentClientNumber++;
				Client client = new Client(clientSocket, clientID);
				client.start();
				clients.put(clientID, client);
				clientConnectedEvent.fire(clientID);
				client.getDisconnectedEvent().addListener(this);
			} catch (IOException e) {
				if(alive) {
					throw new RuntimeException("Error accepting client.", e);
				} else {
					return;
					// server stopped, just exit
				}
			}

		}
	}
	
	public Event<ClientConnectedListener, Integer> getClientConnectedEvent() {
		return clientConnectedEvent;
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getClientDisconnectedEvent() {
		return clientDisconnectedEvent;
	}
	
	public void die() {
		alive = false;
	}

	private ServerSocket openServerSocket() {
		try {
			return new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + this.serverPort + ":", e);
		}
	}

	@Override
	public Set<Integer> getConnectedClients() {
		return clients.keySet();
	}

	@Override
	public NetworkSocket getClient(int clientID) {
		if(clients.containsKey(clientID)) {
			return clients.get(clientID);
		} else {
			throw new IllegalArgumentException("No client with ID " + clientID);
		}
	}

	/**
	 * Listener implementation for disconnecting a client
	 */
	@Override
	public void onClientDisconnected(int clientID) {
		clientDisconnectedEvent.fire(clientID);
		clients.get(clientID).getDisconnectedEvent().removeListeners(this);
		clients.remove(clientID);
	}
}
