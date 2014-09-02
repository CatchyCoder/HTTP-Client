package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	
	private Connection connection;
	
	private final String hostIP = "127.0.0.1";
	
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
		System.out.print("Attemping connection with server... ");
		
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
				if(attempts == 0) System.out.println();
				System.out.print(".");
			}
		}
		
		if(socket == null) System.err.println(" Connection timeout.");
		else {
			System.out.println(" Connected.");
			connection = new Connection(socket);
		}
	}
}
