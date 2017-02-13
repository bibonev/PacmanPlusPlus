package teamproject.gamelogic.event.listener;

import java.util.ArrayList;
import java.util.Collection;

import teamproject.event.Event;
import teamproject.event.arguments.container.NewGameRequestedEventArguments;
import teamproject.event.arguments.container.NewGameStartedEventArguments;
import teamproject.event.listener.NewGameStartedEventListener;
import teamproject.gamelogic.domain.GLMap;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.World;
import teamproject.gamelogic.domain.repository.Repository;

public class NewGameRequestedEventListener implements teamproject.event.listener.NewGameRequestedEventListener {

	@Override
	public void onNewGameRequested(final NewGameRequestedEventArguments args) {
		// Collect players
		// Just the one for now
		final Player player = Repository.getHumanPlayer(args.getUserName());
		final Collection<Player> players = new ArrayList<Player>();
		players.add(player);

		// Add some ghosts TODO: generate ghost properly
		// Just one for now
		final Collection<Ghost> ghosts = new ArrayList<Ghost>();

		// Generate a map
		// Simplest one for now (random)
		final Map map = new GLMap();

		// Create new game and store it
		final World world = new World(players, new RuleEnforcer(), ghosts, map);
		final Game game = new Game(Repository.nextGameId(), world, args.getSettings());
		Repository.addGame(game);

		// Fire NewGameStartedEvent
		final NewGameStartedEventArguments gameStartedEventArgs = new NewGameStartedEventArguments(game,
				args.getStage());
		final Event<NewGameStartedEventListener, NewGameStartedEventArguments> newGameStartedEvent = new Event<>(
				(listener, s) -> listener.onNewGameStarted(s));
		newGameStartedEvent.fire(gameStartedEventArgs);
	}

}
