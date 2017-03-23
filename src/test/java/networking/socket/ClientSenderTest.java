package test.java.networking.socket;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.junit.Test;

import main.java.networking.socket.ClientSender;

public class ClientSenderTest {
	@Test
	public void testClientSenderSendsPackets() throws InterruptedException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ClientSender sender = new ClientSender(new DataOutputStream(data));
		
		Thread senderThread = new Thread(sender);
		senderThread.start();
		sender.send(new byte[] {10, 50});
		sender.send(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
		
		byte[] bigPacket = new byte[16 * 16];
		for(int i = 0; i < bigPacket.length; i++)
			bigPacket[i] = (byte)(i % 16);
		
		sender.send(bigPacket);
		senderThread.join(100);
		sender.die();
		
		byte[] actualData = data.toByteArray();
	
		byte[] expectedData = {
				0, 0, 0, 2,
				10, 50,
				
				0, 0, 0, 10,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
				
				0, 0, 1, 0,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
		};
		
		assertArrayEquals(expectedData, actualData);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testClientSenderDoesntSendZeroLengthPackets() throws InterruptedException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ClientSender sender = new ClientSender(new DataOutputStream(data));
		
		Thread senderThread = new Thread(sender);
		senderThread.start();
		sender.send(new byte[0]);
		sender.send(new byte[] { 1, 2, 3}); // should never do this
		senderThread.join(100);
		sender.die();
		
		byte[] actualData = data.toByteArray();
		// should not reach here
	}
}
