package client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection implements Runnable {

	private final Socket socket;
	
	private static final Logger log = LogManager.getLogger(Connection.class);
	
	private boolean gotFile = false;
	
	public Connection(Socket socket) {
		this.socket = socket;
		
		// Using this class as a thread may not be used later,
		// the methods within this class may be called externally
		// from the clients GUI.
		Thread thread = new Thread(this);
		log.debug("Setup for [" + socket.getInetAddress() + "] complete. Thread [" + thread.getName() + "] will now start.");
		thread.start();
	}
	
	@Override
	public void run() {
		
		while(true) {
			// Tell the server to send a test object
			sendObject(0);
			
			/*
			 * The sockets input stream is not intended to be closed, that's why
			 * it's okay to embed the input stream object in the constructor - that way
			 * the close() method for the socket's input stream isn't called.
			 */
			try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
				// Read command from server
				Object object = input.readObject();
				if(object instanceof Integer) {
					log.debug("Recieved test object from server. Object reads: " + ((Integer)object).intValue());
				} else {
					// Something went wrong with the server's message
					log.error("Command from server is not an Integer. " + object + " received.");
				}
			} catch (IOException | ClassNotFoundException e) {
				log.error(e.getStackTrace());
			}
			
			if(!gotFile) {
				sendObject(1);
				gotFile = downloadFile("/home/clay/" + String.valueOf(((int)(Math.random() * 10 + 1))) + ".mp3");
			}
		}
	}
	
	private void sendObject(Object object) {
		log.debug("Setting up Object transfer streams...");
		/*
		 * The sockets output stream is not intended to be closed, that's why
		 * it's okay to embed the output stream object in the constructor - that way
		 * the close() method for the socket's output stream isn't called.
		 */
		OutputStream s = null;
		try {
			s = socket.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try(ObjectOutputStream output = new ObjectOutputStream(s)) {
			output.flush();
			log.debug("Done.");
			
			// Send a message in the form of an object to the client
			log.debug("Sending " + object);
			output.writeObject(object);
			output.flush();
			log.debug("Done.");
			log.debug("STATE A: " + socket.isClosed());
		} catch (IOException e) {
			log.error(e.getStackTrace(), e);
		}
		log.debug("STATE B: " + socket.isClosed());
	}
	
	private boolean downloadFile(final String filePath) {
		log.debug("Setting up file transfer streams...");
		try(
			// For storing the incoming file (saving)
			FileOutputStream fOutput = new FileOutputStream(filePath);
			BufferedOutputStream bOutput = new BufferedOutputStream(fOutput)
		) {
			fOutput.flush();
			bOutput.flush();
			
			// For reading the incoming file
			InputStream input = socket.getInputStream();
			
			log.debug("Done.");
			
			// Declaring buffer size
			int bufferSize = 1024 * 8;
			byte[] bytes = new byte[bufferSize];
			
			log.debug("Downloading file...");
			
			// Reading from the input stream and saving to a file	
			for(int bytesRead; (bytesRead = input.read(bytes)) > -1;) {
				log.debug(bytesRead + " bytes received.");
				bOutput.write(bytes, 0, bytesRead);
			}
			bOutput.flush();
			
			log.debug("Done.");
		} catch (IOException e) {
			log.error(e.getStackTrace());
			return false;
		}
		log.debug("try-with-resources block executed. Streams should be closed.");
		return true;
	}
	
	public void disconnect() {
		log.debug("Ending connection...");
		try {
			// Close the socket and its associated input/output streams
			socket.close();
		}
		catch(IOException e) {
			// Ignoring exception to close quietly
		}
		log.debug("Done.");
	}
}