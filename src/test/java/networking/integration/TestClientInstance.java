package test.java.networking.integration;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.ai.AIGhost;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.listener.PlayerLeavingGameListener;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.LocalGameLogic;
import main.java.gamelogic.core.RemoteGameLogic;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalGhost;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.World;
import main.java.networking.ClientManager;
import main.java.networking.ServerManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientTrigger;
import main.java.networking.event.ServerTrigger;
import main.java.networking.integration.ClientInstance;
import main.java.networking.integration.ServerInstance;
import main.java.ui.GameInterface;
import main.java.ui.GameUI;

public class TestClientInstance {
	private TestableClientInstance client;
	private Game game;
	private RemoteGameLogic logic;
	private Lobby lobby;

	@Before
	public void setUp() throws Exception {
		game = new Game(new World(new RuleChecker(), Map.generateMap(), false), new GameSettings(), GameType.MULTIPLAYER_SERVER);
		lobby = new Lobby();
		client = new TestableClientInstance(new GameInterface() {
			private Event<PlayerLeavingGameListener, Object> onPlayerLeavingGame;

			{
				this.onPlayerLeavingGame = new Event<PlayerLeavingGameListener, Object>((l, a) -> l.onPlayerLeavingGame());
			}
			
			@Override
			public void timer() {}
			
			@Override
			public void setLobby(Lobby lobby) {}
			
			@Override
			public void onPlayerLeavingGame() {}
			
			@Override
			public Event<PlayerLeavingGameListener, Object> getOnPlayerLeavingGame() {
				return onPlayerLeavingGame;
			}
		}, "hello", "localhost");
		logic = new RemoteGameLogic(game);
		client.run();
		client.onGameCreated(new GameCreatedEventArgs(game, logic));
	}

	@After
	public void tearDown() throws Exception {
		client.stop();
	}
	
	@Test
	public void testLocalPlayerMovementPacketsSent() {
		
	}
	
	public static class TestableClientInstance extends ClientInstance {
		public TestableClientInstance(GameInterface gameUI, String username, String serverAddress) {
			super(gameUI, username, serverAddress);
		}
		
		public void setManager(ClientManager manager) {
			this.manager = manager;
			this.manager.setTrigger(this);
		}
		
		@Override
		public void run() {
			this.addGameHooks();
		}
		
		@Override
		public void stop() {
			this.removeGameHooks();
		}
	}

	public static abstract class MockClientManager implements ClientManager {
		private ClientTrigger trigger;
		private Lobby lobby;
		
		public MockClientManager(ClientTrigger trigger, Lobby lobby) {
			this.lobby = lobby;
			this.trigger = trigger;
		}
		
		@Override
		public void setTrigger(ClientTrigger trigger) {}

		@Override
		public ClientTrigger getTrigger() {
			return trigger;
		}
	}
}
