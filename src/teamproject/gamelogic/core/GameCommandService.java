package teamproject.gamelogic.core;

import java.util.HashMap;

import teamproject.ai.AIGhost;
import teamproject.ai.GhostBehaviour;
import teamproject.constants.EntityType;
import teamproject.event.Event;
import teamproject.event.arguments.SingleplayerGameStartingEventArgs;
import teamproject.event.arguments.MultiplayerGameStartingEventArgs;
import teamproject.event.listener.GameStartedListener;
import teamproject.event.listener.MultiplayerGameStartingListener;
import teamproject.event.listener.SingleplayerGameStartingListener;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.ControlledPlayer;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.constants.GameType;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RuleChecker;
import teamproject.gamelogic.domain.World;

public class GameCommandService
		implements SingleplayerGameStartingListener, MultiplayerGameStartingListener {
	
	private Event<GameStartedListener, Game> gameStartedEvent = new Event<>((l, g) -> l.onGameStarted(g));
	
	public Event<GameStartedListener, Game> getGameStartedEvent() {
		return gameStartedEvent;
	}
	
	private void populateWorld(World world) {
		final AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(1, 1));
		ghost.setType(EntityType.GHOST);
		Behaviour b = new GhostBehaviour(world, ghost, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost.setBehaviour(b);

		final AIGhost ghost1 = new AIGhost();
		ghost1.setPosition(new Position(1, 13));
		ghost1.setType(EntityType.GHOST);
		Behaviour b1 = new GhostBehaviour(world, ghost1, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost1.setBehaviour(b1);

		final AIGhost ghost2 = new AIGhost();
		ghost2.setPosition(new Position(13, 13));
		ghost2.setType(EntityType.GHOST);
		Behaviour b2 = new GhostBehaviour(world, ghost2, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost2.setBehaviour(b2);

		world.addEntity(ghost);
		world.addEntity(ghost1);
		world.addEntity(ghost2);
	}

	private Game generateNewClientsideGame(final String localUsername, final int localPlayerID, final GameSettings settings, final boolean multiplayer) {
		// Generate a map
		// Simplest one for now
		final Map map = Map.generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map, multiplayer);
		final ControlledPlayer player = new ControlledPlayer(localPlayerID, localUsername);
		player.setPosition(new Position(6, 0));
		
		final Game game = new Game(world, settings, player, multiplayer ? GameType.MULTIPLAYER_CLIENT : GameType.SINGLEPLAYER);

		
		// Collect players
		// Just the one for now
		
		world.addEntity(player);

		return game;
	}
	
	private Game generateNewServersideGame(final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = Map.generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map, false);
		
		final Game game = new Game(world, settings, null, GameType.MULTIPLAYER_SERVER);

		return game;
	}

	@Override
	public void onSingleplayerGameStarting(SingleplayerGameStartingEventArgs args) {
		Game g = generateNewClientsideGame(args.getUsername(), 0, args.getSettings(), false);
		getGameStartedEvent().fire(g);
		populateWorld(g.getWorld());
	}

	@Override
	public void onMultiplayerGameStarting(MultiplayerGameStartingEventArgs args) {
		Game g;
		if(args.isServer()) {
			g = generateNewServersideGame(args.getSettings());
			getGameStartedEvent().fire(g);
			populateWorld(g.getWorld());
		} else {
			g = generateNewClientsideGame(args.getLocalUsername(), args.getLocalPlayerID(), args.getSettings(), true);
			getGameStartedEvent().fire(g);
		}
	}
}
