package teamproject.gamelogic.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javafx.stage.Stage;
import teamproject.ai.AIGhost;
import teamproject.ai.DefaultBehaviour;
import teamproject.ai.GhostBehaviour;
import teamproject.constants.EntityType;
import teamproject.event.Event;
import teamproject.event.arguments.NewGameStartedEventArguments;
import teamproject.event.arguments.NewMultiplayerGameRequestedEventArguments;
import teamproject.event.listener.NewGameStartedEventListener;
import teamproject.event.listener.NewMultiplayerGameRequestedEventListener;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.ControlledPlayer;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.LocalPlayer;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RuleChecker;
import teamproject.gamelogic.domain.World;

public class GameCommandService {

	// TODO: refactor to get some of these things as constructor params rather
	// than creating
	// them everytime we call the methods

	public static Game generateNewSinglePlayerGame(final String userName, final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = new Map().generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map);
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
	
	public static Game generateNewMultiplayerGame(final String userName, final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = new Map().generateMap();

		// Create new game and store it
		final World world = new World(new RuleChecker(), map);
		final ControlledPlayer player = new ControlledPlayer(0, userName);
		player.setPosition(new Position(0, 0));
		
		final Game game = new Game(world, settings, player);

		
		// Collect players
		// Just the one for now

		world.addEntity(player);

		return game;
	}

	public void startNewSingleplayerGame(final String userName, final GameSettings settings, final Stage stage) {
		final Game game = generateNewSinglePlayerGame(userName, settings);

		// Fire NewGameStartedEvent
		final NewGameStartedEventArguments gameStartedEventArgs = new NewGameStartedEventArguments(game, stage);
		final Event<NewGameStartedEventListener, NewGameStartedEventArguments> newGameStartedEvent = new Event<>(
				(listener, s) -> listener.onNewGameStarted(s));
		newGameStartedEvent.addListener(new teamproject.graphics.event.listener.NewGameStartedEventListener());
		newGameStartedEvent.fire(gameStartedEventArgs);
	}

	public void startNewMultiplayerGame() {
		// TODO: fill up and call this from listener that is linked to
		// networking
	}

	// TODO: Maybe we could do without passing the stage around? Not sure how to
	// do this though...
	public void requestNewMultiplayerGame(final String userName, final GameSettings settings, final Stage stage) {
		// Fire NewMultiplayerGameRequestedEvent
		final NewMultiplayerGameRequestedEventArguments multiplayerGameRequestedEventArgs = new NewMultiplayerGameRequestedEventArguments(
				userName, settings, stage);
		final Event<NewMultiplayerGameRequestedEventListener, NewMultiplayerGameRequestedEventArguments> multiplayerGameRequestedEvent = new Event<>(
				(listener, s) -> listener.onNewMultiplayerGameRequested(s));
		// TODO: Networking to add their listener here
		multiplayerGameRequestedEvent.fire(multiplayerGameRequestedEventArgs);
	}

}
