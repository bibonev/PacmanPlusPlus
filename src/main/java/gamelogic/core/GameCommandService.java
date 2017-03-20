package main.java.gamelogic.core;

import main.java.ai.AIGhost;
import main.java.ai.GhostBehaviour;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.ReadyToStartEventArgs;
import main.java.event.arguments.SingleplayerGameStartingEventArgs;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.MultiplayerGameStartingListener;
import main.java.event.listener.ReadyToStartListener;
import main.java.event.listener.SingleplayerGameStartingListener;
import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.World;

public class GameCommandService implements SingleplayerGameStartingListener, MultiplayerGameStartingListener {

	private Event<GameCreatedListener, GameCreatedEventArgs> remoteGameCreatedEvent = new Event<>(
			(l, g) -> l.onGameCreated(g));
	private Event<GameCreatedListener, GameCreatedEventArgs> localGameCreatedEvent = new Event<>(
			(l, g) -> l.onGameCreated(g));

	public Event<GameCreatedListener, GameCreatedEventArgs> getLocalGameCreatedEvent() {
		return localGameCreatedEvent;
	}
	
	public Event<GameCreatedListener, GameCreatedEventArgs> getRemoteGameCreatedEvent() {
		return remoteGameCreatedEvent;
	}

	private void populateWorld(final World world) {
		final AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(1, 1));
		final Behaviour b = new GhostBehaviour(world, ghost, 1000, Behaviour.Type.GHOST);
		ghost.setBehaviour(b);

		final AIGhost ghost1 = new AIGhost();
		ghost1.setPosition(new Position(1, 13));
		final Behaviour b1 = new GhostBehaviour(world, ghost1, 1000, Behaviour.Type.GHOST);
		ghost1.setBehaviour(b1);

		final AIGhost ghost2 = new AIGhost();
		ghost2.setPosition(new Position(13, 13));
		final Behaviour b2 = new GhostBehaviour(world, ghost2, 1000, Behaviour.Type.GHOST);
		ghost2.setBehaviour(b2);

		world.addEntity(ghost);
		world.addEntity(ghost1);
		world.addEntity(ghost2);
	}

	private Game generateNewClientsideGame(final String localUsername, final int localPlayerID,
			final GameSettings settings, final boolean multiplayer) {
		// Generate a map
		// Simplest one for now
		final Map map = Map.generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map, multiplayer);

		final Game game = new Game(world, settings, 
				multiplayer ? GameType.MULTIPLAYER_CLIENT : GameType.SINGLEPLAYER);
		

		return game;
	}

	private Game generateNewServersideGame(final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = Map.generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map, false);

		final Game game = new Game(world, settings, GameType.MULTIPLAYER_SERVER);

		return game;
	}

	@Override
	public void onSingleplayerGameStarting(final SingleplayerGameStartingEventArgs args) {
		final Game g = generateNewClientsideGame(args.getUsername(), 0, args.getSettings(), false);
		final GameLogic gl = new LocalGameLogic(g);
		
		getLocalGameCreatedEvent().fire(new GameCreatedEventArgs(g, gl));
		populateWorld(g.getWorld());
	}

	@Override
	public void onMultiplayerGameStarting(final MultiplayerGameStartingEventArgs args) {
		Game g;
		if (args.isServer()) {
			g = generateNewServersideGame(args.getSettings());
			final GameLogic gl = new LocalGameLogic(g);
			getLocalGameCreatedEvent().fire(new GameCreatedEventArgs(g, gl));
			populateWorld(g.getWorld());
		} else {
			g = generateNewClientsideGame(args.getLocalUsername(), args.getLocalPlayerID(), args.getSettings(), true);
			final GameLogic gl = new RemoteGameLogic(g);
			getRemoteGameCreatedEvent().fire(new GameCreatedEventArgs(g, gl));
		}
	}
}
