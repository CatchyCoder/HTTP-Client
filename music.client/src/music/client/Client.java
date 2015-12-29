package music.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
	
	private final String hostIP = "65.129.41.219"; // Raspberry Pi public IP
	//private final String hostIP = "192.168.0.34"; // Raspberry Pi private IP
	//private final String hostIP = "127.0.0.1";
	
	private static final Logger log = LogManager.getLogger(Client.class);
	
	public Client() {
		// Connect to the server
		connect();
	}
	
	public void connect() {
		log.debug("Attemping connection with server [" + hostIP + "]...");
		
		Socket socket = null;
		boolean connected = false;
		
		// Make several attempts to connect to the server
		for(int attempts = 0; !connected && attempts < 15; attempts++) {
			try {
				// Connecting to the server using the IP address and port
				socket = new Socket(InetAddress.getByName(hostIP), 6501);
				log.debug("Local (Ephemeral) port: " + socket.getLocalPort());
				log.debug("Remote port: " + socket.getPort());
				connected = true;
			}
			catch(IOException e) {
				log.debug("Attempt " + (attempts + 1) + " failed. " + e);
				
				try {
					// Wait 1 second before attempting to connect again
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					log.error(e2.getStackTrace(), e2);
				}
			}
		}
		
		if(socket == null) log.error("Connection timeout.");
		else {
			log.debug("Connected.");
			new ConnectionImpl(socket);
		}
	}
}
