package main.java.networking.integration;

import main.java.constants.CellState;
import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.arguments.LocalPlayerDespawnEventArgs;
import main.java.event.arguments.LocalPlayerSpawnEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.PlayerAbilityUsedEventArgs;
import main.java.event.arguments.PlayerMovedEventArgs;
import main.java.event.arguments.ReadyToStartEventArgs;
import main.java.event.arguments.RemoteGameEndedEventArgs;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.LocalPlayerDespawnListener;
import main.java.event.listener.LocalPlayerSpawnListener;
import main.java.event.listener.MultiplayerGameStartingListener;
import main.java.event.listener.PlayerAbilityUsedListener;
import main.java.event.listener.PlayerLeavingGameListener;
import main.java.event.listener.ReadyToStartListener;
import main.java.event.listener.RemoteGameEndedListener;
import main.java.event.listener.ServerEntityUpdatedListener;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.RemoteGameLogic;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemoteGhost;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.RemoteSkillSet;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.networking.ClientManager;
import main.java.networking.StandardClientManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientDisconnectedListener;
import main.java.networking.event.ClientTrigger;
import main.java.networking.socket.Client;
import main.java.ui.GameInterface;
import main.java.ui.GameUI;

public class ClientInstance implements Runnable, ClientTrigger, ClientDisconnectedListener, ServerEntityUpdatedListener,
		GameCreatedListener, LocalPlayerSpawnListener, LocalPlayerDespawnListener, ReadyToStartListener, PlayerLeavingGameListener, PlayerAbilityUsedListener {
	private Client client;
	private String serverAddress;
	protected ClientManager manager;
	private Game game;
	private RemoteGameLogic gameLogic;
	private ControlledPlayer player;
	private Lobby lobby;
	private String username;
	private GameInterface gameUI;
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
	public ClientInstance(final GameInterface gameUI, final String username, final String serverAddress) {
		this.username = username;
		this.serverAddress = serverAddress;
		this.gameUI = gameUI;
		alreadyDoneHandshake = false;
		lobby = new Lobby();
		this.gameUI.setLobby(lobby);
		game = null;
		player = null;
		gameLogic = null;
		multiplayerGameStartingEvent = new Event<>((l, a) -> l.onMultiplayerGameStarting(a));
		onRemoteGameEndedEvent = new Event<>((l, a) -> l.onRemoteGameEnded(a));
		gameUI.getOnPlayerLeavingGame().addOneTimeListener(this);
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
		gameUI.onPlayerLeavingGame();
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
	protected void addGameHooks() {
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

	/**
	 * Adds this client instance as a listener to any relevant events in the
	 * currently-used {@link Game} and {@link GameLogic} objects.
	 * 
	 * @param game The game object that this client instance is to attach to.
	 * @param logic The game logic object controlling the game which this
	 * client instance is to attach to.
	 */
	private void addWorldGameHooks(final Game game, final RemoteGameLogic logic) {
		logic.getOnLocalPlayerSpawn().addListener(this);
		logic.getOnLocalPlayerDespawn().addListener(this);
		logic.getOnReadyToStart().addListener(this);
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
	protected void removeGameHooks() {
		/*
		 * Corresponding example for the local player moved event (see above).
		 *
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this);
		 */

		if (game != null) {
			removeWorldGameHooks(game, gameLogic);
		}
	}

	/**
	 * Removes this {@link ClientInstance} object as a listener from any relevant
	 * events in the currently-used {@link Game} and {@link GameLogic} objects.
	 * 
	 * @param game The game object that this client instance is to detach from.
	 * @param logic The game logic object controlling the game which this
	 * client instance is to detach from.
	 */
	private void removeWorldGameHooks(final Game game, final RemoteGameLogic remoteGameLogic) {
		getOnRemoteGameEndedEvent().removeListener(remoteGameLogic);
		remoteGameLogic.getOnLocalPlayerSpawn().removeListener(this);
		remoteGameLogic.getOnLocalPlayerDespawn().removeListener(this);
		remoteGameLogic.getOnReadyToStart().removeListener(this);
	}

	/**
	 * Gets the event which this client instance will fire when the remote game on
	 * the server transitions from the lobby state to an actual game in progress.
	 */
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
		} else if (p.getPacketName().equals("remote-ghost-joined")) {
			triggerRemoteGhostJoined(p);
		} else if (p.getPacketName().equals("remote-player-joined")) {
			triggerRemotePlayerJoined(p);
		}  else if (p.getPacketName().equals("remote-ghost-died")) {
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
			triggerRemotePlayerDied(p);
		} else if (p.getPacketName().equals("local-player-died")) {
			triggerLocalPlayerDied(p);
		} else if (p.getPacketName().equals("local-player-joined")) {
			triggerLocalPlayerJoined(p);
		} else if (p.getPacketName().equals("count-down-started")) {
			triggerCountDown(p);
		} else if(p.getPacketName().equals("spawner-added")) {
			triggerSpawnerAdded(p);
		}
	}
	
	/**
	 * Handles received packets which indicate that a countdown spawner
	 * has been added to the world.
	 * 
	 * @param p
	 */
	private void triggerSpawnerAdded(Packet p) {
		Spawner.SpawnerColor color;
		String spawnerType = p.getString("entity-type");
		switch(spawnerType) {
		case "local-player": color = SpawnerColor.GREEN; break;
		case "remote-player": color = SpawnerColor.YELLOW; break;
		case "ghost": color = SpawnerColor.RED; break;
		default: color = SpawnerColor.CYAN; break;
		}
		Spawner s = new Spawner(p.getInteger("duration"), null, color);
		s.setPosition(new Position(p.getInteger("row"), p.getInteger("col")));
		game.getWorld().addEntity(s);
	}

	/**
	 * Handles received packets which indicate that the lobby countdown.
	 * 
	 * @param p
	 */
	private void triggerCountDown(final Packet p) {
		gameUI.timer();
	}

	/**
	 * Handles received packets which indicate that the player which the
	 * current client is controlling has been (re-)added to the world.
	 * 
	 * @param p
	 */
	private void triggerLocalPlayerJoined(Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final double angle = p.getDouble("angle");

		ControlledPlayer player = new ControlledPlayer(client.getClientID(), username);
		RemoteSkillSet remoteSkillSet = new RemoteSkillSet(player);
		remoteSkillSet.getOnPlayerAbilityUsed().addListener(this);
		player.setSkillSet(remoteSkillSet);
		
		player.setPosition(new Position(row, col));
		player.setAngle(angle);
		game.getWorld().addEntity(player);
	}

	/**
	 * Handles received packets which indicate that the player which the
	 * current client is controlling has died and has been removed from the
	 * world.
	 * 
	 * @param p
	 */
	private void triggerLocalPlayerDied(final Packet p) {
		if(game.getWorld().getEntity(client.getClientID()) != null) {
			Entity e = game.getWorld().getEntity(client.getClientID());
			if(e instanceof ControlledPlayer) {
				((ControlledPlayer) e).setCanRespawn(p.getBoolean("rejoinable"));
				((ControlledPlayer) e).setDeathReason(p.getString("message"));
			}
			game.getWorld().removeEntity(client.getClientID());
		}
	}

	/**
	 * Handles received packets which indicate that another player has
	 * died and is removed from the world.
	 * 
	 * @param p
	 */
	private void triggerRemotePlayerDied(final Packet p) {
		final int playerID = p.getInteger("player-id");

		game.getWorld().removeEntity(playerID);
	}

	/**
	 * Handles packets which indicate that the game has ended with a specified
	 * outcome.
	 * 
	 * @param p
	 */
	private void triggerGameEnded(final Packet p) {
		final String outcomeString = p.getString("outcome");
		GameOutcome outcome;

		if (outcomeString.equals("tie")) {
			outcome = new GameOutcome(GameOutcomeType.TIE);
		} else if (outcomeString.equals("ghosts-won")) {
			outcome = new GameOutcome(GameOutcomeType.GHOSTS_WON);
		} else if (outcomeString.equals("player-won")) {
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

	/**
	 * Handles packets which indicate that a cell on the map has changed state.
	 * 
	 * @param p
	 */
	private void triggerCellChanged(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final String newStateStr = p.getString("new-state");

		final CellState newState = CellState.valueOf(newStateStr);

		game.getWorld().getMap().getCell(row, col).setState(newState);
	}

	/**
	 * Handles packets which indicate that the player's position is to be
	 * forcibly moved.
	 * 
	 * @param p
	 */
	private void triggerForceMove(final Packet p) {
		if(player != null) {
			final int row = p.getInteger("row"), col = p.getInteger("col");
			final double angle = p.getDouble("angle");
	
			player.setPosition(new Position(row, col));
			player.setAngle(angle);
		} else {
			// received force move packet from server, but the
			// player is despawned - ignore this for now
		}
	}

	/**
	 * Handles packets which indicate that the game is starting.
	 * 
	 * @param p
	 */
	private void triggerGameStarting(final Packet p) {
		final GameSettings settings = new GameSettings();
		settings.setInitialPlayerLives(p.getInteger("initial-player-lives"));
		// reconstruct game settings as needed

		final MultiplayerGameStartingEventArgs args = new MultiplayerGameStartingEventArgs(settings,
				client.getClientID(), username);

		getMultiplayerGameStartingEvent().fire(args);
	}

	/**
	 * Handles packets which indicate that a remote player is leaving the
	 * lobby in the game.
	 * @param p
	 */
	private void triggerLobbyPlayerLeave(final Packet p) {
		final int playerID = p.getInteger("player-id");

		lobby.removePlayer(playerID);
	}

	/**
	 * Handles packets which indicate that the rule display in the pre-game lobby
	 * has changed.
	 * 
	 * @param p
	 */
	private void triggerLobbyRuleDisplayChanged(final Packet p) {
		final int size = p.getInteger("rule-strings.length");
		final String[] s = new String[size];

		for (int i = 0; i < size; i++) {
			s[i] = p.getString("rule-strings[" + i + "]");
		}

		lobby.setSettingsString(s);
	}

	/**
	 * Handles packets which indicate that a remote player is joining the pre-game lobby.
	 * @param p
	 */
	private void triggerLobbyPlayerEnter(final Packet p) {
		final int playerID = p.getInteger("player-id");
		final String playerName = p.getString("player-name");

		final LobbyPlayerInfo lobbyPlayerInfo = new LobbyPlayerInfo(playerID, playerName);

		lobby.addPlayer(playerID, lobbyPlayerInfo);
	}

	/**
	 * Handles packets which indicate that a ghost has been removed
	 * from the world.
	 * 
	 * @param p
	 */
	private void triggerRemoteGhostLeft(final Packet p) {
		final int ghostID = p.getInteger("ghost-id");

		game.getWorld().removeEntity(ghostID);
	}

	/**
	 * Handles packets which indicate that a ghost has been added to
	 * the world.
	 * 
	 * @param p
	 */
	private void triggerRemoteGhostJoined(final Packet p) {
		final int ghostID = p.getInteger("ghost-id");

		final RemoteGhost ghost = new RemoteGhost(ghostID);
		ghost.setPosition(new Position(p.getInteger("row"), p.getInteger("col")));
		game.getWorld().addEntity(ghost);
	}

	/**
	 * Handles packets which indicate that a remote player has been
	 * added to the world.
	 * 
	 * @param p
	 */
	private void triggerRemotePlayerJoined(final Packet p) {
		final int playerID = p.getInteger("player-id");
		final String name = p.getString("name");

		final RemotePlayer player = new RemotePlayer(playerID, name);
		player.setPosition(new Position(p.getInteger("row"), p.getInteger("col")));

		game.getWorld().addEntity(player);
	}

	/**
	 * Handles packets which indicate that a remote player has 
	 * moved within the world.
	 * 
	 * @param p
	 */
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
			// won't happen
		}
	}

	/**
	 * Handles packets which indicate that a ghost has moved
	 * within the world.
	 * 
	 * @param p
	 */
	private void triggerRemoteGhostMoved(final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");
		final int ghostID = p.getInteger("ghost-id");

		final Entity e = game.getWorld().getEntity(ghostID);

		if (e instanceof RemoteGhost) {
			final RemoteGhost ghost = (RemoteGhost) e;
			ghost.setPosition(new Position(row, col));
		} else {
			// won't happen
		}
	}

	/**
	 * Handles the initial handshake packet that is sent from the server to the
	 * client.
	 * 
	 * @param p
	 */
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
	public void onGameCreated(final GameCreatedEventArgs args) {
		if (args.getGame().getGameType() == GameType.MULTIPLAYER_CLIENT) {
			if (game != null) {
				removeWorldGameHooks(game, gameLogic);
			}
			game = args.getGame();
			gameLogic = (RemoteGameLogic) args.getGameLogic();
			addWorldGameHooks(game, gameLogic);
		}
	}
	
	/**
	 * Gets the event which is fired when the game ends on the server.
	 * 
	 * @return
	 */
	public Event<RemoteGameEndedListener, RemoteGameEndedEventArgs> getOnRemoteGameEndedEvent() {
		return onRemoteGameEndedEvent;
	}

	@Override
	public void onLocalPlayerDespawn(LocalPlayerDespawnEventArgs args) {
		this.player.getOnMovedEvent().removeListener(this);
		this.player = null;
	}

	@Override
	public void onLocalPlayerSpawn(LocalPlayerSpawnEventArgs args) {
		this.player = args.getPlayer();
		this.player.getOnMovedEvent().addListener(this);
	}

	@Override
	public void onReadyToStart(ReadyToStartEventArgs args) {
		Packet p = new Packet("ready-to-start");
		manager.dispatch(p);
	}

	@Override
	public void onPlayerLeavingGame() {
		client.die();
	}

	@Override
	public void onPlayerAbilityUsed(PlayerAbilityUsedEventArgs args) {
		Packet p = new Packet("use-ability");
		p.setString("ability-key", String.valueOf(args.getSlot()));
		manager.dispatch(p);
	}
}
