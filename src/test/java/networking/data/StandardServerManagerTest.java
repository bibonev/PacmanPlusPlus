package test.java.networking.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.junit.Test;

import main.java.networking.StandardServerManager;
import main.java.networking.data.Packet;

public class StandardServerManagerTest {
	@Test
	public void testTriggersTriggered() {
		final HashMap<Integer, Integer> received = new HashMap<Integer, Integer>();

		final StandardServerManager nm = new StandardServerManager(new MockServer());

		nm.setTrigger((i, t) -> {
			received.put(i, t.getInteger("value"));
		});

		nm.setTrigger((i, t) -> {
			received.put(i, t.getInteger("value") + 1);
		});

		final Packet p1 = new Packet("data");
		p1.setInteger("value", 3);
		nm.receive(5, p1.toString().getBytes(StandardCharsets.UTF_8));

		final Packet p2 = new Packet("data-plus-1");
		p2.setInteger("value", 5);
		nm.receive(3, p2.toString().getBytes(StandardCharsets.UTF_8));

		assertTrue(received.containsKey(5));
		assertEquals(4, (int) received.get(5));
		assertTrue(received.containsKey(3));
		assertEquals(6, (int) received.get(3));
	}

	@Test(expected = RuntimeException.class)
	public void testNonexistentTrigger() {
		final StandardServerManager nm = new StandardServerManager(new MockServer());
		final Packet p1 = new Packet("data");
		nm.receive(3, p1.toString().getBytes(StandardCharsets.UTF_8));
	}
}
