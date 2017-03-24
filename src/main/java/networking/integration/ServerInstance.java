package main.java.networking.integration;

import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.CellStateChangedEventArgs;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameSettingsChangedEventArgs;
import main.java.event.arguments.HostStartingMultiplayerGameEventArgs;
import main.java.event.arguments.LobbyChangedEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.arguments.PlayerLaserActivatedEventArgs;
import main.java.event.arguments.PlayerMovedEventArgs;
import main.java.event.arguments.PlayerShieldActivatedEventArgs;
import main.java.event.arguments.PlayerShieldRemovedEventArgs;
import main.java.event.listener.CellStateChangedEventListener;
import main.java.event.listener.CountDownStartingListener;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityRemovingListener;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.GameSettingsChangedEventListener;
import main.java.event.listener.HostStartingMultiplayerGameListener;
import main.java.event.listener.LobbyStateChangedListener;
import main.java.event.listener.MultiplayerGameStartingListener;
import main.java.event.listener.PlayerCooldownChangedListener;
import main.java.event.listener.PlayerLaserActivatedListener;
import main.java.event.listener.PlayerShieldActivatedListener;
import main.java.event.listener.PlayerShieldRemovedListener;
import main.java.event.listener.ServerEntityUpdatedListener;
import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.core.GameLogicTimer;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.LocalGameLogic;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.Ghost;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.ServerEntityTracker;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.gamelogic.domain.World;
import main.java.networking.ServerManager;
import main.java.networking.StandardServerManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientConnectedListener;
import main.java.networking.event.ClientDisconnectedListener;
import main.java.networking.event.ServerTrigger;
import main.java.networking.socket.Server;

