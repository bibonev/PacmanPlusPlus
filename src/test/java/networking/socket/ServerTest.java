package test.java.networking.socket;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.java.networking.socket.Server;

public class ServerTest {
	private Server server;
	
	@Before
	public void setup() {
		this.server = new Server();
	}
	@Test
	public void testNotAliveBeforeStarting() {
		assertFalse(server.isAlive());
	}
	
	@Test
	public void testNoConnectedClientsBeforeStartingServer() {
		assertEquals(0, server.getConnectedClients().size());
	}
	
	@Test
	public void testEventsNotNull() {
		assertNotNull(server.getClientConnectedEvent());
		assertNotNull(server.getClientDisconnectedEvent());
	}
	
	@Test
	public void testDieDoesNothingBeforeStarting() {
		server.die();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCannotGetNonexistentClients() {
		server.getClient(0);
		server.getClient(1);
	}
}
