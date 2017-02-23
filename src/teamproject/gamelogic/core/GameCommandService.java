package teamproject.gamelogic.core;

import java.util.HashMap;

import teamproject.ai.AIGhost;
import teamproject.ai.GhostBehaviour;
import teamproject.constants.EntityType;
import teamproject.event.Event;
import teamproject.event.arguments.GameStartingEventArgs;
import teamproject.event.arguments.MultiplayerGameStartingEventArgs;
import teamproject.event.listener.GameStartedListener;
import teamproject.event.listener.RemoteGameStartingListener;
import teamproject.event.listener.StartingSingleplayerGameListener;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.ControlledPlayer;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.World;

public class GameCommandService
		implements StartingSingleplayerGameListener, RemoteGameStartingListener {
	
	private Event<GameStartedListener, Game> gameStartedEvent = new Event<>((l, g) -> l.onGameStarted(g));
	
	public Event<GameStartedListener, Game> getGameStartedEvent() {
		return gameStartedEvent;
	}

	private Game generateNewSinglePlayerGame(final String userName, final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = new Map().generateMap();

		// Create new game and store it
		final World world = new World(new RuleEnforcer(), map);
		final ControlledPlayer player = new ControlledPlayer(0, userName);
		player.setPosition(new Position(6, 0));
		
		final Game game = new Game(world, settings, player);

		
		// Collect players
		// Just the one for now
		
		final AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(1, 1));
		ghost.setType(EntityType.GHOST);
		Behaviour b = new GhostBehaviour(map, ghost, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost.setBehaviour(b);
		
		final AIGhost ghost1 = new AIGhost();
		ghost1.setPosition(new Position(1, 13));
		ghost1.setType(EntityType.GHOST);
		Behaviour b1 = new GhostBehaviour(map, ghost1, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost1.setBehaviour(b1);
		
		final AIGhost ghost2 = new AIGhost();
		ghost2.setPosition(new Position(13, 13));
		ghost2.setType(EntityType.GHOST);
		Behaviour b2 = new GhostBehaviour(map, ghost2, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost2.setBehaviour(b2);
		
		
		world.addEntity(ghost);
		world.addEntity(ghost1);
		world.addEntity(ghost2);
		world.addEntity(player);

		return game;
	}
	
	private Game generateNewMultiplayerGame(final String userName, final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = new Map().generateMap();

		// Create new game and store it
		final World world = new World(new RuleEnforcer(), map);
		final ControlledPlayer player = new ControlledPlayer(0, userName);
		player.setPosition(new Position(0, 0));
		
		final Game game = new Game(world, settings, player);

		
		// Collect players
		// Just the one for now
		final AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(1, 1));
		ghost.setType(EntityType.GHOST);
		Behaviour b = new GhostBehaviour(map, ghost, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost.setBehaviour(b);
		
		final AIGhost ghost1 = new AIGhost();
		ghost1.setPosition(new Position(1, 13));
		ghost1.setType(EntityType.GHOST);
		Behaviour b1 = new GhostBehaviour(map, ghost1, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost1.setBehaviour(b1);
		
		final AIGhost ghost2 = new AIGhost();
		ghost2.setPosition(new Position(13, 13));
		ghost2.setType(EntityType.GHOST);
		Behaviour b2 = new GhostBehaviour(map, ghost2, 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST);
		ghost2.setBehaviour(b2);
		
		
		world.addEntity(ghost);
		world.addEntity(ghost1);
		world.addEntity(ghost2);
		world.addEntity(player);

		return game;
	}

	@Override
	public void onStartingGame(GameStartingEventArgs args) {
		Game g = generateNewSinglePlayerGame(args.getUsername(), args.getSettings());
		getGameStartedEvent().fire(g);
	}

	@Override
	public void onRemoteGameStarting(MultiplayerGameStartingEventArgs args) {
		Game g = generateNewMultiplayerGame(args.getUsername(), args.getSettings());
		getGameStartedEvent().fire(g);
	}
}
