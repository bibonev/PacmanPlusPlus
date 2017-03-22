package test.java.networking.integration;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.ai.AIGhost;
import main.java.constants.GameType;
import main.java.event.arguments.GameCreatedEventArgs;
import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.core.LocalGameLogic;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalGhost;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.World;
import main.java.networking.ServerManager;
import main.java.networking.data.Packet;
import main.java.networking.event.ServerTrigger;
import main.java.networking.integration.ServerInstance;

public class TestServerInstance {
	private ServerInstance server;
	private Game game;
	private LocalGameLogic logic;
	private Lobby lobby;

	@Before
	public void setUp() throws Exception {
		game = new Game(new World(new RuleChecker(), Map.generateMap(), false), new GameSettings(), GameType.MULTIPLAYER_SERVER);
		lobby = new Lobby();
		server = new ServerInstance(lobby);
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
		
		int[] remotePackets = {0, 0, 0};
		int[] localPackets = {0, 0};
		
		server.setManager(new MockServerManager(server, lobby) {
			@Override
			public void dispatch(int clientID, Packet packet) {
				if(clientID == 1) {
					switch(packet.getPacketName()) {
					case "remote-player-joined": remotePackets[0]++; break;
					case "remote-player-moved": remotePackets[1]++; break;
					case "remote-player-died": remotePackets[2]++; break;
					}
				} else {
					switch(packet.getPacketName()) {
					case "local-player-joined": localPackets[0]++; break;
					case "local-player-died": localPackets[1]++; break;
					}
				}
			}
		});
		RemotePlayer alice = new RemotePlayer(0, lobby.getPlayer(0).getName());
		alice.setPosition(new Position(0, 0));
		game.getWorld().addEntity(alice);
		
		assertEquals(1, remotePackets[0]);
		assertEquals(1, localPackets[0]);
		
		for(int i = 0; i < 10; i++) {
			assertEquals(i, remotePackets[1]);
			alice.setPosition(new Position(0, i));
		}
		assertEquals(10, remotePackets[1]);
		
		alice.setDeathReason("bad");
		game.getWorld().removeEntity(alice.getID());

		assertEquals(1, remotePackets[2]);
		assertEquals(1, localPackets[1]);
	}
	
	@Test
	public void testLobbyStateChangesSent() {
		int[] lobbyChangePackets = {0, 0};
		Random r = new Random();
		
		server.setManager(new MockServerManager(server, lobby) {
			@Override
			public void dispatch(int clientID, Packet packet) {
				switch(packet.getPacketName()) {
				case "lobby-player-enter": lobbyChangePackets[0]++; break;
				case "lobby-player-left": lobbyChangePackets[1]++; break;
				}
			}
		});
		
		for(int i = 0; i < 200; i++) {
			if(lobby.getPlayerCount() == 0 || r.nextFloat() > 0.333f) {//add
				int idtoAdd = lobby.getPlayerIDs().stream().max(Integer::compare).orElse(0);
				lobby.addPlayer(idtoAdd, new LobbyPlayerInfo(idtoAdd, "player" + i));
				assertEquals(lobby.getPlayerCount() - 1, lobbyChangePackets[0]);
				lobbyChangePackets[0] = 0;
			} else { //remove
				int idToRemove = r.nextInt(lobby.getPlayerCount());
				lobby.removePlayer(idToRemove);
				assertEquals(lobby.getPlayerCount(), lobbyChangePackets[1]);
				lobbyChangePackets[1] = 0;
			}
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
