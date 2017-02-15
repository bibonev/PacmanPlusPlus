package teamproject.gamelogic.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javafx.stage.Stage;
import teamproject.ai.AIGhost;
import teamproject.ai.GhostBehaviour;
import teamproject.event.Event;
import teamproject.event.arguments.container.NewGameStartedEventArguments;
import teamproject.event.arguments.container.NewMultiplayerGameRequestedEventArguments;
import teamproject.event.listener.NewGameStartedEventListener;
import teamproject.event.listener.NewMultiplayerGameRequestedEventListener;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.GLMap;
import teamproject.gamelogic.domain.GLPosition;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.World;
import teamproject.gamelogic.domain.repository.Repository;

public class GameCommandService {

	// TODO: refactor to get some of these things as constructor params rather
	// than creating
	// them everytime we call the methods

	private Game generateNewGame(final String userName, final GameSettings settings) {
		// Generate a map
		// Simplest one for now
		final Map map = new GLMap().generateMap();

		// Collect players
		// Just the one for now
		final Player player = Repository.getHumanPlayer(userName);
		final Collection<Player> players = new ArrayList<Player>();
		players.add(player);

		// Collect ghosts
		// Just one for now
		final Ghost ghost = new AIGhost(new GhostBehaviour(map, new GLPosition(0, 0), 1000,
				new Inventory(new HashMap<Item, Integer>()), Behaviour.Type.GHOST), "Ghost");
		final Collection<Ghost> ghosts = new ArrayList<Ghost>();
		ghosts.add(ghost);

		// Create new game and store it
		final World world = new World(players, new RuleEnforcer(), ghosts, map);
		final Game game = new Game(Repository.nextGameId(), world, settings);
		Repository.addGame(game);

		return game;
	}

	public void startNewSingleplayerGame(final String userName, final GameSettings settings, final Stage stage) {
		final Game game = generateNewGame(userName, settings);

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

	public void requestNewMultiplayerGame(final String userName, final GameSettings settings) {
		// Fire NewMultiplayerGameRequestedEvent
		final NewMultiplayerGameRequestedEventArguments multiplayerGameRequestedEventArgs = new NewMultiplayerGameRequestedEventArguments(
				userName, settings);
		final Event<NewMultiplayerGameRequestedEventListener, NewMultiplayerGameRequestedEventArguments> multiplayerGameRequestedEvent = new Event<>(
				(listener, s) -> listener.onNewMultiplayerGameRequested(s));
		// TODO: Networking to add their listener here
		multiplayerGameRequestedEvent.fire(multiplayerGameRequestedEventArgs);
	}

}
