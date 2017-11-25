package music.client;

import java.net.Socket;

import music.core.Connection;
import music.core.binarytree.BinaryTree;

public abstract class ClientConnection extends Connection {

	public ClientConnection(Socket socket) {
		super(socket);
		
		// Read server's first ACK
		readACK();
	}
	
	@Override
	public synchronized void disconnect() {
		if(!isClosed()) {
			// Below code caused a loop of disconnect()
			// Notify server that client is disconnecting
			//writeInt(Message.DISCONNECT.ordinal());
			
			// Disconnect
			super.disconnect();
		}
	}
	
	@Override
	protected void readFile(String newPath) {
		// Notify server that client is downloading a file
		writeCommand(Message.DATABASE_RETRIEVE.ordinal());
		
		// Download file
		super.readFile(newPath);
	}
	
	@Override
	protected void stream() {
		// Notify server that client is streaming a file
		writeCommand(Message.DATABASE_STREAM.ordinal());
		
		// Stream file
		super.stream();
	}
	
	@Override
	protected void writeFile(String filePath, boolean streaming) {
		// Notify server that client is sending a file
		writeCommand(Message.DATABASE_ADD.ordinal());
		
		// Send file
		super.writeFile(filePath, streaming);
	}
	
	protected BinaryTree readTree() {
		// Notify server that client is downloading tree
		writeCommand(Message.LIBRARY.ordinal());
		
		// Read BinaryTree
		return (BinaryTree) super.readObject();
	}
}
