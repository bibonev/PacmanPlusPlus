package test.java.networking.integration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.ai.AIGhost;
import main.java.ai.DefaultBehaviour;
import main.java.ai.GhostBehaviour;
import main.java.constants.GameType;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.LocalGameLogic;
import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Behaviour.Type;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalGhost;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.RemoteSkillSet;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;
import main.java.gamelogic.domain.World;
import main.java.networking.ServerManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ServerTrigger;
import main.java.networking.integration.ServerInstance;

public class ServerInstanceTest {
	private TestableServerInstance server;
	private Game game;
	private LocalGameLogic logic;
	private Lobby lobby;

	@Before
	public void setUp() throws Exception {
		game = new Game(new World(new RuleChecker(), Map.generateMap(), false), new GameSettings(), GameType.MULTIPLAYER_SERVER);
		lobby = new Lobby();
		server = new TestableServerInstance(lobby);
		logic = new LocalGameLogic(game);
		server.run();
		server.onGameCreated(new GameCreatedEventArgs(game, logic));
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	/**
	 * Tests that the correct packets are sent to players representing
	 * the initial state of the lobby upon joining a server.
	 */
	@Test
	public void testInitialLobbyStateSent() {
		lobby.addPlayer(0, new LobbyPlayerInfo(0, "alice"));
		lobby.addPlayer(1, new LobbyPlayerInfo(1, "bob"));
		
		int[] playerCount = {0}; // bloody java closures
		
		server.setManager(new MockServerManager(server, lobby) {
			@Override
			public void dispatch(int clientID, Packet packet) {
				if(packet.getPacketName().equals("lobby-player-enter")) playerCount[0]++;
			}
		});
		
		server.sendInitialLobbyState(0);
		
		assertEquals(2, playerCount[0]);
	}
	
	@Test
	public void testEntityMovementTransmitted() {
		lobby.addPlayer(0, new LobbyPlayerInfo(0, "alice"));
		lobby.addPlayer(1, new LobbyPlayerInfo(1, "bob"));
		LocalGhost g = new LocalGhost();
		g.setPosition(new Position(0, 0));
		
		int[] movementPackets = {0, 0, 0};
		
		server.setManager(new MockServerManager(server, lobby) {
			@Override
			public void dispatch(int clientID, Packet packet) {
				switch(packet.getPacketName()) {
				case "remote-ghost-joined": movementPackets[0]++; break;
				case "remote-ghost-moved": movementPackets[1]++; break;
				case "remote-ghost-died": movementPackets[2]++; break;
				}
			}
		});
		game.getWorld().addEntity(g);
		assertEquals(2, movementPackets[0]);
		
		for(int i = 0; i < 10; i++) {
			assertEquals(i * 2, movementPackets[1]);
			g.setPosition(new Position(0, i));
		}
		assertEquals(20, movementPackets[1]);
		
		game.getWorld().removeEntity(g.getID());
		assertEquals(2, movementPackets[2]);
	}
	
	@Test
	public void testPlayerPacketsTransmitted() {
		lobby.addPlayer(0, new LobbyPlayerInfo(0, "alice"));
		lobby.addPlayer(1, new LobbyPlayerInfo(1, "bob"));
		lobby.addPlayer(2, new LobbyPlayerInfo(2, "charles"));
		
		PacketCounterServerManager manager = new PacketCounterServerManager(server, lobby);
		server.setManager(manager);
		RemotePlayer alice = new RemotePlayer(0, lobby.getPlayer(0).getName());
		alice.setPosition(new Position(0, 0));
		alice.setSkillSet(new RemoteSkillSet(alice));
		game.getWorld().addEntity(alice);
		
		assertEquals(2, manager.getCount("remote-player-joined"));
		assertEquals(1, manager.getCount("local-player-joined"));
		
		for(int i = 0; i < 10; i++) {
			assertEquals(2 * i, manager.getCount("remote-player-moved"));
			alice.setPosition(new Position(0, i));
		}
		assertEquals(20, manager.getCount("remote-player-moved"));
		
		alice.setDeathReason("bad");
		game.getWorld().removeEntity(alice.getID());

		assertEquals(2, manager.getCount("remote-player-died"));
		assertEquals(1, manager.getCount("local-player-died"));
	}
	
	@Test
	public void testLobbyStateChangesSent() {
		Random r = new Random();

		PacketCounterServerManager manager = new PacketCounterServerManager(server, lobby);
		server.setManager(manager);
		
		for(int i = 0; i < 200; i++) {
			if(lobby.getPlayerCount() == 0 || r.nextFloat() > 0.333f) {//add
				int idtoAdd = lobby.getPlayerIDs().stream().max(Integer::compare).orElse(0);
				lobby.addPlayer(idtoAdd, new LobbyPlayerInfo(idtoAdd, "player" + i));
				assertEquals(lobby.getPlayerCount() - 1, manager.getCount("lobby-player-enter"));
				manager.reset();
			} else { //remove
				int idToRemove = r.nextInt(lobby.getPlayerCount());
				lobby.removePlayer(idToRemove);
				assertEquals(lobby.getPlayerCount(), manager.getCount("lobby-player-leave"));
				manager.reset();
			}
		}
	}
	
	@Test
	public void testSpawnersWork() {
		PacketCounterServerManager manager = new PacketCounterServerManager(server, lobby);
		server.setManager(manager);
		String[] players = {"alice", "bob", "charles"};
		
		for(int i = 0; i < players.length; i++) {
			lobby.addPlayer(i, new LobbyPlayerInfo(i, players[i]));
			RemotePlayer p = new RemotePlayer(i, players[i]);
			p.setSkillSet(LocalSkillSet.createDefaultSkillSet(p));
			p.setPosition(new Position(0, i));
			game.getWorld().addEntity(p);
		}
		game.setStarted();
		final int spawnDuration = 5;
		AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(0, 0));
		Behaviour behaviour = new DefaultBehaviour(game.getWorld(), ghost, Type.DEFAULT);
		ghost.setBehaviour(behaviour);
		
		Spawner spawner = new Spawner(spawnDuration, ghost, SpawnerColor.RED);
		spawner.setPosition(ghost.getPosition());
		
		game.getWorld().addEntity(spawner);
		assertEquals(3, manager.getCount("spawner-added"));
		
		for(int i = 0; i < 10; i++) {
			assertEquals(0, manager.getCount("remote-ghost-joined"));
			logic.gameStep(250);
		}

		assertEquals(3, manager.getCount("remote-ghost-joined"));
	}
	
