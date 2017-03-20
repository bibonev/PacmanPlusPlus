package main.java.gamelogic.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;

public class LocalGameLogic extends GameLogic implements EntityAddedListener, EntityRemovingListener {
	private Game game;
	private int gameStepsElapsed = 0;

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
            final HashSet<Entity> killedEntities = new HashSet<>();

			for (final Entity entity : game.getWorld().getEntities()) {
				entity.gameStep(game);
				eatenPlayers.addAll(getEatenPlayers());
				invalidateDisplay();
                killedEntities.addAll(getKilledEntitiesByLaser());
				checkEndingConditions();
			}

			eatenPlayers.addAll(getEatenPlayers());
            killedEntities.addAll(getKilledEntitiesByLaser());
			for (final Player p : eatenPlayers) {
			    if(p.getShield() > 0) {
			        p.reduceShield();
			        System.out.println("Saved!");
                } else {
                    p.setDeathReason("Eaten by a ghost!");
                    game.getWorld().removeEntity(p.getID());
                }
			}
            for (final Entity e : killedEntities) {
			    if(game.getWorld().getEntities().contains(e)) {
			        if(e instanceof Player) {
			            ((Player) e).setDeathReason("Killed by a laser!");
                    }
                    game.getWorld().removeEntity(e.getID());
                }
            }

			Set<Spawner> spawnersToRemove = new HashSet<Spawner>();
			for(Spawner spawner : game.getWorld().getEntities(Spawner.class)) {
				if(spawner.isExpired()) spawnersToRemove.add(spawner);
			}
			for (final Spawner spawner : spawnersToRemove) {
				if (spawner.getEntity() != null) {
					game.getWorld().addEntity(spawner.getEntity());
				}
				game.getWorld().removeEntity(spawner.getID());
			}
            checkEndingConditions();
            decayPlayerShields();
			invalidateDisplay();


			gameStepsElapsed += 1;
		}
	}

    private void decayPlayerShields() {
        if(gameStepsElapsed % 20 == 0) {
            for(Player p : game.getWorld().getPlayers()) {
                if(p.getShield() > 0)
                    p.reduceShield();
            }
        }
    }

	private Set<Player> getEatenPlayers(){
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

	private Set<Entity> getKilledEntitiesByLaser(){
        final Set<Entity> entities = new HashSet<>();
        for (final Entity entity : game.getWorld().getEntities()) {
            if(entity.getIsKilled()) {
                entities.add(entity);
            }
        }
        return entities;
    }

	private boolean ghostsEatenPlayers() {
		for (final Spawner c : game.getWorld().getEntities(Spawner.class)) {
			final Entity e = c.getEntity();
			if (e != null && e instanceof Player) {
				return false;
			}
		}
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

	private void checkEndingConditions() {
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
		invalidateDisplay();
	}

	private List<Player> getWinners() {
		int maxDots = 0;
		final List<Player> winners = new ArrayList<>();
		final Collection<Player> allPlayers = game.getWorld().getPlayers();

		for (final Player player : allPlayers) {
			if (player.getDotsEaten() >= maxDots) { // add to winner collection
				if (player.getDotsEaten() > maxDots) { // better than other
														// winners
					maxDots = player.getDotsEaten();

					winners.clear();
				}
				winners.add(player);
			}
		}

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
	public void onEntityRemoving(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());
		if (e instanceof ControlledPlayer) {
			final boolean canRespawn = ((ControlledPlayer) e).canRespawn();
			final String deathReason = ((ControlledPlayer) e).getDeathReason();
			getOnLocalPlayerDespawn().fire(new LocalPlayerDespawnEventArgs(canRespawn, deathReason));
		}
	}

	@Override
	public void onEntityAdded(final EntityChangedEventArgs args) {
		final Entity e = args.getWorld().getEntity(args.getEntityID());
		if (e instanceof ControlledPlayer) {
			getOnLocalPlayerSpawn().fire(new LocalPlayerSpawnEventArgs((ControlledPlayer) e));
		}
	}

	@Override
	public void readyToStart() {
		super.readyToStart();

		if (game.getGameType() == GameType.SINGLEPLAYER) {
			final ControlledPlayer player = new ControlledPlayer(0, "You");
			player.setSkillSet(LocalSkillSet.createDefaultSkillSet(player));
			player.setPosition(getCandidateSpawnPosition());
			final Spawner spawner = new Spawner(5, player, SpawnerColor.GREEN);
			spawner.setPosition(player.getPosition());
			game.getWorld().addEntity(spawner);
			game.setStarted();
		}
	}
	
	public double getSpawnPositionRating(Position p) {
		CellState cellState = game.getWorld().getMap().getCell(p).getState();
		if(cellState == CellState.OBSTACLE || RuleChecker.isOutOfBounds(p.getRow(), p.getColumn())) return 0;
		
		double rating = 1;
		double aggregateDistance = 0;
		int entityCount = 0;
		for(Entity e : game.getWorld().getEntities()) {
			double distance = Math.abs(e.getPosition().getRow() - p.getRow()) +
					Math.abs(e.getPosition().getColumn() - p.getColumn());
			
			if(e instanceof Spawner) e = ((Spawner) e).getEntity();
			if(e instanceof Player) {
				aggregateDistance += distance;
			}
			if(e instanceof Ghost) {
				aggregateDistance += 2 * distance;
			}
			entityCount += 1;
		}
		rating += aggregateDistance / entityCount;
		if(cellState == CellState.FOOD) rating *= 0.75;
		
		return rating;
	}
	
	public Position getCandidateSpawnPosition() {
		Position bestPosition = null;
		double bestRating = 0;
		int mapSize = game.getWorld().getMap().getMapSize();
		
		for(int row = 0; row < mapSize; row++) {
			for(int col = 0; col < mapSize; col++) {
				Position position = new Position(row, col);
				double rating = getSpawnPositionRating(position);
				
				if(rating > bestRating) {
					bestRating = rating;
					bestPosition = position;
				}
			}
		}
		
		return bestPosition;
	}
}
