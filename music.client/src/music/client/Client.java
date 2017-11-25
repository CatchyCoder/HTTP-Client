package music.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Storage;

public class Client {
	
	private static final Logger log = LogManager.getLogger(Client.class);
	
	public static Channel channel;
	
	//private final String hostIP = "63.155.32.96"; // Raspberry Pi public IP
	//private final String hostIP = "192.168.0.34"; // Raspberry Pi private IP
	private final String hostIP = "127.0.0.1";
	//private final String hostIP = "192.168.0.104";
	
	/* A single client connection is represented by a Channel. Each
	 * Channel utilizes three independent socket connections, each are on different
	 * servers (using different ports) that are dedicated to a specific use:
	 * 
	 * 1 ADD: client wishes to upload files to server database.
	 * 2 RETRIEVE: client wishes to download files from server database, or 
	 * 	retrieve library information from server database.
	 * 3 STREAM: client wishes to stream files from server database.
	 */
	
	// Loads client files into memory
	public static final Storage STORAGE = new Storage("C:/music_client", "/database", "/download");
	
	public void run() {
		// Create main gui
		new GUI();
		
		// Attempting to connect with server
		channel = connect();
		
	}
	
	/**
	 * Connects client application to server.
	 * @return a ClientConnectionImpl representation of the connection if
	 * successfully connected, null otherwise.
	 */
	protected Channel connect() {
		int port = 6501;
		
		Socket socket1 = null, socket2 = null, socket3 = null;
		boolean fullyConnected = false;
		
		// Make several attempts to connect to the server\
		log.debug("Attemping connections with server [" + hostIP + "] on ports " + port + " through " + (port + 2) + ".");
		for(int attempts = 0; !fullyConnected && attempts < 3; attempts++) {
			try {
				// Connecting to the server using the IP address and port
				if(socket1 == null) socket1 = new Socket(InetAddress.getByName(hostIP), port);
				if(socket2 == null) socket2 = new Socket(InetAddress.getByName(hostIP), port + 1);
				if(socket3 == null) socket3 = new Socket(InetAddress.getByName(hostIP), port + 2);
				if(socket1 != null && socket2 != null && socket3 != null) fullyConnected = true;
			}
			catch(IOException e) {
				log.debug("Attempt " + (attempts + 1) + " failed. " + e);
				
				try {
					// Wait a short period before attempting to connect again
					Thread.sleep(50);
				} catch (InterruptedException e2) {
					log.error(e2.getStackTrace(), e2);
				}
			}
		}
		
		if(socket1 == null || socket2 == null || socket3 == null) {
			log.error("Connection timeout. (One or more sockets is null)");
			return null;
		}
		else {
			log.debug("Fully Connected:");
			log.debug("ADD socket [Local: " + socket1.getLocalPort() + " Remote: " + socket1.getPort() + "]");
			log.debug("RETRIEVE socket [Local: " + socket2.getLocalPort() + " Remote: " + socket2.getPort() + "]");
			log.debug("STREAM socket [Local: " + socket3.getLocalPort() + " Remote: " + socket3.getPort() + "]");
			log.debug("Creating channel...");
			Channel channel = new Channel(socket1, socket2, socket3);
			log.debug("Done.");
			return channel;
		}
	}
}
