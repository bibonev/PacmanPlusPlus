package teamproject.networking.integration;

import java.util.logging.Level;
import java.util.logging.Logger;

import teamproject.constants.EntityType;
import teamproject.event.Event;
import teamproject.event.arguments.EntityChangedEventArgs;
import teamproject.event.arguments.EntityMovedEventArgs;
import teamproject.event.arguments.HostStartingMultiplayerGameEventArgs;
import teamproject.event.arguments.LobbyChangedEventArgs;
import teamproject.event.arguments.MultiplayerGameStartingEventArgs;
import teamproject.event.arguments.PlayerMovedEventArgs;
import teamproject.event.listener.EntityAddedListener;
import teamproject.event.listener.EntityRemovingListener;
import teamproject.event.listener.GameStartedListener;
import teamproject.event.listener.HostStartingMultiplayerGameListener;
import teamproject.event.listener.LobbyStateChangedListener;
import teamproject.event.listener.RemoteEntityUpdatedListener;
import teamproject.event.listener.MultiplayerGameStartingListener;
import teamproject.gamelogic.core.Lobby;
import teamproject.gamelogic.core.LobbyPlayerInfo;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Game;
import teamproject.constants.GameType;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RemoteEntityTracker;
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
		ClientConnectedListener, RemoteEntityUpdatedListener,
		EntityAddedListener, EntityRemovingListener,
		ClientDisconnectedListener, LobbyStateChangedListener,
		HostStartingMultiplayerGameListener, GameStartedListener {
	private Server server;
	private ServerManager manager;
	private Game game;
	private GameUI gameUI;
	private RemoteEntityTracker tracker;
	private Logger logger = Logger.getLogger("network-server");
	private Lobby lobby;
	private Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> multiplayerGameStartingEvent;
	
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
	public ServerInstance(GameUI gameUI, Lobby lobby) {
		this.lobby = lobby;
		this.game = null;
		this.gameUI = gameUI;
		logger.setLevel(Level.FINEST);
		this.multiplayerGameStartingEvent = new Event<>((l, a) -> l.onMultiplayerGameStarting(a));
		
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
	
	public Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> getMultiplayerGameStartingEvent() {
		return multiplayerGameStartingEvent;
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
		tracker = new RemoteEntityTracker(this);
		server.getClientConnectedEvent().addListener(this);
		server.getClientDisconnectedEvent().addListener(this);
		lobby.getLobbyStateChangedEvent().addListener(this);
	}
	
	private void addWorldGameHooks(World world) {
		world.getOnEntityAddedEvent().addListener(this);
		world.getOnEntityAddedEvent().addListener(tracker);
		world.getOnEntityRemovingEvent().addListener(this);
		world.getOnEntityRemovingEvent().addListener(tracker);
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
		
		if(game != null) removeWorldGameHooks(game.getWorld());
	}
	
	private void removeWorldGameHooks(World world) {
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
	
	private void sendInitialLobbyState(int clientID) {
		for(int i : lobby.getPlayerIDs()) {
			manager.dispatch(clientID, createPlayerJoinedLobbyPacket(lobby.getPlayer(i)));
		}
		manager.dispatch(clientID, createRulesChangedPacket(lobby.getSettingsString()));
	}
	
	private Packet createRulesChangedPacket(String[] rules) {
		Packet p = new Packet("lobby-rule-display-changed");
		
		p.setInteger("rule-strings.length", rules.length);
		for(int i = 0; i < rules.length; i++) {
			p.setString("rule-strings[" + i + "]", rules[i]);
		}
		
		return p;
	}
	
	private Packet createPlayerJoinedLobbyPacket(LobbyPlayerInfo player) {
		Packet p = new Packet("lobby-player-enter");
		p.setInteger("player-id", player.getID());
		p.setString("player-name", player.getName());
		return p;
	}
	
	private Packet createPlayerLeftLobbyPacket(int playerID) {
		Packet p = new Packet("lobby-player-left");
		p.setInteger("player-id", playerID);
		return p;
	}
	
	private Packet createForceMovePacket(int row, int column, double angle) {
		Packet p = new Packet("force-move");
		p.setInteger("row", row);
		p.setInteger("col", column);
		p.setDouble("angle", angle);
		return p;
	}
	
	@Override
	public void onEntityMoved(EntityMovedEventArgs args) {
		if(args.getEntity().getType()==EntityType.PLAYER){
			Packet p = new Packet("remote-player-moved");
			p.setInteger("row", args.getRow());
			p.setInteger("col", args.getCol());
			if(args instanceof PlayerMovedEventArgs)
				p.setDouble("angle", ((PlayerMovedEventArgs) args).getAngle());
			p.setInteger("player-id", args.getEntity().getID());
			
			if(server.getConnectedClients().contains(args.getEntity().getID())) {
				// if the player ID is connected to the server, then
				// this is an actual player
				// otherwise it's an AI player
				
				manager.dispatchAllExcept(p, args.getEntity().getID());
			} else {
				manager.dispatchAll(p);
			}
		}
		if(args.getEntity().getType()==EntityType.GHOST){
			Packet p = new Packet("remote-ghost-moved");
			p.setInteger("row", args.getRow());
			p.setInteger("col", args.getCol());
			p.setInteger("ghost-id", args.getEntity().getID());
			
			manager.dispatchAll(p);
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
		
		Entity e = game.getWorld().getEntity(sender);
		
		if(e != null && e instanceof Player) {
			Player player = (Player)e;
			if(p.hasParameter("angle"))
				player.setAngle(p.getDouble("angle"));
			player.setPosition(new Position(row, col));
		} else {
			throw new IllegalStateException("Sender ID does not correspond to player.");
		}
	}

	private void triggerHandshake(int sender, Packet p) {
		String username = p.getString("username");

		lobby.addPlayer(sender, new LobbyPlayerInfo(sender, username));
		sendInitialLobbyState(sender);
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			Packet p = new Packet("remote-player-joined");
			p.setInteger("player-id", e.getID());
			p.setString("name", ((Player) e).getName());
			p.setInteger("row", e.getPosition().getRow());
			p.setInteger("col", e.getPosition().getColumn());
			manager.dispatchAllExcept(p, e.getID());
		}
		if(e instanceof Ghost) {
			Packet p = new Packet("remote-ghost-joined");
			p.setInteger("ghost-id", e.getID());
			p.setInteger("row", e.getPosition().getRow());
			p.setInteger("col", e.getPosition().getColumn());
			manager.dispatchAll(p);
		}
	}

	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		
		if(e instanceof Player) {
			Packet p = new Packet("remote-player-left");
			p.setInteger("player-id", e.getID());
			gameUI.multiPlayerLobbyScreen.list.removePlayer(e.getID());
			manager.dispatchAllExcept(p, e.getID());
		}
		if(e instanceof Ghost) {
			Packet p = new Packet("remote-ghost-left");
			p.setInteger("ghost-id", e.getID());
			manager.dispatchAll(p);
		}
	}

	@Override
	public void onClientDisconnected(int clientID) {
		logger.log(Level.INFO, "Client {0} disconnected.", clientID);
		lobby.removePlayer(clientID);
		if(game != null) game.getWorld().removeEntity(clientID);
	}

	@Override
	public void onLobbyStateChanged(LobbyChangedEventArgs args) {
		if(args instanceof LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) {
			int id = ((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID();
			manager.dispatchAllExcept(createPlayerLeftLobbyPacket(
					((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID()),
					id);
		} else if(args instanceof LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) {
			int id = ((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerID();
			manager.dispatchAllExcept(createPlayerJoinedLobbyPacket(
					((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerInfo()),
					id);
		} else if(args instanceof LobbyChangedEventArgs.LobbyRulesChangedEventArgs) {
			manager.dispatchAll(createRulesChangedPacket(
					((LobbyChangedEventArgs.LobbyRulesChangedEventArgs) args).getNewRules()));
		}
	}

	@Override
	public void onHostStartingGame(HostStartingMultiplayerGameEventArgs args) {
		Packet p = new Packet("game-starting");
		
		// add game configuration stuff into this packet
		
		manager.dispatchAll(p);
		
		getMultiplayerGameStartingEvent().fire(
				new MultiplayerGameStartingEventArgs(args.getSettings()));
	}

	@Override
	public void onGameStarted(Game game) {
		if(game.getGameType() == GameType.MULTIPLAYER_SERVER) {
			if(this.game != null) {
				// cleanup hooks to old game
				
				removeWorldGameHooks(this.game.getWorld());
			}
			this.game = game;
			addWorldGameHooks(game.getWorld());
			logger.log(Level.INFO, "Adding remote players");
			for(int i : lobby.getPlayerIDs()) {
				LobbyPlayerInfo info = lobby.getPlayer(i);
				RemotePlayer player = new RemotePlayer(info.getID(), info.getName());
				player.setPosition(new Position(0, 0));
				game.getWorld().addEntity(player);

				manager.dispatch(i, createForceMovePacket(
						player.getPosition().getRow(),
						player.getPosition().getColumn(),
						player.getAngle()));
			}
		}
	}

	/*
	 * For posterity: a corresponding removeTriggers method is not necessary. Once
	 * the Server object's connection dies, the server manager will no longer
	 * receive packets and so cleaning up its triggers is not necessary.
	 */
}
