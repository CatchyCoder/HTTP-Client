package client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection implements Runnable {

	private final Socket socket;
	
	// For simple communication with the client
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private static final Logger log = LogManager.getLogger(Connection.class);
	
	private boolean gotFile = false;
	
	public Connection(Socket socket) {
		this.socket = socket;
		
		try {
			setupStreams();
			log.debug("Setup is now finished.\n");
			
			// Using this class as a thread may not be used later,
			// the methods within this class may be called externally
			// from the clients GUI.
			new Thread(this).start();
			
		}
		catch(IOException e) {
			log.error(e);
		}
	}
	
	private void setupStreams() throws IOException {
		// Get input & output streams setup
		log.debug("Setting up streams... ");
		
		output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
		input = new ObjectInputStream(socket.getInputStream());
		
		log.debug("Done.");
	}
	
	@Override
	public void run() {
		
		while(true) {
			// Tell the server to send a test object
			send(0);
			try {
				Integer integer = (Integer) input.readObject();
				log.debug("Recieved test object from server.\nObject reads: " + integer.intValue());
			} catch (ClassNotFoundException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
			
			send(1);
			if(!gotFile) gotFile = downloadFile();
		}
		
		/*if(true)return;
		
		try {
			log.debug("Retrieving songs...");
			// Retrieve songs
			String[][] songs = (String[][]) input.readObject();
			log.debug("Done.");
			
			// Displaying received songs
			for(int n = 0; n < songs.length; n++) {
				log.debug(songs[n][0] + " " + songs[n][1] + " " + songs[n][2] + " " + songs[n][3]);
			}
			// Get a certain song
			send(1);
			// Say what song
			send(0);
			
			downloadFile();
			
		} catch (ClassNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		
		disconnect();*/
	}
	
	private void send(Object object) {
		try {
			// Send a message in the form of an object to the client
			output.writeObject(object);
			output.flush();
		}
		catch(IOException e) {
			log.error(e);
		}
	}
	
	private boolean downloadFile() {
		// For reading the incoming file
		InputStream in = null;
		
		// For storing the incoming file (saving)
		FileOutputStream fOutput = null;
		BufferedOutputStream bOutput = null;
		
		try {
			log.debug("Getting ready to recieve file...");
			
			// Setting up streams
			in = socket.getInputStream();
			
			fOutput = new FileOutputStream("C:/Users/owner1/Music/" + String.valueOf(((int)(Math.random() * 10 + 1))) + ".mp3");
			bOutput = new BufferedOutputStream(fOutput);
			fOutput.flush();
			bOutput.flush();
			
			// Declaring buffer size
			int bufferSize = 1024 * 8;
			byte[] bytes = new byte[bufferSize];
			
			log.debug("Recieving file:");
			
			// Reading from the input stream and saving to a file	
			for(int bytesRead; (bytesRead = in.read(bytes)) > -1;) {
				log.debug("  " + bytesRead + " bytes received.");
				bOutput.write(bytes, 0, bytesRead);
			}
			
			log.debug("File recieved!");
			return true;
		} catch (IOException e) {
			log.error(e);
		} finally {
			// Close down all streams
			try {
				if(in != null) in.close();
				if(fOutput != null) fOutput.close();
				if(bOutput != null) bOutput.close();
			}
			catch(IOException e) {
				log.error(e);
			}
		}
		return false;
	}
	
	public void disconnect() {
		log.debug("Ending connection...");
		try {
			// Close the socket and its associated input/output streams
			socket.close();
		}
		catch(IOException e) {
			log.error(e);
		}
		log.debug("Done.");
		log.debug("You are no longer connected to the server.");
	}
}
