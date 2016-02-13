package music.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.AbstractConnection;

public class ConnectionImpl extends AbstractConnection implements Runnable {
	
	private static final Logger log = LogManager.getLogger(ConnectionImpl.class);
	
	/*
	 * TODO: Have methods like download/send file use the sendInt(); method
	 * that way it doesn't have to be done manually, this could maybe ensure
	 * that the server/client got the correct message (match it against the send integer)
	 */
	
	public ConnectionImpl(Socket socket) {
		super(socket);
		
		// Start session
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println("===============\n\n");
			// Download file
//			sendInt(2);
//			long downStart = System.currentTimeMillis();
//			downloadFile("C://Users/Clay-/Documents/music_client/download");
//			System.out.println((System.currentTimeMillis() - downStart) / 1000.0 + " seconds to download.");
			
			try {Thread.sleep(10000);}
			catch(Exception e){}
			
			// Send file
			sendInt(3);
			long upStart = System.currentTimeMillis();
			sendFile("C://Users/Clay-/Documents/music_client/send/test.mp3");
			System.out.println((System.currentTimeMillis() - upStart) / 1000.0 + " seconds to upload.");
		}
		
		
		/*
		 * WORKS: 1000
		 * FAILS: 700
		 */
		
		
		
		
		// Disconnect
//		sendInt(0);
//		try {Thread.sleep(1000);}
//		catch(Exception e){}
//		disconnect();
	}
}