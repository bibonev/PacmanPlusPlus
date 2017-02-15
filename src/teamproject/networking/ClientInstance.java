package teamproject.networking;

import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.ClientDispatcher;
import teamproject.networking.event.HandshakeTrigger;
import teamproject.networking.socket.Client;

public class ClientInstance implements Runnable, ClientDisconnectedListener {
	private ClientDispatcher dispatcher;
	private Client client;
	private String serverAddress;
	private ClientManager manager;
	
	/**
	 * Creates a new client instance which, when ran, will connect to the server
	 * with the specified IP address.
	 * 
	 * Any objects which will be involved with networking (eg. the map, the game,
	 * etc.) should be passed in to this {@link ClientInstance} object via the
	 * constructor. This, in turn, will pass those objects into the appropriate
	 * triggers (so packets received from the network will update the local game
	 * state accordingly), and also register the created {@link ClientDispatcher}
	 * object as a listener to any local events (eg. player moved) which must be
	 * transmitted over the network.
	 * 
	 * @param serverAddress The IP address of the server to connect to.
	 */
	public ClientInstance(String serverAddress) {
		// Create a dispatcher object which 
		this.dispatcher = new ClientDispatcher();
		this.serverAddress = serverAddress;
	}
	
	@Override
	public void run() {
		// Create the client socket object
		this.client = this.createClient(this.serverAddress);
		this.client.getDisconnectedEvent().addListener(this);
		
		// Create 
		ClientManager manager = new StandardClientManager(client);
		dispatcher.getDispatchEvent().addListener(manager);
		
		addGameHooks();
		addTriggers();
		
		client.start();
	}
	
	/**
	 * This kills the connection to the server.
	 * 
	 * This relies on the client's disconnect event being fired in order
	 * to remove the game hooks in {@link this#onClientDisconnected(int)}.
	 */
	public void stop() {
		client.die();
	}

	@Override
	public void onClientDisconnected(int clientID) {
		removeGameHooks();
	}
	
	/**
	 * Creates the client object connecting to the server with the specified
	 * IP address.
	 * 
	 * @param ip The IP address of the server to connect to.
	 * @return The client object which, when started, will connect to the
	 * specified server.
	 */
	private Client createClient(String ip) {
		Client client = new Client(ip);
		
		return client;
	}
	
	/**
	 * This method adds the client dispatcher as a listener to any local game events
	 * which must be transmitted over the network. The dispatcher will listen to
	 * those events, transform them into a packet representing the same info, and then
	 * send them to the current client object, which sends the packets to the server.
	 * 
	 * See the comment at the top of the method for a (fictitious) example of how this
	 * would be done for a "local player moved" game event.
	 */
	private void addGameHooks() {
		/* Example of how a local player moved event would be handled.
		 * Assume the game world object was passed to the ClientInstance via the
		 * constructor.
		 * 
		 * this.gameWorld.getPlayerMovedEvent().addListener(this.dispatcher);
		 * 
		 * The dispatcher object would then implement the PlayerMovedListener interface,
		 * such that the onPlayerMoved method transforms any of the relevant data (eg.
		 * X and Y co-ordinates) and turns it into a packet, which is then dispatched
		 * to the client manager, which turns the packet into bytes and sends it to
		 * the server.
		 */
	}
	
	/**
	 * In order to prevent resource leaks and to stop the event listener lists growing
	 * increasingly large as the player joins successive online games, any of the game
	 * hooks registered in {@link this#addGameHooks()} should be removed in the exact
	 * same manner they were registered.
	 * 
	 * This is also necessary so that game events are not passed to a client manager
	 * for a connection which has since been terminated.
	 */
	private void removeGameHooks() {
		/* Corresponding example for the local player moved event (see above).
		 * 
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this.dispatcher);
		 */
	}
	
	/**
	 * This method creates triggers and registers them with the client manager.
	 * A trigger object is registered to some number of packet names (eg. a trigger
	 * may be registered to the packet type {@code "server-handshake"} if it is
	 * to handle handshake packets from the server). Any incoming packets of that
	 * type are then passed to the trigger to handle.
	 * 
	 * As it is then the trigger's responsibility to update the local game state
	 * accordingly, any game objects (eg. the map) which must be updated must be
	 * passed in as constructors to the trigger objects themselves.
	 */
	private void addTriggers() {
		manager.addTrigger(new HandshakeTrigger(client), "server-handshake");
	}
	
	/*
	 * For posterity: a corresponding removeTriggers method is not necessary. Once
	 * the Client object's connection dies, the client manager will no longer
	 * receive packets and so cleaning up its triggers is not necessary.
	 */
}