public class ServerInstance implements Runnable, ServerTrigger, ClientConnectedListener, ServerEntityUpdatedListener,
		EntityAddedListener, EntityRemovingListener, ClientDisconnectedListener, LobbyStateChangedListener,
		HostStartingMultiplayerGameListener, GameCreatedListener, CellStateChangedEventListener, GameSettingsChangedEventListener, GameEndedListener, CountDownStartingListener, PlayerCooldownChangedListener, PlayerLaserActivatedListener, PlayerShieldActivatedListener, PlayerShieldRemovedListener {
	private Server server;
	protected ServerManager manager;
	private Game game;
	private ServerEntityTracker tracker;
	protected Lobby lobby;
	private Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> multiplayerGameStartingEvent;
	private GameLogic gameLogic;
	private GameLogicTimer gameLogicTimer;

	/**
	 * Creates a new server instance which, when ran, will connect to the server
	 * with the specified IP address.
	 *
	 * Any objects which will be involved with networking (eg. the map, the
	 * game, etc.) should be passed in to this {@link ServerInstance} object via
	 * the constructor. This, in turn, will pass those objects into the
	 * appropriate triggers (so packets received from the network will update
	 * the local game state accordingly), and also register the current server
	 * instance object as a listener to any local events (eg. player moved)
	 * which must be transmitted over the network.
	 */
	public ServerInstance(final Lobby lobby) {
		this.lobby = lobby;
		game = null;
		gameLogic = null;
		gameLogicTimer = null;
		multiplayerGameStartingEvent = new Event<>((l, a) -> l.onMultiplayerGameStarting(a));
	}

	@Override
	public void run() {
		// Create the server socket object
		server = createServer();

		// Create
		manager = new StandardServerManager(server);
		manager.setTrigger(this);

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
		final Server server = new Server();

		return server;
	}

	/**
	 * Gets the event which is fired when the game which this server is using is
	 * about to start.
	 *
	 * @return
	 */
	public Event<MultiplayerGameStartingListener, MultiplayerGameStartingEventArgs> getMultiplayerGameStartingEvent() {
		return multiplayerGameStartingEvent;
	}

	/**
	 * This method adds the server dispatcher as a listener to any local game
	 * events which must be transmitted over the network. The dispatcher will
	 * listen to those events, transform them into a packet representing the
	 * same info, and then send them to the current server object, which sends
	 * the packets to the server.
	 *
	 * See the comment at the top of the method for a (fictitious) example of
	 * how this would be done for a "local player moved" game event.
	 */
	protected void addGameHooks() {
		/*
		 * Example of how a local player moved event would be handled. Assume
		 * the game world object was passed to the ServerInstance via the
		 * constructor.
		 *
		 * this.gameWorld.getPlayerMovedEvent().addListener(this.dispatcher);
		 *
		 * The dispatcher object would then implement the PlayerMovedListener
		 * interface, such that the onPlayerMoved method transforms any of the
		 * relevant data (eg. X and Y co-ordinates) and turns it into a packet,
		 * which is then dispatched to the server manager, which turns the
		 * packet into bytes and sends it to the server.
		 */
		tracker = new ServerEntityTracker(this);
		lobby.getLobbyStateChangedEvent().addListener(this);

		if (server != null) {
			server.getClientConnectedEvent().addListener(this);
			server.getClientDisconnectedEvent().addListener(this);
		}
	}

	/**
	 * Adds this client instance as a listener to any relevant events in the
	 * currently-used {@link Game} and {@link GameLogic} objects.
	 *
	 * @param game
	 *            The game object that this server instance is to attach to.
	 * @param logic
	 *            The game logic object controlling the game which this server
	 *            instance is to attach to.
	 */
	private void addWorldGameHooks(final World world, final LocalGameLogic logic) {
		world.getOnEntityAddedEvent().addListener(this);
		world.getOnEntityAddedEvent().addListener(tracker);
		world.getOnEntityRemovingEvent().addListener(this);
		world.getOnEntityRemovingEvent().addListener(tracker);

		world.getMap().getOnCellStateChanged().addListener(this);
		logic.getOnGameEnded().addOneTimeListener(this);
	}

	/**
	 * In order to prevent resource leaks and to stop the event listener lists
	 * growing increasingly large as the player joins successive online games,
	 * any of the game hooks registered in {@link this#addGameHooks()} should be
	 * removed in the exact same manner they were registered.
	 *
	 * This is also necessary so that game events are not passed to a server
	 * manager for a connection which has since been terminated.
	 */
	protected void removeGameHooks() {
		/*
		 * Corresponding example for the local player moved event (see above).
		 *
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this.dispatcher);
		 */

		if (game != null) {
			removeWorldGameHooks(game.getWorld(), (LocalGameLogic) gameLogic);
		}
	}

	/**
	 * Removes this {@link ServerInstance} object as a listener from any
	 * relevant events in the currently-used {@link Game} and {@link GameLogic}
	 * objects.
	 *
	 * @param game
	 *            The game object that this server instance is to detach from.
	 * @param logic
	 *            The game logic object controlling the game which this server
	 *            instance is to detach from.
	 */
	private void removeWorldGameHooks(final World world, final LocalGameLogic logic) {
		world.getOnEntityAddedEvent().removeListener(this);
		world.getOnEntityAddedEvent().removeListener(tracker);
		world.getOnEntityRemovingEvent().removeListener(this);
		world.getOnEntityRemovingEvent().removeListener(tracker);

		world.getMap().getOnCellStateChanged().removeListener(this);
	}

	@Override
	public void onClientConnected(final int clientID) {
		final Packet p = new Packet("server-handshake");
		p.setInteger("client-id", clientID);

		manager.dispatch(clientID, p);
	}

	/**
	 * Sends the initial state of the lobby to a newly-joined player, so a
	 * player who is joining an existing lobby receives the joining packets for
	 * all of the existing players.
	 *
	 * @param clientID
	 *            The player to send the initial lobby state to.
	 *
	 */
	public void sendInitialLobbyState(final int clientID) {
		for (final int i : lobby.getPlayerIDs()) {
			manager.dispatch(clientID, createPlayerJoinedLobbyPacket(lobby.getPlayer(i)));
		}
		manager.dispatch(clientID, createRulesChangedPacket(lobby.getSettingsString()));
	}

	/**
	 * Create a packet which indicates that the textual display in the game
	 * lobby showing the game rules has changed.
	 *
	 * @param rules
	 *            The rule strings to send.
	 * @return A packet representing the new rules display.
	 */
	private Packet createRulesChangedPacket(final String[] rules) {
		final Packet p = new Packet("lobby-rule-display-changed");

		p.setInteger("rule-strings.length", rules.length);
		for (int i = 0; i < rules.length; i++) {
			p.setString("rule-strings[" + i + "]", rules[i]);
		}

		return p;
	}

	/**
	 * Create a packet indicating that the player represented by {@code player}
	 * has joined the lobby.
	 *
	 * @param player
	 *            The player info of the player who is joining.
	 * @return A packet representing that the given player has joined a lobby.
	 */
	private Packet createPlayerJoinedLobbyPacket(final LobbyPlayerInfo player) {
		final Packet p = new Packet("lobby-player-enter");
		p.setInteger("player-id", player.getID());
		p.setString("player-name", player.getName());
		return p;
	}

	/**
	 * Create a packet indicating that the player with the given
	 * {@code playerID} has left the lobby.
	 *
	 * @param playerID
	 *            The ID of the player who is leaving the lobby.
	 * @return The sendable packet object representing a player leaving the
	 *         lobby.
	 */
	private Packet createPlayerLeftLobbyPacket(final int playerID) {
		final Packet p = new Packet("lobby-player-left");
		p.setInteger("player-id", playerID);
		return p;
	}

	@Override
	public void onEntityMoved(final EntityMovedEventArgs args) {
		if (args.getEntity() instanceof Player) {
			final Packet p = new Packet("remote-player-moved");
			p.setInteger("row", args.getRow());
			p.setInteger("col", args.getCol());
			if (args instanceof PlayerMovedEventArgs) {
				p.setDouble("angle", ((PlayerMovedEventArgs) args).getAngle());
			}
			p.setInteger("player-id", args.getEntity().getID());

			if (lobby.containsPlayer(args.getEntity().getID())) {
				// if the player ID is connected to the server, then
				// this is an actual player
				// otherwise it's an AI player

				manager.dispatchAllExcept(p, args.getEntity().getID());
			} else {
				manager.dispatchAll(p);
			}
		}
		if (args.getEntity() instanceof Ghost) {
			final Packet p = new Packet("remote-ghost-moved");
			p.setInteger("row", args.getRow());
			p.setInteger("col", args.getCol());
			p.setInteger("ghost-id", args.getEntity().getID());

			manager.dispatchAll(p);
		}
	}

	@Override
	public void trigger(final int sender, final Packet p) {
		if (p.getPacketName().equals("client-handshake")) {
			triggerHandshake(sender, p);
		} else if (p.getPacketName().equals("player-moved")) {
			triggerPlayerMoved(sender, p);
		} else if (p.getPacketName().equals("ready-to-start")) {
			triggerClientReadyToStart(sender, p);
		} else if (p.getPacketName().equals("use-ability")) {
			triggerPlayerUseAbility(sender, p);
		}
	}

	/**
	 * Handes packets indicating that a client is trying to use one of its
	 * abilities.
	 *
	 * @param sender
	 * @param p
	 */
	private void triggerPlayerUseAbility(final int sender, final Packet p) {
		final Player player = (Player) game.getWorld().getEntity(sender);

		if (player != null) {
			final SkillSet skillSet = player.getSkillSet();

			if (skillSet != null) {
				final char abilityKey = p.getString("ability-key").charAt(0);

				switch (abilityKey) {
				case 'q':
					skillSet.activateQ();
					break;
				case 'w':
					skillSet.activateW();
					break;
				default:
					throw new IllegalStateException("Unknown ability key " + abilityKey);
				}
			}
		}
	}

	/**
	 * Handles packets indicating that the client is ready to start. This packet
	 * can mean one of two things:
	 *
	 * <ul>
	 * <li>The player's client has loaded the relevant JavaFX resources and may
	 * now begin the game (ie. finished loading(</li>
	 * <li>The player is trying to respawn after having died</li>
	 * </ul>
	 *
	 * The intended meaning is deduced based on whether the game has already
	 * started or not, and if so, whether the player is already in game or not.
	 *
	 * @param sender
	 * @param p
	 */
	private void triggerClientReadyToStart(final int sender, final Packet p) {
		final LobbyPlayerInfo playerInfo = lobby.getPlayer(sender);
		if (!game.hasStarted()) {
			if (!playerInfo.isReady()) {
				playerInfo.setReady(true);

				if (lobby.allReady()) {
					startGame();
				}
			}
		} else {
			if (game.getWorld().getEntity(sender) == null) {
				if (playerInfo.getRemainingLives() > 0) {
					final Position pos = ((LocalGameLogic) gameLogic).getCandidateSpawnPosition();
					playerInfo.setRemainingLives(playerInfo.getRemainingLives() - 1);
					addHumanPlayerToWorld(sender, pos.getRow(), pos.getColumn());
				}
			}
		}
	}

	/**
	 * Handles packets indicating that a client's player is attempting to move
	 * in the world (ie. to another position on the map).
	 *
	 * @param sender
	 * @param p
	 */
	private void triggerPlayerMoved(final int sender, final Packet p) {
		final int row = p.getInteger("row"), col = p.getInteger("col");

		final Entity e = game.getWorld().getEntity(sender);

		if (e != null && e instanceof Player) {
			final Player player = (Player) e;
			if (p.hasParameter("angle")) {
				player.setAngle(p.getDouble("angle"));
			}
			player.setPosition(new Position(row, col));
		} else {
			// ignore
		}
	}

	/**
	 * Handles the packet sent by the client to the server specifying attributes
	 * of the player.
	 *
	 * @param sender
	 * @param p
	 */
	private void triggerHandshake(final int sender, final Packet p) {
		final String username = p.getString("username");

		lobby.addPlayer(sender, new LobbyPlayerInfo(sender, username));
		sendInitialLobbyState(sender);
	}

	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof Player) {
			final Packet p = new Packet("remote-player-joined");
			p.setInteger("player-id", e.getID());
			p.setString("name", ((Player) e).getName());
			p.setInteger("row", e.getPosition().getRow());
			p.setInteger("col", e.getPosition().getColumn());
			manager.dispatchAllExcept(p, e.getID());

            ((Player)e).getSkillSet().getOnPlayerLaserActivated().addListener(this);
            ((Player)e).getSkillSet().getOnPlayerShieldActivated().addListener(this);
            ((Player)e).getSkillSet().getOnPlayerShieldRemoved().addListener(this);
            
			if (lobby.containsPlayer(e.getID())) {
				((Player)e).getSkillSet().getOnPlayerCooldownChanged().addListener(this);
				manager.dispatch(e.getID(), createLocalPlayerJoinPacket(e.getPosition().getRow(),
						e.getPosition().getColumn(), ((Player) e).getAngle()));
			}
		}
		if (e instanceof Ghost) {
			final Packet p = new Packet("remote-ghost-joined");
			p.setInteger("ghost-id", e.getID());
			p.setInteger("row", e.getPosition().getRow());
			p.setInteger("col", e.getPosition().getColumn());
			manager.dispatchAll(p);
		}
		if (e instanceof Spawner) {
			final Spawner s = (Spawner) e;
			final Entity toSpawn = s.getEntity();

			if (toSpawn instanceof Ghost) {
				manager.dispatchAll(createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "ghost"));
			} else if (toSpawn instanceof Player) {
				manager.dispatchAllExcept(
						createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "remote-player"),
						toSpawn.getID());

				if (lobby.containsPlayer(toSpawn.getID())) {
					manager.dispatch(toSpawn.getID(),
							createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "local-player"));
				}
			}
		}
	}

	/**
	 * Creates a packet indicating that a spawner countdown has been added to
	 * the world at a given position with a specified starting number.
	 *
	 * @param position
	 *            The position that the new countdown will appear at.
	 * @param time
	 *            The starting time number of the countdown.
	 * @param type
	 *            The entity type which the countdown is going to spawn.
	 * @return A packet representing the appearance of a countdown number in the
	 *         world.
	 */
	private Packet createSpawnCountdownPacket(final Position position, final int time, final String type) {
		final Packet p = new Packet("spawner-added");
		p.setInteger("row", position.getRow());
		p.setInteger("col", position.getColumn());
		p.setInteger("duration", time);
		p.setString("entity-type", type);

		return p;
	}

	@Override
	public void onEntityRemoving(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());

		if (e instanceof Player) {
			final Packet p = new Packet("remote-player-died");
			p.setInteger("player-id", e.getID());
			manager.dispatchAllExcept(p, e.getID());

			if (e instanceof RemotePlayer && lobby.containsPlayer(e.getID())) {
				handleRemovingHumanPlayerFromWorld(e.getID(), ((RemotePlayer) e).getDeathReason());
			}

			((Player)e).getSkillSet().getOnPlayerCooldownChanged().removeListener(this);
            ((Player)e).getSkillSet().getOnPlayerLaserActivated().removeListener(this);
            ((Player)e).getSkillSet().getOnPlayerShieldActivated().removeListener(this);
            ((Player)e).getSkillSet().getOnPlayerShieldRemoved().removeListener(this);
		}
		if (e instanceof Ghost) {
			final Packet p = new Packet("remote-ghost-died");
			p.setInteger("ghost-id", e.getID());
			manager.dispatchAll(p);
		}
	}

	/**
	 * Handles adding a (human) player to the world, including adding the
	 * appropriate skill set info and updating the lobby.
	 *
	 * @param playerID
	 *            The player ID to add to the world.
	 * @param row
	 *            The row of the position at which to add the player.
	 * @param col
	 *            the column of the position at which to add the player.
	 */
	public Player addHumanPlayerToWorld(final int playerID, final int row, final int col) {
		if (lobby.containsPlayer(playerID)) {
			final LobbyPlayerInfo info = lobby.getPlayer(playerID);

			if (info.isInGame()) {
				throw new IllegalStateException("Player " + playerID + " already in-game.");
			} else {
				final RemotePlayer player = new RemotePlayer(info.getID(), info.getName());
				player.setSkillSet(LocalSkillSet.createDefaultSkillSet(player));
				player.setPosition(new Position(row, col));
				final Spawner spawner = new Spawner(5, player, SpawnerColor.GREEN);
				spawner.setPosition(player.getPosition());
				game.getWorld().addEntity(spawner);
				info.setInGame(true);

				return player;
			}
		} else {
			throw new IllegalStateException("Human player with ID " + playerID + " does not exist.");
		}
	}

	/**
	 * Handles removing a human player from the world, including setting the
	 * player's death reason to an appropriate value.
	 *
	 * @param playerID
	 *            The ID of the human player to remove.
	 * @param reason
	 *            The reason for why the player is being removed (this will be
	 *            displayed on the client's screen).
	 */
	public void handleRemovingHumanPlayerFromWorld(final int playerID, String reason) {
		final LobbyPlayerInfo playerInfo = lobby.getPlayer(playerID);
		playerInfo.setInGame(false);
		if (playerInfo.getRemainingLives() > 0) {
			reason = reason + "\nLives remaining: " + playerInfo.getRemainingLives();
		} else {
			reason = reason + "\nYou have no more lives!";
		}
		manager.dispatch(playerID, createLocalPlayerDiedPacket(reason, playerInfo.getRemainingLives() > 0));
	}

	/**
	 * Create a packet indicating that a local client's player has appeared at a
	 * given position in the world.
	 *
	 * @param row
	 *            The row of the position which the player appears at.
	 * @param col
	 *            The column of the position which the player appears at.
	 * @param angle
	 *            The angle which the player start off pointing to.
	 * @return
	 */
	public Packet createLocalPlayerJoinPacket(final int row, final int col, final double angle) {
		final Packet packet = new Packet("local-player-joined");
		packet.setInteger("row", row);
		packet.setInteger("col", col);
		packet.setDouble("angle", angle);
		return packet;
	}

	/**
	 * Create a packet indicating that a local client's player is being removed
	 * from the world, with a given reason.
	 *
	 * @param message
	 *            The message sent to the client describing why they died
	 * @param rejoinable
	 *            Whether the player who died is given the chance to respawn or
	 *            not.
	 * @return
	 */
	public Packet createLocalPlayerDiedPacket(final String message, final boolean rejoinable) {
		final Packet packet = new Packet("local-player-died");
		packet.setString("message", message);
		packet.setBoolean("rejoinable", rejoinable);
		return packet;
	}

	@Override
	public void onClientDisconnected(final int clientID) {
		lobby.removePlayer(clientID);
		if (game != null && game.getWorld().getEntity(clientID) != null) {
			game.getWorld().removeEntity(clientID);
		}

		if (lobby.getPlayerCount() == 0 || clientID == 0) {
			server.die();
			if (gameLogicTimer != null) {
				gameLogicTimer.stop();
			}
		}
	}

	@Override
	public void onLobbyStateChanged(final LobbyChangedEventArgs args) {
		if (args instanceof LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) {
			final int id = ((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID();
			manager.dispatchAllExcept(
					createPlayerLeftLobbyPacket(((LobbyChangedEventArgs.LobbyPlayerLeftEventArgs) args).getPlayerID()),
					id);
		} else if (args instanceof LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) {
			final int id = ((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerID();
			manager.dispatchAllExcept(createPlayerJoinedLobbyPacket(
					((LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs) args).getPlayerInfo()), id);
		} else if (args instanceof LobbyChangedEventArgs.LobbyRulesChangedEventArgs) {
			manager.dispatchAll(
					createRulesChangedPacket(((LobbyChangedEventArgs.LobbyRulesChangedEventArgs) args).getNewRules()));
		}
	}

	@Override
	public void onHostStartingGame(final HostStartingMultiplayerGameEventArgs args) {
		getMultiplayerGameStartingEvent().fire(new MultiplayerGameStartingEventArgs(args.getSettings()));
	}

	@Override
	public void onGameCreated(final GameCreatedEventArgs args) {
		if (args.getGame().getGameType() == GameType.MULTIPLAYER_SERVER) {
			if (game != null) {
				// cleanup hooks to old game

				removeWorldGameHooks(game.getWorld(), (LocalGameLogic) gameLogic);
				gameLogicTimer.stop();
			}
			game = args.getGame();
			gameLogic = args.getGameLogic();

			final Packet p = new Packet("game-starting");

			p.setInteger("initial-player-lives", game.getGameSettings().getInitialPlayerLives());
			// add game configuration stuff into this packet

			manager.dispatchAll(p);

			addWorldGameHooks(game.getWorld(), (LocalGameLogic) gameLogic);
		}
	}

	/**
	 * Start the game, adding all of the player in the lobby to the world in the
	 * process. This also starts a timer to tick the game logic. The server has
	 * no render loop, so a timer must be used manually to tick the game logic
	 * along.
	 */
	private void startGame() {
		game.setStarted();
		lobby.resetReady();

		for (final int i : lobby.getPlayerIDs()) {
			lobby.getPlayer(i).setRemainingLives(game.getGameSettings().getInitialPlayerLives());
		}

		for (final int i : lobby.getPlayerIDs()) {
			final Position p = ((LocalGameLogic) gameLogic).getCandidateSpawnPosition();
			addHumanPlayerToWorld(i, p.getRow(), p.getColumn());
		}

		gameLogicTimer = new GameLogicTimer(gameLogic);
		gameLogicTimer.start(GameLogic.GAME_STEP_DURATION);
	}

	@Override
	public void onCellStateChanged(final CellStateChangedEventArgs args) {
		final Packet p = new Packet("cell-changed");
		final Position cellPosition = args.getChangeCell().getPosition();
		p.setInteger("row", cellPosition.getRow());
		p.setInteger("col", cellPosition.getColumn());
		p.setString("new-state", args.getState().name());
		manager.dispatchAll(p);
	}

	@Override
	public void onGameEnded(final GameEndedEventArgs args) {
		sendGameEndedPacket(args.getOutcome());
	}

	/**
	 * Creates and sends a packet to all connected players indicating that the
	 * game has ended, along with information on the outcome of the game.
	 *
	 * @param outcome
	 *            The outcome of the game.
	 */
	private void sendGameEndedPacket(final GameOutcome outcome) {
		final Packet p = new Packet("game-ended");
		final GameOutcomeType o = outcome.getOutcomeType();

		switch (o) {
		case GHOSTS_WON:
			p.setString("outcome", "ghosts-won");
			break;
		case PLAYER_WON:
			p.setString("outcome", "player-won");
			p.setInteger("winner-id", outcome.getWinner().getID());
			break;
		case TIE:
			p.setString("outcome", "tie");
			break;
		default:
			throw new IllegalStateException("Unhandled game outcome: " + o.name());
		}

		manager.dispatchAll(p);
	}

	@Override
	public void onCountDownStarted() {
		final Packet p = new Packet("count-down-started");
		manager.dispatchAll(p);
	}

	@Override
	public void onGameSettingsChanged(GameSettingsChangedEventArgs args) {
		lobby.setSettingsString(args.getSettings().toDisplayString());	
	}
	
	public void onPlayerCooldownChanged(PlayerCooldownChangedEventArgs args) {
		if(lobby.containsPlayer(args.getPlayer().getID())) {
			Packet p = new Packet("player-cooldown-changed");

			p.setString("slot", String.valueOf(args.getSlot()));
			p.setInteger("cooldown-level", args.getCooldownLevel());

			manager.dispatch(args.getPlayer().getID(), p);
		}
	}
    @Override
    public void onPlayerLaserActivated(PlayerLaserActivatedEventArgs args) {
        Packet p = new Packet("player-laser-activated");

        p.setInteger("player-id", args.getPlayer().getID());
        p.setDouble("direction", args.getDirection());
        p.setInteger("cool-down", args.getCoolDown());

        manager.dispatchAll(p);
    }

    @Override
    public void onPlayerShieldActivated(PlayerShieldActivatedEventArgs args) {

        Packet p = new Packet("player-shield-activated");
        p.setInteger("player-id", args.getPlayer().getID());
        p.setInteger("shield-value", args.getShieldValue());


        manager.dispatchAll(p);
    }


    @Override
    public void onPlayerShieldRemoved(PlayerShieldRemovedEventArgs args) {
        Packet p = new Packet("player-shield-removed");
        p.setInteger("player-id", args.getPlayer().getID());
        p.setInteger("shield-value", args.getShieldValue());

        manager.dispatchAll(p);
    }

	/*
	 * For posterity: a corresponding removeTriggers method is not necessary.
	 * Once the Server object's connection dies, the server manager will no
	 * longer receive packets and so cleaning up its triggers is not necessary.
	 */
}
