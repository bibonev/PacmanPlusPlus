package test.java.networking.socket;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import org.junit.Test;

import main.java.networking.socket.ClientReceiver;

public class ClientReceiverTest {
	@Test
	public void testClientReceiverReceivesPackets() {
		byte[] data = {
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
		int[] counter = {0};
		
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
		ClientReceiver reciever = new ClientReceiver(stream, p -> {
			int packet = ++counter[0];
			
			if(packet == 1) {
				assertEquals(2, p.length);
				assertEquals(10, p[0]);
				assertEquals(50, p[1]);
			} else if(packet == 2) {
				assertEquals(10, p.length);
				for(int i = 0; i < p.length; i++) {
					assertEquals(i, p[i]);
				}
			} else if(packet == 3) {
				assertEquals(16 * 16, p.length);
				for(int j = 0; j < 16; j++) {
					for(int i = 0; i < 16; i++) {
						assertEquals(i, p[j * 16 + i]);
					}
				}
			}
		});
		
		reciever.run();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testClientWontReceivePacketsOfLengthZero() {
		byte[] data = {
				0, 0, 0, 0,
				
				0, 0, 0, 10,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9
		};
		int[] counter = {0};
		
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
		ClientReceiver reciever = new ClientReceiver(stream, p -> {
			int packet = ++counter[0];
			
			if(packet == 1) {
				assertEquals(0, p.length);
			} else if(packet == 2) {
				assertEquals(10, p.length);
				for(int i = 0; i < p.length; i++) {
					assertEquals(i, p[i]);
				}
			}
		});
		
		reciever.run();
	}
}
