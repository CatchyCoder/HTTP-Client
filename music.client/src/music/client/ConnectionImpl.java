package music.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.AbstractConnection;

public class ConnectionImpl extends AbstractConnection implements Runnable {
	
	private static final Logger log = LogManager.getLogger(ConnectionImpl.class);
	
	public ConnectionImpl(Socket socket) {
		super(socket);
		
		// Start session
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		// Send file
		sendInt(3);
		sendFile("C://Users/owner1/Documents/test/test_File.mp3");
		
		// Disconnect
		sendInt(0);
		try {Thread.sleep(1000);}
		catch(Exception e){}
		disconnect();
	}
}