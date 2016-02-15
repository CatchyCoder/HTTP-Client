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
	
	private void randomWait() {
		int randTime = (int)(Math.random() * 3000);
		
		try {
			Thread.sleep(0);
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public void run() {
		// Read server's first ACK
		readACK();
		randomWait();
		// Download multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Download file
			long downStart = System.currentTimeMillis();
			readFile("C://Users/Clay-/Documents/music_client/download");
			System.out.println((System.currentTimeMillis() - downStart) / 1000.0 + " seconds to download.");
			randomWait();
		}
		
		// Upload multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Send file
			long upStart = System.currentTimeMillis();
			writeFile("C://Users/Clay-/Documents/music_client/send/test.mp3");
			System.out.println((System.currentTimeMillis() - upStart) / 1000.0 + " seconds to upload.");
			randomWait();
		}
		
		// Download AND Upload multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Download file
			long downStart = System.currentTimeMillis();
			readFile("C://Users/Clay-/Documents/music_client/download");
			System.out.println((System.currentTimeMillis() - downStart) / 1000.0 + " seconds to download.");
			
			randomWait();
			
			// Send file
			long upStart = System.currentTimeMillis();
			writeFile("C://Users/Clay-/Documents/music_client/send/test.mp3");
			System.out.println((System.currentTimeMillis() - upStart) / 1000.0 + " seconds to upload.");
			
			randomWait();
		}
		randomWait();
		
		// Disconnect
		disconnect();
	}
	
	@Override
	public synchronized void disconnect() {
		if(!isClosed()) {
			// Notify server that client is disconnecting
			writeInt(Message.DISCONNECT.ordinal());
			
			// Disconnect
			super.disconnect();
		}
	}
	
	@Override
	protected void readFile(String newPath) {
		// Notify server that client is downloading a file
		writeInt(Message.SERVER_UPLOAD_FILE.ordinal());
		
		// Download file
		super.readFile(newPath);
	}
	
	@Override
	protected void writeFile(String filePath) {
		// Notify server that client is sending a file
		writeInt(Message.CLIENT_UPLOAD_FILE.ordinal());
		
		// Send file
		super.writeFile(filePath);
	}
}