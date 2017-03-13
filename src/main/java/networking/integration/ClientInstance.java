package main.java.networking.integration;

import main.java.constants.CellState;
import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.arguments.GameStartedEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.PlayerMovedEventArgs;
import main.java.event.arguments.RemoteGameEndedEventArgs;
import main.java.event.listener.GameStartedListener;
import main.java.event.listener.MultiplayerGameStartingListener;
import main.java.event.listener.RemoteGameEndedListener;
import main.java.event.listener.ServerEntityUpdatedListener;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.RemoteGameLogic;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemoteGhost;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.networking.ClientManager;
import main.java.networking.StandardClientManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientDisconnectedListener;
import main.java.networking.event.ClientTrigger;
import main.java.networking.socket.Client;
import main.java.ui.GameUI;

public class ClientInstance implements Runnable, ClientTrigger, ClientDisconnectedListener, ServerEntityUpdatedListener,
		GameStartedListener {
	private Client client;
	private String serverAddress;
	private ClientManager manager;
	private Game game;
	private RemoteGameLogic gameLogic;
	private Lobby lobby;
	private String username;
	private GameUI gameUI;
	private boolean alreadyDoneHandshake;
	private Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> multiplayerGameStartingEvent;
	private Event<RemoteGameEndedListener, RemoteGameEndedEventArgs> onRemoteGameEndedEvent;

	/**
	 * Creates a new client instance which, when ran, will connect to the server
	 * with the specified IP address.
	 *
	 * Any objects which will be involved with networking (eg. the map, the
	 * game, etc.) should be passed in to this {@link ClientInstance} object via
	 * the constructor. This, in turn, will pass those objects into the
	 * appropriate triggers (so packets received from the network will update
	 * the local game state accordingly), and also register the created new
	 * instance object as a listener to any local events (eg. player moved)
	 * which must be transmitted over the network.
	 *
	 * @param serverAddress
	 *            The IP address of the server to connect to.
	 */
	public ClientInstance(final GameUI gameUI, final String username, final String serverAddress) {
		this.username = username;
		this.serverAddress = serverAddress;
		this.gameUI = gameUI;
		alreadyDoneHandshake = false;
		lobby = new Lobby();
		this.gameUI.setLobby(lobby);
		game = null;
		gameLogic = null;
		multiplayerGameStartingEvent = new Event<>((l, a) -> l.onMultiplayerGameStarting(a));
		onRemoteGameEndedEvent = new Event<>((l, a) -> l.onRemoteGameEnded(a));
	}

	@Override
	public void run() {
		// Create the client socket object
		client = createClient(serverAddress);
		client.getDisconnectedEvent().addListener(this);

		// Create
		manager = new StandardClientManager(client);
		manager.setTrigger(this);

		addGameHooks();

		client.start();
	}

	public boolean isHost() {
		return client.getClientID() == 0;
	}

	/**
	 * This kills the connection to the server.
	 *
	 * This relies on the client's disconnect event being fired in order to
	 * remove the game hooks in {@link this#onClientDisconnected(int)}.
	 */
	public void stop() {
		client.die();
	}

	@Override
	public void onClientDisconnected(final int clientID) {
		removeGameHooks();
	}

	/**
	 * Creates the client object connecting to the server with the specified IP
	 * address.
	 *
	 * @param ip
	 *            The IP address of the server to connect to.
	 * @return The client object which, when started, will connect to the
	 *         specified server.
	 */
	private Client createClient(final String ip) {
		final Client client = new Client(ip);

		return client;
	}

	/**
	 * This method adds the client dispatcher as a listener to any local game
	 * events which must be transmitted over the network. The dispatcher will
	 * listen to those events, transform them into a packet representing the
	 * same info, and then send them to the current client object, which sends
	 * the packets to the server.
	 *
	 * See the comment at the top of the method for a (fictitious) example of
	 * how this would be done for a "local player moved" game event.
	 */
	private void addGameHooks() {
		/*
		 * Example of how a local player moved event would be handled. Assume
		 * the game world object was passed to the ClientInstance via the
		 * constructor.
		 *
		 * this.gameWorld.getPlayerMovedEvent().addListener(this);
		 *
		 * This instance object would then implement the PlayerMovedListener
		 * interface, such that the onPlayerMoved method transforms any of the
		 * relevant data (eg. X and Y co-ordinates) and turns it into a packet,
		 * which is then dispatched to the client manager, which turns the
		 * packet into bytes and sends it to the server.
		 */
	}

	private void addWorldGameHooks(final Game game, final RemoteGameLogic logic) {
		game.getPlayer().getOnMovedEvent().addListener(this);
		getOnRemoteGameEndedEvent().addListener(gameLogic);
	}

	/**
	 * In order to prevent resource leaks and to stop the event listener lists
	 * growing increasingly large as the player joins successive online games,
	 * any of the game hooks registered in {@link this#addGameHooks()} should be
	 * removed in the exact same manner they were registered.
	 *
	 * This is also necessary so that game events are not passed to a client
	 * manager for a connection which has since been terminated.
	 */
	private void removeGameHooks() {
		/*
		 * Corresponding example for the local player moved event (see above).
		 *
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this);
		 */

		if (game != null) {
			removeWorldGameHooks(game, gameLogic);
		}
	}

	private void removeWorldGameHooks(final Game game, final RemoteGameLogic remoteGameLogic) {
		game.getPlayer().getOnMovedEvent().removeListener(this);
		getOnRemoteGameEndedEvent().removeListener(remoteGameLogic);
	}

	public Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> getMultiplayerGameStartingEvent() {
		return multiplayerGameStartingEvent;
	}

	/* HANDLERS to create/deal with outgoing packets */
	@Override
	public void onEntityMoved(final EntityMovedEventArgs args) {
		if (args.getEntity() instanceof LocalPlayer) {
			final Packet p = new Packet("player-moved");
			p.setInteger("row", args.getRow());
			p.setInteger("col", args.getCol());
			if (args instanceof PlayerMovedEventArgs) {
				p.setDouble("angle", ((PlayerMovedEventArgs) args).getAngle());
			}
			manager.dispatch(p);
		}
	}

	/* TRIGGERS to deal with incoming packets */
	@Override
	public void trigger(final Packet p) {
		if (p.getPacketName().equals("server-handshake")) {
			triggerHandshake(p);
		} else if (p.getPacketName().equals("remote-player-moved")) {
			triggerRemotePlayerMoved(p);
		} else if (p.getPacketName().equals("remote-ghost-moved")) {
			triggerRemoteGhostMoved(p);
		} else if (p.getPacketName().equals("game-tile-changed")) {
			triggerGameTileChanged(p);
		} else if (p.getPacketName().equals("remote-ghost-joined")) {
			triggerRemoteGhostJoined(p);
		} else if (p.getPacketName().equals("remote-player-joined")) {
			triggerRemotePlayerJoined(p);
		} else if (p.getPacketName().equals("remote-player-left")) {
			triggerRemotePlayerLeft(p);
		} else if (p.getPacketName().equals("remote-ghost-left")) {
			triggerRemoteGhostLeft(p);
		} else if (p.getPacketName().equals("lobby-player-enter")) {
			triggerLobbyPlayerEnter(p);
		} else if (p.getPacketName().equals("lobby-player-left")) {
			triggerLobbyPlayerLeave(p);
		} else if (p.getPacketName().equals("lobby-rule-display-changed")) {
			triggerLobbyRuleDisplayChanged(p);
		} else if (p.getPacketName().equals("game-starting")) {
			triggerGameStarting(p);
		} else if (p.getPacketName().equals("force-move")) {
			triggerForceMove(p);
		} else if (p.getPacketName().equals("cell-changed")) {
			triggerCellChanged(p);
		} else if (p.getPacketName().equals("game-ended")) {
			triggerGameEnded(p);
		} else if (p.getPacketName().equals("remote-player-died")) {
			triggerPlayerDied(p);
		} else if (p.getPacketName().equals("local-player-died")) {
			triggerLocalPlayerDied(p);
		}
	}

	private void triggerLocalPlayerDied(final Packet p) {
		// TODO: handling by graphics (send event to them)
	}

	private void triggerPlayerDied(final Packet p) {
		final int playerID = p.getInteger("player-id");

		game.getWorld().removeEntity(playerID);
	}

	private void triggerGameEnded(final Packet p) {
		final String outcomeString = p.getString("outcome");
		GameOutcome outcome;

		if (outcomeString.equals("tie")) {
			outcome = new GameOutcome(GameOutcomeType.TIE);
		} else if (outcomeString.equals("ghosts-win")) {
			outcome = new GameOutcome(GameOutcomeType.GHOSTS_WON);
		} else if (outcomeString.equals("player-win")) {
			final int winnerID = p.getInteger("winner-id");
			if (lobby.containsPlayer(winnerID)) {
				final Player winner = (Player) game.getWorld().getEntity(winnerID);
				outcome = new GameOutcome(GameOutcomeType.PLAYER_WON, winner);
			} else {
				throw new IllegalStateException("Unknown winner ID: " + winnerID);
			}
		} else {
			throw new IllegalStateException("Unknown game outcome from server: " + outcomeString);
		}

		onRemoteGameEndedEvent.fire(new RemoteGameEndedEventArgs(outcome));
	}

	private void triggerCellChanged(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final String newStateStr = p.getString("new-state");

		final CellState newState = CellState.valueOf(newStateStr);

		game.getWorld().getMap().getCell(row, col).setState(newState);
	}

	private void triggerForceMove(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final double angle = p.getDouble("angle");

		game.getPlayer().setPosition(new Position(row, col));
		game.getPlayer().setAngle(angle);
	}

	private void triggerGameStarting(final Packet p) {
		final GameSettings settings = new GameSettings();
		// reconstruct game settings as needed

		final MultiplayerGameStartingEventArgs args = new MultiplayerGameStartingEventArgs(settings,
				client.getClientID(), username);

		getMultiplayerGameStartingEvent().fire(args);
	}

	private void triggerLobbyPlayerLeave(final Packet p) {
		final int playerID = p.getInteger("player-id");

		lobby.removePlayer(playerID);
	}

	private void triggerLobbyRuleDisplayChanged(final Packet p) {
		final int size = p.getInteger("rule-strings.length");
		final String[] s = new String[size];

		for (int i = 0; i < size; i++) {
			s[i] = p.getString("rule-strings[" + i + "]");
		}

		lobby.setSettingsString(s);
	}

	private void triggerLobbyPlayerEnter(final Packet p) {
		final int playerID = p.getInteger("player-id");
		final String playerName = p.getString("player-name");

		final LobbyPlayerInfo lobbyPlayerInfo = new LobbyPlayerInfo(playerID, playerName);

		lobby.addPlayer(playerID, lobbyPlayerInfo);
	}

	private void triggerRemoteGhostLeft(final Packet p) {
		final int ghostID = p.getInteger("ghost-id");

		game.getWorld().removeEntity(ghostID);
	}

	private void triggerRemoteGhostJoined(final Packet p) {
		final int ghostID = p.getInteger("ghost-id");

		final RemoteGhost ghost = new RemoteGhost(ghostID);
		ghost.setPosition(new Position(p.getInteger("row"), p.getInteger("col")));
		game.getWorld().addEntity(ghost);
	}

	private void triggerRemotePlayerJoined(final Packet p) {
		final int playerID = p.getInteger("player-id");
		final String name = p.getString("name");

		final RemotePlayer player = new RemotePlayer(playerID, name);
		player.setPosition(new Position(p.getInteger("row"), p.getInteger("col")));

		game.getWorld().addEntity(player);
	}

	private void triggerRemotePlayerLeft(final Packet p) {
		final int playerID = p.getInteger("player-id");
		game.getWorld().removeEntity(playerID);
	}

	private void triggerRemotePlayerMoved(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final int playerID = p.getInteger("player-id");

		final Entity e = game.getWorld().getEntity(playerID);

		if (e instanceof RemotePlayer) {
			final RemotePlayer player = (RemotePlayer) e;
			player.setPosition(new Position(row, col));
			if (p.hasParameter("angle")) {
				player.setAngle(p.getDouble("angle"));
			}
		} else {
			throw new RuntimeException("wut"); // TODO: error reporting
		}
	}

	private void triggerGameTileChanged(final Packet p) {
		final int x = p.getInteger("x"), y = p.getInteger("y");
		final CellState cellState = CellState.valueOf(p.getString("cell-state"));

		game.getWorld().getMap().getCell(x, y).setState(cellState);
	}

	private void triggerRemoteGhostMoved(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final int ghostID = p.getInteger("ghost-id");

		final Entity e = game.getWorld().getEntity(ghostID);

		if (e instanceof RemoteGhost) {
			final RemoteGhost ghost = (RemoteGhost) e;
			ghost.setPosition(new Position(row, col));
		} else {
			throw new RuntimeException("wut"); // TODO: error reporting
		}
	}

	private void triggerHandshake(final Packet p) {
		if (!alreadyDoneHandshake) {
			final int clientID = p.getInteger("client-id");
			client.setClientID(clientID);
			alreadyDoneHandshake = true;

			final Packet p2 = new Packet("client-handshake");
			p2.setString("username", username);
			manager.dispatch(p2);
		} else {
			throw new RuntimeException("Already received handshake packet from server.");
		}
	}

	@Override
	public void onGameStarted(final GameStartedEventArgs args) {
		if (args.getGame().getGameType() == GameType.MULTIPLAYER_CLIENT) {
			if (game != null) {
				removeWorldGameHooks(game, gameLogic);
			}
			game = args.getGame();
			gameLogic = (RemoteGameLogic) args.getGameLogic();
			addWorldGameHooks(game, gameLogic);
		}
	}

	public Event<RemoteGameEndedListener, RemoteGameEndedEventArgs> getOnRemoteGameEndedEvent() {
		return onRemoteGameEndedEvent;
	}
}
