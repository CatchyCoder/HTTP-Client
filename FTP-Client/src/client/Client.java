package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
		
	private final String hostIP = "127.0.0.1";
	
	private static final Logger log = LogManager.getLogger(Client.class);
	
	public Client() {	
		try {		
			// Connect to the server
			connect();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() throws IOException {
		log.debug("Attemping connection with server[" + hostIP + "]...");
		
		Socket socket = null;
		boolean connected = false;
		
		// Make several attempts to connect to the server
		for(int attempts = 0; !connected && attempts < 15; attempts++) {
			try {
				// Connecting to the server using the IP address and port
				socket = new Socket(InetAddress.getByName(hostIP), 6789);
				connected = true;
			}
			catch(IOException e) {
				log.debug("Attempt: " + (attempts + 1));
				
				try {
					// Wait 1 second before attempting to connect again
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		if(socket == null) log.error("Connection timeout.");
		else {
			log.debug("Connected.");
			new Connection(socket);
		}
	}
}
