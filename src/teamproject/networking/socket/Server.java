package teamproject.networking.socket;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Simeon Kostadinov
 *
 */

public class Server extends Thread{
	
	private ServerSocket serverSocket = null;
	private int serverPort = 8000;
	private boolean alive = true;
	private DataInputStream in = null;
    private DataOutputStream out = null;

    
	
	public Server(){
		// empty constructor
		this.serverPort = Port.number;
	}
	
	public void run(){
		
		openServerSocket();
		
		BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

	    while(alive){	
			
			Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(alive) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            
            try {
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    
            
         // objects ClientSender and ClientReceiver
    	    ServerSender sender = new ServerSender(out, queue);
    	    ServerReceiver receiver = new ServerReceiver(in, queue);

    	    // Run them in parallel:
    	    sender.start();
    	    receiver.start();
        }
        System.out.println("Server Stopped.") ;
	    		    	
	    }
	
		private void openServerSocket() {
	        try {
	            this.serverSocket = new ServerSocket(this.serverPort);
	        } catch (IOException e) {
	            throw new RuntimeException("Cannot open port " + this.serverPort + ":", e);
	        }
	    }
	}

class ServerSender extends Thread{
	private boolean alive = true;
	private DataOutputStream out = null;
	private BlockingQueue<byte[]> queue;
	
	public ServerSender(DataOutputStream out, BlockingQueue<byte[]> queue){
		this.out = out;
		this.queue = queue;
	}
	
	public void run(){
		while(alive){
			// send data
			
			byte[] message = null;
			
			try {
				message = queue.take();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				out.writeInt(message.length);
				out.write(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // write length of the message
			
		}
	}
}

class ServerReceiver extends Thread{
	private boolean alive = true;
	private DataInputStream in = null;
	private int length;
	private byte[] message;
	private BlockingQueue<byte[]> queue;
	
	public ServerReceiver(DataInputStream in, BlockingQueue<byte[]> queue){
		this.in = in;
		this.queue = queue;
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
			
			queue.offer(message);			
			
		}
	}
}

