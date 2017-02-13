package teamproject.example;

import java.util.Scanner;

import teamproject.event.Event;
import teamproject.networking.ClientManager;
import teamproject.networking.StandardClientManager;
import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.ClientDispatcher;
import teamproject.networking.event.ClientTrigger;
import teamproject.networking.event.HandshakeTrigger;
import teamproject.networking.socket.Client;

public class ClientTest implements Runnable {
	public Event<InputListener, String> ie = new Event<>((i, s) -> i.onInput(s));
	
	public static void main(String... args) {
		new ClientTest().run();
	}

	@Override
	public void run() {
		Client client =  new Client("0.0.0.0");
		client.getDisconnectedEvent().addListener(new ClientDisconnectedListener() {
			@Override
			public void onClientDisconnected(int clientID) {
				System.out.println("client-disc");
				System.exit(0);
			}
		});
		ClientDispatcherT dispatcher = new ClientDispatcherT();
		ie.addListener(dispatcher);
		ClientManager manager = new StandardClientManager(client);
		manager.addTrigger(new ClientTrigger() {
			@Override
			public void trigger(Packet p) {
				System.out.println(p.getPacketName() + ": " + p.getString("input"));
			}
		}, "input-from-server");
		manager.addTrigger(new HandshakeTrigger(client), "server-handshake");
		dispatcher.getDispatchEvent().addListener(manager);
		client.start();
		
		try(Scanner s = new Scanner(System.in)) {
			while(true) {
				String l = s.nextLine();
				if(l.equals("quit")) {
					client.die();
				} else {
					ie.fire(l);
				}
			}
		}
	}
	
	public class ClientDispatcherT extends ClientDispatcher implements InputListener {
		@Override
		public void onInput(String s) {
			Packet p = new Packet("input-from-client");
			p.setString("input", s);
			getDispatchEvent().fire(p);
		}
	}
}
