package teamproject.networking.socket;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket clientSocket = null;
	private DataInputStream in = null;
    private DataOutputStream out = null;
    private byte[] message = null;
    private int length;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
        	try {
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
			
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
	    	
			try {
				out.writeInt(message.length);
				out.write(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // write length of the message
			
            out.close();
            in.close();
            
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}