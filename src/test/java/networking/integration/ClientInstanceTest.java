package test.java.networking.integration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.event.listener.PlayerLeavingGameListener;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.RemoteGameLogic;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemoteSkillSet;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.World;
import main.java.networking.ClientManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ClientTrigger;
import main.java.networking.event.ServerTrigger;
import main.java.networking.integration.ClientInstance;
import main.java.networking.socket.Client;
import main.java.ui.GameInterface;
import test.java.networking.integration.ServerInstanceTest.MockServerManager;

public class ClientInstanceTest {
	private TestableClientInstance client;
	private Game game;
	private RemoteGameLogic logic;
	@Before
	public void setUp() throws Exception {
		game = new Game(new World(new RuleChecker(), Map.generateMap(), false), new GameSettings(), GameType.MULTIPLAYER_CLIENT);
		new Lobby();
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
		ControlledPlayer player = new ControlledPlayer(0, "hello");
		Random r = new Random();
		player.setPosition(new Position(0, 0));
		game.getWorld().addEntity(player);
		
		PacketCounterClientManager manager = new PacketCounterClientManager(client);
		client.setManager(manager);
		
		for(int i = 0; i < 10; i++) {
			if(r.nextBoolean()) {
				player.setPosition(new Position(0, i));
				assertEquals(2, manager.getCount("player-moved"));
				manager.reset();
			} else {
				player.setAngle(-90 + 90 * r.nextInt(4));
				assertEquals(1, manager.getCount("player-moved"));
				manager.reset();
			}
		}
	}
	
	@Test
	public void testHandshakeSent() throws Exception {
		boolean[] correctHandshake = {false};
		
		MockClientManager fakeManager = new MockClientManager(client) {
			@Override
			public void dispatch(Packet packet) {
				if(packet.getString("username").equals("hello")) {
					correctHandshake[0] = true;
				}
			}
		};
		client.setManager(fakeManager);
		client.trigger(new Packet("server-handshake").setInteger("client-id", 2));
		
		assertTrue(correctHandshake[0]);
	}
	
	@Test
	public void testPlayerAbilityPacketsSent() throws Exception {
		int[] packetCount = {0, 0};
		this.client.setClient(new Client("blah") {
			@Override
			public int getClientID() {
				return 0;
			}
		});
		
		ClientManager fakeManager = new MockClientManager(client) {
			@Override
			public void dispatch(Packet packet) {
				if(packet.getPacketName().equals("use-ability")) {
					char slot = packet.getString("ability-key").charAt(0);
					packetCount[slot == 'q' ? 0 : slot == 'w' ? 1 : -1]++;
				}
			}
		};

		client.setManager(fakeManager);
		client.trigger(
				new Packet("local-player-joined")
				.setInteger("row", 0)
				.setInteger("col", 0)
				.setDouble("angle", 0));
		
		ControlledPlayer player = (ControlledPlayer) game.getWorld().getEntity(0);
		assertNotNull(player);

		player.getSkillSet().activateW();
		assertEquals(1, packetCount[1]);
		player.getSkillSet().activateQ();
		assertEquals(1, packetCount[0]);
		player.getSkillSet().activateW();
		assertEquals(2, packetCount[1]);
	}
	
	@Test
	public void testSpawnersDisappear() {
		this.client.setManager(new PacketCounterClientManager(client));
		this.client.setClient(new Client("blah") {
			@Override
			public int getClientID() {
				return 0;
			}
		});
		
		client.trigger(
				new Packet("local-player-joined")
				.setInteger("row", 0)
				.setInteger("col", 0)
				.setDouble("angle", 0));
		
		ControlledPlayer player = (ControlledPlayer) game.getWorld().getEntity(0);
		assertNotNull(player);
		
		final int duration = 5;
		
		client.trigger(
				new Packet("spawner-added")
				.setInteger("row", 0)
				.setInteger("col", 5)
				.setInteger("duration", duration)
				.setString("entity-type", "ghost"));
		
		Optional<Spawner> spawnerOptional = game.getWorld().getEntities(Spawner.class).stream().findFirst();
		
		assertTrue(spawnerOptional.isPresent());
		Spawner spawner = spawnerOptional.get();
		int id = spawner.getID();
		
		for(int i = 0; i < duration * 2; i++) {
			assertNotNull(game.getWorld().getEntity(id));
			logic.gameStep(250);
		}
		
		assertNull(game.getWorld().getEntity(id));
	}
	
	public static class TestableClientInstance extends ClientInstance {
		public TestableClientInstance(GameInterface gameUI, String username, String serverAddress) {
			super(gameUI, username, serverAddress);
		}
		
		public void setManager(ClientManager manager) {
			this.manager = manager;
			this.manager.setTrigger(this);
		}
		
		public void setClient(Client client) {
			this.client = client;
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
	
	public static class PacketCounterClientManager extends MockClientManager {
		private HashMap<String, Integer> packets;
		
		public PacketCounterClientManager(ClientTrigger trigger) {
			super(trigger);
			packets = new HashMap<>();
		}
		
		@Override
		public void dispatch(Packet packet) {
			String packetName = packet.getPacketName();
			packets.put(packetName, 1 + packets.getOrDefault(packetName, 0));
		}
		
		public int getCount(String packetName) {
			return packets.getOrDefault(packetName, 0);
		}
		
		public void reset() {
			packets.clear();
		}
	}

	public static abstract class MockClientManager implements ClientManager {
		private ClientTrigger trigger;
		
		public MockClientManager(ClientTrigger trigger) {
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
