package main.java.gamelogic.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.java.constants.CellState;
import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.constants.GameType;
import main.java.event.arguments.EntityChangedEventArgs;
import main.java.event.arguments.GameDisplayInvalidatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.LocalPlayerDespawnEventArgs;
import main.java.event.arguments.LocalPlayerSpawnEventArgs;
import main.java.event.listener.EntityAddedListener;
import main.java.event.listener.EntityRemovingListener;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.Ghost;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;

public class LocalGameLogic extends GameLogic implements EntityAddedListener, EntityRemovingListener {
	private Game game;

	public LocalGameLogic(final Game game) {
		super(game);
		this.game = game;
		
		this.game.getWorld().getOnEntityAddedEvent().addListener(this);
		this.game.getWorld().getOnEntityRemovingEvent().addListener(this);
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void gameStep(final int period) {
		if (game.hasStarted() && !game.hasEnded()) {
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
				p.setDeathReason("Eaten by a ghost!");
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
		getOnGameDisplayInvalidated().fire(new GameDisplayInvalidatedEventArgs(this));
	}

	private void onGameEnded(final GameOutcome outcome) {
		if (!game.hasEnded()) {
			game.setEnded();
			getOnGameEnded().fire(new GameEndedEventArgs(this, outcome));
		}
	}

	@Override
	public void onEntityRemoving(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		if(e instanceof ControlledPlayer) {
			boolean canRespawn = ((ControlledPlayer) e).canRespawn();
			String deathReason = ((ControlledPlayer) e).getDeathReason();
			getOnLocalPlayerDespawn().fire(new LocalPlayerDespawnEventArgs(canRespawn, deathReason));
		}
	}

	@Override
	public void onEntityAdded(EntityChangedEventArgs args) {
		Entity e = args.getWorld().getEntity(args.getEntityID());
		if(e instanceof ControlledPlayer) {
			getOnLocalPlayerSpawn().fire(new LocalPlayerSpawnEventArgs((ControlledPlayer) e));
		}
	}
	
	@Override
	public void readyToStart() {
		super.readyToStart();
		
		if(game.getGameType() == GameType.SINGLEPLAYER) {
			ControlledPlayer player = new ControlledPlayer(0, "You");
			player.setPosition(new Position(0, 6));
			game.getWorld().addEntity(player);
			game.setStarted();
		}
	}
}
