package teamproject.networking.data;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

import teamproject.event.Event;
import teamproject.networking.NetworkSocket;
import teamproject.networking.NetworkListener;
import teamproject.networking.NetworkServer;
import teamproject.networking.StandardClientManager;
import teamproject.networking.StandardServerManager;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;

public class StandardServerManagerTest {
	@Test
	public void testTriggersTriggered() {
		HashMap<Integer, Integer> received = new HashMap<Integer, Integer>();
		
		StandardServerManager nm = new StandardServerManager(new MockServer());
		
		nm.addTrigger((i, t) -> {
			received.put(i, t.getInteger("value"));
		}, "data");
		
		nm.addTrigger((i, t) -> {
			received.put(i, t.getInteger("value") + 1);
		}, "data-plus-1");
		
		Packet p1 = new Packet("data");
		p1.setInteger("value", 3);
		nm.receive(5, p1.toString().getBytes(StandardCharsets.UTF_8));
		
		Packet p2 = new Packet("data-plus-1");
		p2.setInteger("value", 5);
		nm.receive(3, p2.toString().getBytes(StandardCharsets.UTF_8));
		
		assertTrue(received.containsKey(5));
		assertEquals(3, (int)received.get(5));
		assertTrue(received.containsKey(3));
		assertEquals(6, (int)received.get(3));
	}
	
	@Test(expected=RuntimeException.class)
	public void testNonexistentTrigger() {
		StandardServerManager nm = new StandardServerManager(new MockServer());
		Packet p1 = new Packet("data");
		nm.receive(3, p1.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoMultipleTriggers() {
		StandardServerManager nm = new StandardServerManager(new MockServer());

		
		nm.addTrigger((i, t) -> {
			System.out.println("d");
		}, "data");
		
		nm.addTrigger((i, t) -> {
			System.out.println("e");
		}, "data");
	}
}
