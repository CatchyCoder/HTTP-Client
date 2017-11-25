package music.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Command;
import music.core.Connection;

public class Channel {
	
	private static final Logger log = LogManager.getLogger(Channel.class);

	/* A single client connection is represented by a Channel. Each
	 * Channel utilizes three independent socket connections, each are on different
	 * servers (using different ports) that are dedicated to a specific use:
	 * 
	 * 1 ADD: client wishes to upload files to server database.
	 * 2 RETRIEVE: client wishes to download files from server database, or 
	 * 	retrieve library information from server database.
	 * 3 STREAM: client wishes to stream files from server database.
	 * 
	 * Each socket will be running on a separate Thread (located in ServerChannel),
	 * allowing each client to upload, download, and stream simultaneously.
	 */
	private final Connection add, retrieve, stream;
	
	public Channel(Socket addSocket, Socket retrieveSocket, Socket streamSocket) {
		// Create the Connection object for each socket. This will configure I/O streams.
		add = new Connection(addSocket);
		retrieve = new Connection(retrieveSocket);
		stream = new Connection(streamSocket);
		
		// Read acknowledgement command from each socket. ACK command means the server is ready
		// for instructions.
		add.readACK();
		retrieve.readACK();
		stream.readACK();
		
		log.debug("Channel setup for " + addSocket.getInetAddress().getHostAddress() + " complete.");
	}
	
	public void retrieveFile(String file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retrieve.writeCommand(Command.DATABASE_RETRIEVE);
				retrieve.readFile(Client.STORAGE.getDownloadPath());
			}
		}).start();
	}
	
	public void streamFile(String file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retrieve.writeCommand(Command.DATABASE_STREAM);
				stream.stream();
			}
		}).start();
	}
	
	public void setStreamIndex(final int index) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				stream.writeInt(index);
			}
		}).start();
	}
	
	public void addFile(final String path) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retrieve.writeCommand(Command.DATABASE_ADD);
				add.writeFile(path, false);
			}
		}).start();
	}
	
	public void readTree() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retrieve.writeCommand(Command.DATABASE_ADD);
				add.writeFile(path, false);
			}
		}).start();
	}
}
