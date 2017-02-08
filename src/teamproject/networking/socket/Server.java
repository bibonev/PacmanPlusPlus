package teamproject.networking.socket;

import java.io.*;
import java.net.*;

/**
 * @author Simeon Kostadinov
 *
 */

public class Server extends Thread{
	
	private ServerSocket serverSocket = null;
	private Thread runningThread= null;
	private int serverPort = 8000;
	private boolean alive = true;
	private Socket socket = null;

    
	
	public Server(){
		// empty constructor
		this.serverPort = Port.number;
	}
	
	public void run(){
		synchronized(this){
            this.runningThread = Thread.currentThread();
        }
		openServerSocket();
	    
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
            new Thread(
                new ServerThread(
                    clientSocket)
            ).start();
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
