package music.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.binarytree.BinaryTree;

public class ClientConnectionImpl extends ClientConnection {
	
	private static final Logger log = LogManager.getLogger(ClientConnectionImpl.class);
	
	public ClientConnectionImpl(Socket socket) {
		super(socket);
	}
	
	@Override
	public void run() {
		// Disconnect
		//disconnect();
	}
	
	public BinaryTree updateTree() {
		// Read binary tree from server
		return readTree();
	}
	
	private void test() {
		// Download multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Download file
			long downStart = System.currentTimeMillis();
			readFile("C://Users/Clay-/Documents/music_client/download");
			System.out.println((System.currentTimeMillis() - downStart) / 1000.0 + " seconds to download.");
		}
		
		// Upload multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Send file
			long upStart = System.currentTimeMillis();
			writeFile("C://Users/Clay-/Documents/music_client/send/test.mp3");
			System.out.println((System.currentTimeMillis() - upStart) / 1000.0 + " seconds to upload.");
		}
		
		// Download AND Upload multiple times
		for(int n = 0; !isClosed() && n < 3; n++) {
			// Download file
			long downStart = System.currentTimeMillis();
			readFile("C://Users/Clay-/Documents/music_client/download");
			System.out.println((System.currentTimeMillis() - downStart) / 1000.0 + " seconds to download.");
						
			// Send file
			long upStart = System.currentTimeMillis();
			writeFile("C://Users/Clay-/Documents/music_client/send/test.mp3");
			System.out.println((System.currentTimeMillis() - upStart) / 1000.0 + " seconds to upload.");
		}
	}
}