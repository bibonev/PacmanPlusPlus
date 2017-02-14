package teamproject.networking.socket;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import teamproject.event.Event;
import teamproject.networking.NetworkServer;
import teamproject.networking.NetworkSocket;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;

/**
 * @author Simeon Kostadinov
 *
 */

public class Server implements NetworkServer, ClientDisconnectedListener, Runnable {
	private ServerSocket serverSocket = null;
	private boolean alive = true;
	private int serverPort;
	private int currentClientNumber = 0;
	private Map<Integer, Client> clients;
	private Event<ClientConnectedListener, Integer> clientConnectedEvent;
	private Event<ClientDisconnectedListener, Integer> clientDisconnectedEvent;

	public Server() {
		this.serverPort = Port.number;
		this.clients = new HashMap<Integer, Client>();

		clientConnectedEvent = new Event<>((l, i) -> l.onClientConnected(i));
		clientDisconnectedEvent = new Event<>((l, i) -> l.onClientDisconnected(i));
	}
	
	public void start() {
		new Thread(this).start();
	}

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
					// server stopped, just exit
				}
			} finally {
				die();
			}
		}
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	public Event<ClientConnectedListener, Integer> getClientConnectedEvent() {
		return clientConnectedEvent;
	}

	@Override
	public Event<ClientDisconnectedListener, Integer> getClientDisconnectedEvent() {
		return clientDisconnectedEvent;
	}
	
	public void die() {
		if(alive) {
			alive = false;
			if(!serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					throw new RuntimeException("Couldn't kill server.", e);
				}
			}
		}
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
		if(alive) {
			return clients.keySet();
		} else {
			return new HashSet<Integer>(0);
		}
	}

	@Override
	public NetworkSocket getClient(int clientID) {
		if(clients.containsKey(clientID)) {
			return clients.get(clientID);
		} else {
			throw new IllegalArgumentException("No client with ID " + clientID);
		}
	}

	@Override
	public void onClientDisconnected(int clientID) {
		clientDisconnectedEvent.fire(clientID);
		clients.get(clientID).getDisconnectedEvent().removeListeners(this);
		clients.remove(clientID);
	}
}
