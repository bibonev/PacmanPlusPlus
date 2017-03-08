package teamproject.gamelogic.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import teamproject.constants.CellState;
import teamproject.constants.GameOutcome;
import teamproject.constants.GameOutcomeType;
import teamproject.event.Event;
import teamproject.event.arguments.GameDisplayInvalidatedEventArgs;
import teamproject.event.arguments.GameEndedEventArgs;
import teamproject.event.listener.GameDisplayInvalidatedListener;
import teamproject.event.listener.GameEndedListener;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.Entity;
import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Player;

public class LocalGameLogic implements GameLogic {
	private Game game;
	private Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> onGameDisplayInvalidated;
	private Event<GameEndedListener, GameEndedEventArgs> onGameEnded;

	public LocalGameLogic(final Game game) {
		this.game = game;
		onGameDisplayInvalidated = new Event<>((l, a) -> l.onGameDisplayInvalidated(a));
		onGameEnded = new Event<>((l, a) -> l.onGameEnded(a));
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void gameStep(final int period) {
		if (!game.hasEnded()) {
			checkEndingConditions();
			game.getWorld().getMap().gameStep(game);
			final HashSet<Player> eatenPlayers = new HashSet<>();

			for (final Entity entity : game.getWorld().getEntities()) {
				entity.gameStep(game);
				eatenPlayers.addAll(getEatenPlayers());
				checkEndingConditions();
			}

			eatenPlayers.addAll(getEatenPlayers());
			for (final Player p : eatenPlayers) {
				game.getWorld().removeEntity(p.getID());
			}
			checkEndingConditions();
			invalidateDisplay();
		}
	}

	private Set<Player> getEatenPlayers() {
		final Set<Player> players = new HashSet<>();
		for (final Ghost g : game.getWorld().getEntities(Ghost.class)) {
			for (final Player p : game.getWorld().getPlayers()) {
				if (g.getPosition().equals(p.getPosition())) {
					players.add(p);
				}
			}
		}
		return players;
	}

	private boolean ghostsEatenPlayers() {
		return game.getWorld().getEntities(Player.class).size() == 0;
	}

	private boolean allFoodEaten() {
		final Cell[][] cells = game.getWorld().getMap().getCells();
		for (final Cell[] cellRow : cells) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cellRow[j].getState().equals(CellState.FOOD)) {
					return false;
				}
			}
		}

		return true;
	}

	public void checkEndingConditions() {
		if (ghostsEatenPlayers()) {
			final GameOutcome outcome = new GameOutcome(GameOutcomeType.GHOSTS_WON);
			onGameEnded(outcome);
		}

		if (allFoodEaten()) {
			final List<Player> winners = getWinners();
			final GameOutcome outcome = winners.size() > 1 ? new GameOutcome(GameOutcomeType.TIE)
					: new GameOutcome(GameOutcomeType.PLAYER_WON, winners.get(0));
			onGameEnded(outcome);
		}
	}

	private List<Player> getWinners() {
		final Stream<Player> playersStream = game.getWorld().getEntities(Player.class).stream();
		final int maxDotsEaten = playersStream.map(player -> player.getDotsEaten()).max((x, y) -> x > y ? x : y).get();

		final List<Player> winners = new ArrayList<Player>();
		winners.addAll(
				playersStream.filter(player -> player.getDotsEaten() == maxDotsEaten).collect(Collectors.toList()));

		return winners;
	}

	private void invalidateDisplay() {
		onGameDisplayInvalidated.fire(new GameDisplayInvalidatedEventArgs(this));
	}

	private void onGameEnded(final GameOutcome outcome) {
		if (!game.hasEnded()) {
			game.setEnded();
			onGameEnded.fire(new GameEndedEventArgs(this, outcome));
		}
	}

	@Override
	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated() {
		return onGameDisplayInvalidated;
	}

	@Override
	public Event<GameEndedListener, GameEndedEventArgs> getOnGameEnded() {
		return onGameEnded;
	}
}
