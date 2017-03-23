package main.java.gamelogic.core;

import main.java.ai.AIGhost;
import main.java.ai.AIPlayer;
import main.java.ai.DefaultBehaviour;
import main.java.ai.GhostBehaviour;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.event.arguments.SingleplayerGameStartingEventArgs;
import main.java.event.listener.GameCreatedListener;
import main.java.event.listener.MultiplayerGameStartingListener;
import main.java.event.listener.SingleplayerGameStartingListener;
import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
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

	// might want to keep the entity count down so the game runs more smoothly
	private void populateWorld(final World world, boolean playAgainstAI) {
		Position[] ghostPositions = new Position[] {
				new Position(1, 1),
				//new Position(1, 13),
				//new Position(13, 13)
		};
		
		for(Position p : ghostPositions) {
			final AIGhost ghost = new AIGhost();
			ghost.setPosition(p);
			final Behaviour b = new GhostBehaviour(world, ghost, Behaviour.Type.GHOST);
			ghost.setBehaviour(b);
			
			Spawner spawner = new Spawner(5, ghost, SpawnerColor.RED);
			spawner.setPosition(ghost.getPosition());
			
			world.addEntity(spawner);
		}

		final AIPlayer aiPlayer = new AIPlayer();
		aiPlayer.setPosition(new Position(7, 7));
		final Behaviour aiPlayerBehavior = new DefaultBehaviour(world, aiPlayer, Behaviour.Type.DEFAULT);
		aiPlayer.setBehaviour(aiPlayerBehavior);
		aiPlayer.setSkillSet(LocalSkillSet.createDefaultSkillSet(aiPlayer));
		Spawner spawner = new Spawner(5, aiPlayer, SpawnerColor.RED);
		spawner.setPosition(aiPlayer.getPosition());
		if(playAgainstAI){
			world.addEntity(spawner);
		}
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
		populateWorld(g.getWorld(), g.getGameSettings().getAIPlayer());
	}

	@Override
	public void onMultiplayerGameStarting(final MultiplayerGameStartingEventArgs args) {
		Game g;
		if (args.isServer()) {
			g = generateNewServersideGame(args.getSettings());
			final GameLogic gl = new LocalGameLogic(g);
			getLocalGameCreatedEvent().fire(new GameCreatedEventArgs(g, gl));
			populateWorld(g.getWorld(), g.getGameSettings().getAIPlayer());
		} else {
			g = generateNewClientsideGame(args.getLocalUsername(), args.getLocalPlayerID(), args.getSettings(), true);
			final GameLogic gl = new RemoteGameLogic(g);
			getRemoteGameCreatedEvent().fire(new GameCreatedEventArgs(g, gl));
		}
	}
}
