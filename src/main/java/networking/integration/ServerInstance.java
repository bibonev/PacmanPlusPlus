package main.java.networking.integration;

import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.CellStateChangedEventArgs;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.arguments.EntityMovedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.GameSettingsChangedEventArgs;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.arguments.HostStartingMultiplayerGameEventArgs;
import main.java.event.arguments.LobbyChangedEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.PlayerMovedEventArgs;
import main.java.event.listener.CellStateChangedEventListener;
import main.java.event.listener.CountDownStartingListener;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityRemovingListener;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.GameSettingsChangedEventListener;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.HostStartingMultiplayerGameListener;
import main.java.event.listener.LobbyStateChangedListener;
import main.java.event.listener.MultiplayerGameStartingListener;
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
import main.java.gamelogic.domain.World;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.networking.ServerManager;
import main.java.networking.StandardServerManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientConnectedListener;
import main.java.networking.event.ClientDisconnectedListener;
import main.java.networking.event.ServerTrigger;
import main.java.networking.socket.Server;
import main.java.ui.GameUI;

public class ServerInstance implements Runnable, ServerTrigger, ClientConnectedListener, ServerEntityUpdatedListener,
		EntityAddedListener, EntityRemovingListener, ClientDisconnectedListener, LobbyStateChangedListener,
		HostStartingMultiplayerGameListener, GameCreatedListener, CellStateChangedEventListener, GameEndedListener,
		CountDownStartingListener, GameSettingsChangedEventListener {
	private Server server;
	private ServerManager manager;
	private Game game;
	private GameUI gameUI;
	private ServerEntityTracker tracker;
	private Lobby lobby;
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
	public ServerInstance(final GameUI gameUI, final Lobby lobby) {
		this.lobby = lobby;
		game = null;
		gameLogic = null;
		gameLogicTimer = null;
		this.gameUI = gameUI;
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
	private void addGameHooks() {
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
		server.getClientConnectedEvent().addListener(this);
		server.getClientDisconnectedEvent().addListener(this);
		lobby.getLobbyStateChangedEvent().addListener(this);
	}

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
	private void removeGameHooks() {
		/*
		 * Corresponding example for the local player moved event (see above).
		 *
		 * this.gameWorld.getPlayerMovedEvent().removeListener(this.dispatcher);
		 */

		if (game != null) {
			removeWorldGameHooks(game.getWorld(), (LocalGameLogic) gameLogic);
		}
	}

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

	private void sendInitialLobbyState(final int clientID) {
		for (final int i : lobby.getPlayerIDs()) {
			manager.dispatch(clientID, createPlayerJoinedLobbyPacket(lobby.getPlayer(i)));
		}
		manager.dispatch(clientID, createRulesChangedPacket(lobby.getSettingsString()));
	}

	private Packet createRulesChangedPacket(final String[] rules) {
		final Packet p = new Packet("lobby-rule-display-changed");

		p.setInteger("rule-strings.length", rules.length);
		for (int i = 0; i < rules.length; i++) {
			p.setString("rule-strings[" + i + "]", rules[i]);
		}

		return p;
	}

	private Packet createPlayerJoinedLobbyPacket(final LobbyPlayerInfo player) {
		final Packet p = new Packet("lobby-player-enter");
		p.setInteger("player-id", player.getID());
		p.setString("player-name", player.getName());
		return p;
	}

	private Packet createPlayerLeftLobbyPacket(final int playerID) {
		final Packet p = new Packet("lobby-player-left");
		p.setInteger("player-id", playerID);
		return p;
	}

	private Packet createForceMovePacket(final int row, final int column, final double angle) {
		final Packet p = new Packet("force-move");
		p.setInteger("row", row);
		p.setInteger("col", column);
		p.setDouble("angle", angle);
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

			if (server.getConnectedClients().contains(args.getEntity().getID())) {
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
		} else if(p.getPacketName().equals("ready-to-start")) {
			triggerClientReadyToStart(sender, p);
		} else if(p.getPacketName().equals("use-ability")) {
			triggerPlayerUseAbility(sender, p);
		}
	}

	private void triggerPlayerUseAbility(int sender, Packet p) {
		Player player = (Player)game.getWorld().getEntity(sender);
		
		if(player != null) {		
			SkillSet skillSet = player.getSkillSet();
			
			if(skillSet != null) {
				char abilityKey = p.getString("ability-key").charAt(0);
				
				switch(abilityKey) {
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

	private void triggerClientReadyToStart(int sender, Packet p) {
		LobbyPlayerInfo playerInfo = lobby.getPlayer(sender);
		if(!game.hasStarted()) {
			if(!playerInfo.isReady()) {
				playerInfo.setReady(true);
				
				if(lobby.allReady()) {
					startGame();
				}
			}
		} else {
			if(game.getWorld().getEntity(sender) == null) {
				if(playerInfo.getRemainingLives() > 0) {
					Position pos = ((LocalGameLogic) gameLogic).getCandidateSpawnPosition();
					playerInfo.setRemainingLives(playerInfo.getRemainingLives() - 1);
					addHumanPlayerToWorld(sender, pos.getRow(), pos.getColumn());
				}
			}
		}
	}

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
			
			if(lobby.containsPlayer(e.getID())) {
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
		if(e instanceof Spawner) {
			Spawner s = (Spawner) e;
			Entity toSpawn = s.getEntity();
			
			if(toSpawn instanceof Ghost) {
				manager.dispatchAll(createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "ghost"));
			} else if(toSpawn instanceof Player) {
				manager.dispatchAllExcept(
						createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "remote-player"),
						toSpawn.getID());
				
				if(lobby.containsPlayer(toSpawn.getID())) {
					manager.dispatch(
							toSpawn.getID(),
							createSpawnCountdownPacket(e.getPosition(), s.getTimeRemaining(), "local-player"));
				}
			}
		}
	}
	
	private Packet createSpawnCountdownPacket(Position position, int time, String type) {
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
			
			if(e instanceof RemotePlayer && lobby.containsPlayer(e.getID())) {
				handleRemovingHumanPlayerFromWorld(e.getID(), ((RemotePlayer) e).getDeathReason());
			}
		}
		if (e instanceof Ghost) {
			final Packet p = new Packet("remote-ghost-died");
			p.setInteger("ghost-id", e.getID());
			manager.dispatchAll(p);
		}
	}
	
	public void addHumanPlayerToWorld(int playerID, int row, int col) {
		if(lobby.containsPlayer(playerID)) {
			LobbyPlayerInfo info = lobby.getPlayer(playerID);
			
			if(info.isInGame()) {
				throw new IllegalStateException("Player " + playerID + " already in-game.");
			} else {
				final RemotePlayer player = new RemotePlayer(info.getID(), info.getName());
				player.setSkillSet(LocalSkillSet.createDefaultSkillSet(player));
				player.setPosition(new Position(row, col));
				final Spawner spawner = new Spawner(5, player, SpawnerColor.GREEN);
				spawner.setPosition(player.getPosition());
				game.getWorld().addEntity(spawner);
				info.setInGame(true);
			}
		}
	}
	
	public void handleRemovingHumanPlayerFromWorld(int playerID, String reason) {
		LobbyPlayerInfo playerInfo = lobby.getPlayer(playerID);
		playerInfo.setInGame(false);
		if(playerInfo.getRemainingLives() > 0) {
			reason = reason + "\nLives remaining: " + playerInfo.getRemainingLives();
		} else {
			reason = reason + "\nYou have no more lives!";
		}
		manager.dispatch(playerID, createLocalPlayerDiedPacket(reason, playerInfo.getRemainingLives() > 0));
	}
	
	public Packet createLocalPlayerJoinPacket(int row, int col, double angle) {
		Packet packet = new Packet("local-player-joined");
		packet.setInteger("row", row);
		packet.setInteger("col", col);
		packet.setDouble("angle", angle);
		return packet;
	}
	
	public Packet createLocalPlayerDiedPacket(String message, boolean rejoinable) {
		Packet packet = new Packet("local-player-died");
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
		
		final Packet p = new Packet("remote-player-left");
		p.setInteger("player-id", clientID);
		gameUI.multiPlayerLobbyScreen.list.removePlayer(clientID);
		manager.dispatchAllExcept(p, clientID);
		
		if(lobby.getPlayerCount() == 0 || clientID == 0) {
			server.die();
			if(gameLogicTimer != null)
				gameLogicTimer.stop();
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


	private void startGame() {
		game.setStarted();
		lobby.resetReady();
		
		for(final int i : lobby.getPlayerIDs()) {
			lobby.getPlayer(i).setRemainingLives(game.getGameSettings().getInitialPlayerLives());
		}
		
		for (final int i : lobby.getPlayerIDs()) {
			Position p = ((LocalGameLogic) gameLogic).getCandidateSpawnPosition();
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
		triggerGameEnded(args.getOutcome());
	}

	private void triggerGameEnded(final GameOutcome outcome) {
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
		final Packet p = new Packet("game-settings-changed");
		p.setInteger("lives", args.getSettings().getInitialPlayerLives());
		p.setBoolean("playAgainstAI", args.getSettings().getAIPlayer());
		manager.dispatchAll(p);		
	}

	/*
	 * For posterity: a corresponding removeTriggers method is not necessary.
	 * Once the Server object's connection dies, the server manager will no
	 * longer receive packets and so cleaning up its triggers is not necessary.
	 */
}
