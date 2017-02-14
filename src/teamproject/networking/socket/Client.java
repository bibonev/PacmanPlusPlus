/**
 * 
 */
package teamproject.networking.socket;

import teamproject.event.Event;
import teamproject.networking.NetworkSocket;
import teamproject.networking.NetworkListener;
import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.HandshakeListener;

import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.io.*;

/**
 * Represents connection between client and server. Handles sending and receiving 
 * data to and from the server and calling the particular events
 * @author Simeon Kostadinov
 */

public class Client implements NetworkSocket, HandshakeListener, Runnable {
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private String hostname = null;
	private ClientSender sender = null;
	private ClientReceiver receiver = null;
	private Event<NetworkListener, byte[]> receiveEvent;
	private int clientID = -1;
	private boolean serverSide;
	private Event<ClientDisconnectedListener, Integer> disconnectedEvent;

	/**
	 * Initialise Client object using a hostname
	 */
	public Client(String hostname) {
		this();
		this.hostname = hostname;
		this.serverSide = false;
	}

	/**
	 * Initialise Client object using the created socket and clientID
	 */
	public Client(Socket socket, int clientID) {
		this();
		this.socket = socket;
		this.clientID = clientID;
		this.serverSide = true;
	}

	private Client() {
		this.receiveEvent = new Event<>((l, b) -> l.receive(b));
		this.disconnectedEvent = new Event<>((l, i) -> l.onClientDisconnected(i));
	}
	
	/**
	 * Method for killing a particular client and closing the socket
	 */
	public void die() {
		try {
			sender.die();
			receiver.die();
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException("Could not close client.", e);
		}
	}
	
	/**
	 * Starting the thread and establishing the InputStream and OutputStream connections
	 */
	public void start() {
		try {
			if (socket == null) {
				socket = new Socket(hostname, Port.number);
			}

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			throw new RuntimeException("Unknown host.", e);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't get input and output streams for client.", e);
		}

		// objects ClientSender and ClientReceiver
		sender = new ClientSender(out);
		receiver = new ClientReceiver(in, b -> receiveEvent.fire(b));

		new Thread(this).start();
	}

	/**
	 * Start both sender and receiver and fire disconnect event when they are closing
	 */
	@Override
	public void run() {
		// Run them in parallel:
		sender.start();
		receiver.start();

		// Wait for them to end and close sockets.
		try {
			receiver.join();
			in.close();
			sender.die();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Something wrong " + e.getMessage());
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Unexpected interruption " + e.getMessage());
			System.exit(1);
		} finally {
			disconnectedEvent.fire(clientID);
			disconnectedEvent.clearListeners();
			receiveEvent.clearListeners();
		}
	}

	@Override
	public void send(byte[] data) {
		sender.send(data);
	}

	public int getClientID() {
		if (this.clientID == -1) {
			throw new RuntimeException("Not yet received client ID from server.");
		} else {
			return this.clientID;
		}
	}

	@Override
	public Event<NetworkListener, byte[]> getReceiveEvent() {
		return receiveEvent;
	}
	
	public Event<ClientDisconnectedListener, Integer> getDisconnectedEvent() {
		return disconnectedEvent;
	}

	@Override
	public void onServerHandshake(int clientID) {
		if (!serverSide) {
			this.clientID = clientID;
		} else {
			throw new RuntimeException("Server should not receive handshake packet.");
		}
	}

}

/**
 * ClientSender thread is used to send a packet byte array to the server
 */
class ClientSender extends Thread {
	private boolean alive = true;
	private DataOutputStream out = null;
	private BlockingQueue<byte[]> packets;

	public ClientSender(DataOutputStream out) {
		this.out = out;
		packets = new LinkedBlockingQueue<byte[]>();
	}

	public void run() {
		try {
			while (alive) {
				byte[] packet = packets.take();
				if(packet.length > 0) {
					out.writeInt(packet.length);
					out.write(packet);
				}
			}
		} catch(EOFException e) {
			// connection dropped
			return;
		} catch (IOException e) {
			if (alive) {
				throw new RuntimeException("Could not send packet.", e);
			} else {
				// connection already dropped, just close thread
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("Client thread interrupted.", e);
		} finally {
			alive = false;
		}
	}

	public void die() {
		alive = false;
		packets.add(new byte[0]);
	}

	public void send(byte[] packet) {
		if (alive) {
			packets.add(packet);
		}
	}
}

/**
 * ClientReceiver thread is used to recieve the packet byte array from the server
 */
class ClientReceiver extends Thread {
	private boolean alive = true;
	private DataInputStream in = null;
	private Consumer<byte[]> onReceive;

	public ClientReceiver(DataInputStream in, Consumer<byte[]> onReceive) {
		this.in = in;
		this.onReceive = onReceive;
	}

	public void run() {
		while (alive) {
			try {
				int length = in.readInt();

				if (length > 0) {
					byte[] message = new byte[length];
					in.read(message);
					this.onReceive.accept(message);
				}
			} catch(EOFException e) {
				System.out.println("disconnected!");
				return;
			} catch (IOException e) {
				if (alive) {
					throw new RuntimeException(e);
				} else {
					// connection already ended, just close thread
				}
			}
		}
	}

	public void die() {
		alive = false;
	}
}
