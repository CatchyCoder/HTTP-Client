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
		//sendInt(2); // 2 is the OLD protocol
		//sendFile("C://Users/Clay-/Documents/test/test_File.mp3");
		
		// Download file
		sendInt(1); // OLD PROTOCOL, not 1,3,2  maybe 0??
		downloadFile("C://Users/Clay-/Documents/music_download/test_File.mp3");
		
		// Disconnect
		//sendInt(0);
		try {Thread.sleep(1500);}
		catch(Exception e){}
		disconnect();
	}
}