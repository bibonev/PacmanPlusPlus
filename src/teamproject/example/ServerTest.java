package teamproject.example;

import java.util.Scanner;

import teamproject.Pair;
import teamproject.event.Event;
import teamproject.networking.ServerManager;
import teamproject.networking.StandardServerManager;
import teamproject.networking.data.Packet;
import teamproject.networking.event.ClientConnectedListener;
import teamproject.networking.event.ClientDisconnectedListener;
import teamproject.networking.event.ServerDispatcher;
import teamproject.networking.event.ServerTrigger;
import teamproject.networking.socket.Server;

public class ServerTest implements Runnable {
	public Event<InputListener, String> ie = new Event<>((i, s) -> i.onInput(s));
	
	public static void main(String... args) {
		new ServerTest().run();
	}

	@Override
	public void run() {
		Server server = new Server();
		server.getClientDisconnectedEvent().addListener(new ClientDisconnectedListener() {
			@Override
			public void onClientDisconnected(int clientID) {
				System.out.println("disc " + clientID);
				System.exit(0);
			}
		});
		server.getClientConnectedEvent().addListener(new ClientConnectedListener() {
			@Override
			public void onClientConnected(int clientID) {
				System.out.println("conn " + clientID);
			}
		});
		ServerDispatcherT dispatcher = new ServerDispatcherT();
		ie.addListener(dispatcher);
		server.getClientConnectedEvent().addListener(dispatcher);
		ServerManager manager = new StandardServerManager(server);
		manager.addTrigger(new ServerTrigger() {
			@Override
			public void trigger(int clientID, Packet p) {
				System.out.println(p.getPacketName() + "(" + clientID + "): " + p.getString("input"));
			}
		}, "input-from-client");
		dispatcher.getDispatchEvent().addListener(manager);
		server.start();

		try(Scanner s = new Scanner(System.in)) {
			while(true) {
				String l = s.nextLine();
				if(l.equals("quit")) {
					server.die();
				} else {
					ie.fire(l);
				}
			}
		}
	}
	
	public class ServerDispatcherT extends ServerDispatcher implements InputListener {
		@Override
		public void onInput(String s) {
			Packet p = new Packet("input-from-server");
			p.setString("input", s);
			getDispatchEvent().fire(new Pair<>(0, p));
		}
	}
}
