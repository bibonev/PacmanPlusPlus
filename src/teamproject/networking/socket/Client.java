/**
 * 
 */
package teamproject.networking.socket;

import teamproject.event.Event;
import teamproject.networking.NetworkSocket;
import teamproject.networking.NetworkSocketListener;
import java.net.*;
import java.io.*;

/**
 * @author Simeon Kostadinov
 *
 */

public class Client implements NetworkSocket {
	
	private Socket socket = null;
	private DataInputStream in = null;
    private DataOutputStream out = null;
    private String hostname = null;

	public Client(String hostname){
		this.hostname = hostname;
	}
	 
    public void start(){
	    try {
			socket = new Socket(hostname, Port.number);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // objects ClientSender and ClientReceiver
	    ClientSender sender = new ClientSender(out);
	    ClientReceiver receiver = new ClientReceiver(in);

	    // Run them in parallel:
	    sender.start();
	    receiver.start();
	    
	    // Wait for them to end and close sockets.
	    try {
	      sender.join();
	      out.close();
	      receiver.join();
	      in.close();
	      socket.close();
	    }
	    catch (IOException e) {
	      System.err.println("Something wrong " + e.getMessage());
	      System.exit(1); 
	    }
	    catch (InterruptedException e) {
	      System.err.println("Unexpected interruption " + e.getMessage());
	      System.exit(1); 
	    }	    
   }


	@Override
	public void send(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Event<NetworkSocketListener, byte[]> getReceiveEvent() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class ClientSender extends Thread{
	private boolean alive = true;
	private DataOutputStream out = null;
	
	public ClientSender(DataOutputStream out){
		this.out = out;
	}
	
	public void run(){
		while(alive){
			// send data
			
//			try {
//				out.writeInt(message.length);
//				out.write(message);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // write length of the message
			
		}
	}
}

class ClientReceiver extends Thread{
	private boolean alive = true;
	private DataInputStream in = null;
	private int length;
	private byte[] message;
	
	public ClientReceiver(DataInputStream in){
		this.in = in;
	}
	
	public void run(){
		while(alive){
			
			try {
				length = in.readInt();
				if(length>0) {
				    message = new byte[length];
				    in.readFully(message, 0, message.length);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			
		}
	}
}
