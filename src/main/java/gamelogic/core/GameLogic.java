package main.java.gamelogic.core;

import main.java.event.Event;
import main.java.event.arguments.GameDisplayInvalidatedEventArgs;
import main.java.event.arguments.GameEndedEventArgs;
import main.java.event.arguments.LocalPlayerDespawnEventArgs;
import main.java.event.arguments.LocalPlayerSpawnEventArgs;
import main.java.event.arguments.ReadyToStartEventArgs;
import main.java.event.listener.GameDisplayInvalidatedListener;
import main.java.event.listener.GameEndedListener;
import main.java.event.listener.LocalPlayerDespawnListener;
import main.java.event.listener.LocalPlayerSpawnListener;
import main.java.event.listener.ReadyToStartListener;
import main.java.gamelogic.domain.Game;

public abstract class GameLogic {
	public static final int GAME_STEP_DURATION = 250;
	private Game game;
	private Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> onGameDisplayInvalidated;
	private Event<GameEndedListener, GameEndedEventArgs> onGameEnded;
	private Event<LocalPlayerSpawnListener, LocalPlayerSpawnEventArgs> onLocalPlayerSpawn;
	private Event<LocalPlayerDespawnListener, LocalPlayerDespawnEventArgs> onLocalPlayerDespawn;
	private Event<ReadyToStartListener, ReadyToStartEventArgs> onReadyToStart;
	
	public abstract void gameStep(int delay);

	public Event<GameDisplayInvalidatedListener, GameDisplayInvalidatedEventArgs> getOnGameDisplayInvalidated() {
		return onGameDisplayInvalidated;
	}

	public Event<GameEndedListener, GameEndedEventArgs> getOnGameEnded() {
		return onGameEnded;
	}

	public Event<LocalPlayerSpawnListener, LocalPlayerSpawnEventArgs> getOnLocalPlayerSpawn() {
		return onLocalPlayerSpawn;
	}

	public Event<LocalPlayerDespawnListener, LocalPlayerDespawnEventArgs> getOnLocalPlayerDespawn() {
		return onLocalPlayerDespawn;
	}

	public Event<ReadyToStartListener, ReadyToStartEventArgs> getOnReadyToStart() {
		return onReadyToStart;
	}
	
	public void readyToStart() {
		getOnReadyToStart().fire(new ReadyToStartEventArgs(game, this));
	}
	
	public GameLogic(final Game game) {
		this.game = game;
		
		onReadyToStart = new Event<>((l, a) -> l.onReadyToStart(a));
		onGameDisplayInvalidated = new Event<>((l, a) -> l.onGameDisplayInvalidated(a));
		onGameEnded = new Event<>((l, a) -> l.onGameEnded(a));
		onLocalPlayerSpawn = new Event<>((l, a) -> l.onLocalPlayerSpawn(a));
		onLocalPlayerDespawn = new Event<>((l, a) -> l.onLocalPlayerDespawn(a));
	}
}
