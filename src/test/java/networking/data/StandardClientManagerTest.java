package test.java.networking.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.Test;

import main.java.networking.StandardClientManager;
import main.java.networking.data.Packet;

public class StandardClientManagerTest {
	@Test
	public void testTriggersTriggered() {
		final ArrayList<Integer> received = new ArrayList<Integer>();

		final StandardClientManager nm = new StandardClientManager(new MockSocket(0));

		nm.setTrigger(t -> {
			received.add(t.getInteger("value"));
		});

		nm.setTrigger(t -> {
			received.add(t.getInteger("value") + 1);
		});

		final Packet p1 = new Packet("data");
		p1.setInteger("value", 3);
		nm.receive(p1.toString().getBytes(StandardCharsets.UTF_8));

		final Packet p2 = new Packet("data-plus-1");
		p2.setInteger("value", 5);
		nm.receive(p2.toString().getBytes(StandardCharsets.UTF_8));

		assertTrue(received.contains(4));
		assertTrue(received.contains(6));
		assertEquals(2, received.size());
	}

	@Test(expected = RuntimeException.class)
	public void testNonexistentTrigger() {
		final StandardClientManager nm = new StandardClientManager(new MockSocket(0));
		final Packet p1 = new Packet("data");
		nm.receive(p1.toString().getBytes(StandardCharsets.UTF_8));
	}
}
