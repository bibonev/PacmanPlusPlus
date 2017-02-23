package teamproject.networking.integration;

import java.util.logging.Level;
import java.util.logging.Logger;

import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.arguments.LocalGhostMovedEventArgs;
import teamproject.event.arguments.LocalPlayerMovedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.LocalEntityUpdatedListener;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.LocalEntityTracker;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RemotePlayer;
import teamproject.gamelogic.domain.World;
import teamproject.networking.ServerManager;
import teamproject.networking.StandardServerManager;
import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.ServerTrigger;
import teamproject.networking.socket.Server;
import teamproject.ui.GameUI;

public class ServerInstance implements Runnable, ServerTrigger,
		ClientConnectedListener, LocalEntityUpdatedListener,
		EntityAddedListener, EntityRemovingListener,
		ClientDisconnectedListener {
	private Server server;
	private ServerManager manager;
	private World world;
	private GameUI gameUI;
	private LocalEntityTracker tracker;
	private Logger logger = Logger.getLogger("network-server");
	
	/**
	 * Creates a new server instance which, when ran, will connect to the server
	 * with the specified IP address.
	 * 
	 * Any objects which will be involved with networking (eg. the map, the game,
	 * etc.) should be passed in to this {@link ServerInstance} object via the
	 * constructor. This, in turn, will pass those objects into the appropriate
	 * triggers (so packets received from the network will update the local game
	 * state accordingly), and also register the created {@link ServerDispatcher}
	 * object as a listener to any local events (eg. player moved) which must be
	 * transmitted over the network.
	 */
	public ServerInstance(GameUI gameUI, World world) {
		this.world = world;
		this.gameUI = gameUI;
		logger.setLevel(Level.FINEST);
	}
	
	@Override
	public void run() {
		// Create the server socket object
		this.server = this.createServer();
		
		// Create 
		this.manager = new StandardServerManager(server);
		this.manager.setTrigger(this);
		
		addGameHooks();
		
		server.start();
	}
	
	/**
	 * This kills the connection to the server and removes game hooks.
	 */
	public void stop() {
		removeGameHooks();
		server.die();
	}
	
	/**
	 * Creates the server object.
	 * 
	 * @return The server object which, when started, will listen for clients.
	 */
	private Server createServer() {
		Server server = new Server();
		
		return server;
	}
	
	/**
	 * This method adds the server dispatcher as a listener to any local game events
	 * which must be transmitted over the network. The dispatcher will listen to
	 * those events, transform them into a packet representing the same info, and then
	 * send them to the current server object, which sends the packets to the server.
	 * 
	 * See the comment at the top of the method for a (fictitious) example of how this
	 * would be done for a "local player moved" game event.
	 */
	private void addGameHooks() {
		/* Example of how a local player moved event would be handled.
		 * Assume the game world object was passed to the ServerInstance via the
		 * constructor.
		 * 
		 * this.gameWorld.getPlayerMovedEvent().addListener(this.dispatcher);
		 * 
		 * The dispatcher object would then implement the PlayerMovedListener interface,
		 * such that the onPlayerMoved method transforms any of the relevant data (eg.
		 * X and Y co-ordinates) and turns it into a packet, which is then dispatched
		 * to the server manager, which turns the packet into bytes and sends it to
		 * the server.
		 */
		tracker = new LocalEntityTracker(this);
		world.getOnEntityAddedEvent().addListener(this);
		world.getOnEntityAddedEvent().addListener(tracker);
		world.getOnEntityRemovingEvent().addListener(this);
		world.getOnEntityRemovingEvent().addListener(tracker);
		server.getClientConnectedEvent().addListener(this);
		server.getClientDisconnectedEvent().addListener(this);
	}
	
	/**
	 * In order to prevent resource leaks and to stop the event listener lists growing
	 * increasingly large as the player joins successive online games, any of the game
	 * hooks registered in {@link this#addGameHooks()} should be removed in the exact
	 * same manner they were registered.
	 * 
	 * This is also necessary so that game events are not passed to a server manager
	 * for a connection which has since been terminated.
	 */
	private void removeGameHooks() {
		/* Corresponding example for the local player moved event (see above).
		 * 
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this.dispatcher);
		 */
		
		world.getOnEntityAddedEvent().removeListener(this);
		world.getOnEntityAddedEvent().removeListener(tracker);
		world.getOnEntityRemovingEvent().removeListener(this);
		world.getOnEntityRemovingEvent().removeListener(tracker);
	}

	@Override
	public void onClientConnected(int clientID) {
		Packet p = new Packet("server-handshake");
		p.setInteger("client-id", clientID);
		
		manager.dispatch(clientID, p);
	}

	@Override
	public void onLocalGhostMoved(LocalGhostMovedEventArgs ghost) {
		Packet p = new Packet("remote-ghost-moved");
		p.setInteger("row", ghost.getRow());
		p.setInteger("col", ghost.getCol());
		p.setInteger("ghost-id", ghost.getGhostID());
		
		manager.dispatchAllExcept(p, 0);
	}

	@Override
	public void onLocalPlayerMoved(LocalPlayerMovedEventArgs player) {
		Packet p = new Packet("remote-player-moved");
		p.setInteger("row", player.getRow());
		p.setInteger("col", player.getCol());
		p.setDouble("angle", player.getAngle());
		p.setInteger("player-id", player.getPlayerID());
		
		if(server.getConnectedClients().contains(player.getPlayerID())) {
			// if the player ID is connected to the server, then
			// this is an actual player
			// otherwise it's an AI player
			
			manager.dispatchAllExcept(p, player.getPlayerID(), 0);
		} else {
			manager.dispatchAllExcept(p, 0);
		}
	}

	@Override
	public void trigger(int sender, Packet p) {
		logger.log(Level.INFO, "Packet received: {0}", p.getPacketName());
		if(p.getPacketName().equals("client-handshake")) {
			triggerHandshake(sender, p);
		} else if(p.getPacketName().equals("player-moved")) {
			triggerPlayerMoved(sender, p);
		}
	}

	private void triggerPlayerMoved(int sender, Packet p) {
		int row = p.getInteger("row"), col = p.getInteger("col");
		double angle = p.getDouble("angle");
		
		Entity e = world.getEntity(sender);
		
		if(e != null && e instanceof Player) {
			Player player = (Player)e;
			player.setAngle(angle);
			player.setPosition(new Position(row, col));
		} else {
			throw new IllegalStateException("Sender ID does not correspond to player.");
		}
	}

	private void triggerHandshake(int sender, Packet p) {
		String username = p.getString("username");
		RemotePlayer player = new RemotePlayer(sender, username);
		world.addEntity(player);
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			Packet p = new Packet("remote-player-joined");
			p.setInteger("player-id", e.getID());
			p.setString("name", ((Player) e).getName());
			gameUI.multiPlayerLobbyScreen.list.addPlayer((Player)e);
			manager.dispatchAllExcept(p, e.getID(), 0);
		}
		if(e instanceof Ghost) {
			Packet p = new Packet("remote-ghost-joined");
			p.setInteger("ghost-id", e.getID());
			manager.dispatchAllExcept(p, 0);
		}
	}

	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			Packet p = new Packet("remote-player-left");
			p.setInteger("player-id", e.getID());
			gameUI.multiPlayerLobbyScreen.list.removePlayer(e.getID());
			manager.dispatchAllExcept(p, e.getID(), 0);
		}
		if(e instanceof Ghost) {
			Packet p = new Packet("remote-ghost-left");
			p.setInteger("ghost-id", e.getID());
			manager.dispatchAllExcept(p, 0);
		}
	}

	@Override
	public void onClientDisconnected(int clientID) {
		logger.log(Level.INFO, "Client {0} disconnected.", clientID);
		world.removeEntity(clientID);
	}
	
	/*
	 * For posterity: a corresponding removeTriggers method is not necessary. Once
	 * the Server object's connection dies, the server manager will no longer
	 * receive packets and so cleaning up its triggers is not necessary.
	 */
}
