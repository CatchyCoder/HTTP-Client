package music.client;

import java.net.Socket;
import java.util.ArrayList;

import music.core.AbstractConnection;
import music.core.Track;
import music.core.binarytree.BinaryTree;

public abstract class ClientConnection extends AbstractConnection implements Runnable {

	public ClientConnection(Socket socket) {
		super(socket);
		
		// Read server's first ACK
		readACK();
		
		// Start session
		new Thread(this).start();
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
		writeInt(Message.DATABASE_RETRIEVE.ordinal());
		
		// Download file
		super.readFile(newPath);
	}
	
	@Override
	protected void writeFile(String filePath) {
		// Notify server that client is sending a file
		writeInt(Message.DATABASE_ADD.ordinal());
		
		// Send file
		super.writeFile(filePath);
	}
	
	@Override
	protected BinaryTree readTree() {
		// Notify server that client is downloading tree
		writeInt(Message.LIBRARY.ordinal());
		
		// Read BinaryTree
		return super.readTree();
	}
}