	@Test
	public void testPlayerJoinAndDeathPacketsFired() {
		lobby.addPlayer(0, new LobbyPlayerInfo(0, "alice"));
		lobby.addPlayer(1, new LobbyPlayerInfo(1, "bob"));
		lobby.addPlayer(2, new LobbyPlayerInfo(2, "charles"));
		
		game.setStarted();
		
		PacketCounterServerManager manager = new PacketCounterServerManager(server, lobby);
		server.setManager(manager);
		
		RemotePlayer player = new RemotePlayer(0, "alice");
		player.setPosition(new Position(0, 0));
		player.setSkillSet(LocalSkillSet.createDefaultSkillSet(player));
		
		game.getWorld().addEntity(player);

		assertEquals(2, manager.getCount("remote-player-joined"));
		assertEquals(1, manager.getCount("local-player-joined"));
		
		AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(0, 0));
		ghost.setBehaviour(new GhostBehaviour(game.getWorld(), ghost, Type.GHOST));
		
		game.getWorld().addEntity(ghost);

		assertEquals(3, manager.getCount("remote-ghost-joined"));

		logic.gameStep(250);
		
		assertNull(game.getWorld().getEntity(player.getID()));
		assertEquals(1, manager.getCount("local-player-died"));
		assertEquals(2, manager.getCount("remote-player-died"));
	}
	
	@Test
	public void testGameEndPacketsFired() {
		lobby.addPlayer(0, new LobbyPlayerInfo(0, "alice"));
		lobby.addPlayer(1, new LobbyPlayerInfo(1, "bob"));
		lobby.addPlayer(2, new LobbyPlayerInfo(2, "charles"));
		
		game.setStarted();
		
		Packet[] packetArr = {null};

		server.setManager(new MockServerManager(server, lobby) {
			@Override
			public void dispatch(int clientID, Packet p) {
				if(p.getPacketName().equals("game-ended"))
					packetArr[0] = p;
			}
		});
	
		RemotePlayer player = new RemotePlayer(0, "alice");
		player.setPosition(new Position(0, 0));
		player.setSkillSet(LocalSkillSet.createDefaultSkillSet(player));
		
		game.getWorld().addEntity(player);
		
		AIGhost ghost = new AIGhost();
		ghost.setPosition(new Position(0, 0));
		ghost.setBehaviour(new GhostBehaviour(game.getWorld(), ghost, Type.GHOST));
		
		game.getWorld().addEntity(ghost);

		logic.gameStep(250);
		
		Packet packet = packetArr[0];
		assertNotNull(packet);
		assertEquals("ghosts-won", packet.getString("outcome"));
	}
	
	public static class TestableServerInstance extends ServerInstance {
		public TestableServerInstance(Lobby lobby) {
			super(lobby);
		}
		
		public void setManager(ServerManager manager) {
			this.manager = manager;
			this.manager.setTrigger(this);
		}
		
		@Override
		public void run() {
			setManager(new MockServerManager(this, this.lobby) {
				@Override
				public void dispatch(int clientID, Packet packet) {}
			});
			this.addGameHooks();
		}
		
		@Override
		public void stop() {
			this.removeGameHooks();
		}
	}
	
	public static class PacketCounterServerManager extends MockServerManager {
		private HashMap<String, Integer> packets;
		public PacketCounterServerManager(ServerTrigger trigger, Lobby lobby) {
			super(trigger, lobby);
			packets = new HashMap<>();
		}
		
		@Override
		public void dispatch(int clientID, Packet packet) {
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

	public static abstract class MockServerManager implements ServerManager {
		private ServerTrigger trigger;
		private Lobby lobby;
		
		public MockServerManager(ServerTrigger trigger, Lobby lobby) {
			this.lobby = lobby;
			this.trigger = trigger;
		}
		
		@Override
		public void setTrigger(ServerTrigger trigger) {}

		@Override
		public ServerTrigger getTrigger() {
			return trigger;
		}
		
		@Override
		public void dispatchAll(Packet packet) {
			for(int id : lobby.getPlayerIDs()) {
				this.dispatch(id, packet);
			}
		}
		
		@Override
		public void dispatchAllExcept(Packet packet, int... clientID) {
			outer: for(int id : lobby.getPlayerIDs()) {
				for(int i = 0; i < clientID.length; i++) {
					if(id == i) continue outer;
				}
				this.dispatch(id, packet);
			}
		}
	}
}
