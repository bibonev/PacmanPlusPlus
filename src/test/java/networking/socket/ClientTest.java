package test.java.networking.socket;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.networking.socket.Client;

public class ClientTest {
	private Client client;

	@Before
	public void setUp() {
		this.client = new Client("localhost");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCannotGetClientIDBeforeConnection() {
		client.getClientID();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCannotGetSendBeforeConnection() {
		client.getClientID();
	}
	
	public void testEventsDontReturnNull() {
		assertNotNull(client.getDisconnectedEvent());
		assertNotNull(client.getReceiveEvent());
	}
}
