package client;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection implements Runnable {
	
	private static final Logger log = LogManager.getLogger(Connection.class);

	private final Socket socket;
	
	// List for additional input and output streams that are used (besides the basic InputStream/OutputStream).
	// Storing them in a list and closing them in a loop ensures all streams have been closed.
	private final ArrayList<InputStream> inStreams = new ArrayList<InputStream>();
	private final ArrayList<OutputStream> outStreams = new ArrayList<OutputStream>();
	
	private boolean gotFile = false;
	
	public Connection(Socket socket) {
		this.socket = socket;
		
		try {
			/*
			 * Input streams block until their corresponding output streams have "written and flushed the header".
			 * Therefore output streams are setup first, and then the input streams
			 */
			// Output streams setup
			log.debug("Setting up output streams...");
			OutputStream out = socket.getOutputStream();
			out.flush();
			ObjectOutputStream oOutput = new ObjectOutputStream(out);
			oOutput.flush();
			DataOutputStream dOutput = new DataOutputStream(out);
			dOutput.flush();
			outStreams.add(oOutput);
			outStreams.add(dOutput);
			log.debug("Done.");
			
			// Input streams setup
			log.debug("Setting up input streams...");
			InputStream in = socket.getInputStream();
			inStreams.add(new ObjectInputStream(in));
			inStreams.add(new DataInputStream(in));
			log.debug("Done.");
			
		} catch (IOException e) {
			log.error("Failed to set up streams with the server. Disconnecting.", e);
			disconnect();
			return;
		}
		
		// Using this class as a thread may not be used later,
		// the methods within this class may be called externally
		// from the clients GUI.
		Thread thread = new Thread(this);
		log.debug("Setup for [" + socket.getInetAddress() + "] complete. Thread [" + thread.getName() + "] will now start.");
		thread.start();
	}
	
	private InputStream getInput(Class<?> classType) {
		for(InputStream stream: inStreams) {
			if(stream.getClass() == classType) return stream;
		}
		log.error("Input stream " + classType + " could not be found");
		return null;
	}
	
	private OutputStream getOutput(Class<?> classType) {
		for(OutputStream stream: outStreams) {
			if(stream.getClass() == classType) return stream;
		}
		log.error("Output stream " + classType + " could not be found");
		return null;
	}
	
	@Override
	public void run() {
		
		while(true) {
			// Tell the server to send a test object
			sendInt(0);
			
			// Read integer from server
			try {
				int serverCommand = ((DataInputStream) getInput(DataInputStream.class)).readInt();
				log.debug("Test integer from server: " + serverCommand);
			} catch (IOException e) {
				log.error("Failed to read test integer from server.", e);
				break;
			}
			
			// Download file if not already downloaded
			if(!gotFile) {
				sendInt(1);
				gotFile = downloadFile("/home/clay/" + String.valueOf(System.currentTimeMillis()) + ".mp3");
				try {
					Thread.sleep(5000);
				} catch(Exception e) {
					log.error("", e);
				}
			}
		}
	}
	
	private void sendInt(int n) {
		final DataOutputStream out = (DataOutputStream) getOutput(DataOutputStream.class);
		try {
			out.flush();
			
			// Send a message in the form of an object to the server
			log.debug("Sending integer: " + n);
			out.writeInt(n);
			out.flush();
			log.debug("Done.");
		} catch (IOException e) {
			log.error("Error sending integer [" + n + "] .", e);
		}
	}
	
	private boolean downloadFile(final String filePath) {
		log.debug("Setting up file streams (for storing file)...");
		try(
			// For storing the incoming file (saving)
			FileOutputStream fOutput = new FileOutputStream(filePath);
			BufferedOutputStream bOutput = new BufferedOutputStream(fOutput)
		) {
			fOutput.flush();
			bOutput.flush();
			
			log.debug("Done.");
			
			// For reading the incoming file
			InputStream input = socket.getInputStream();
			
			// Declaring buffer size
			int bufferSize = 1024 * 8;
			byte[] bytes = new byte[bufferSize];
			
			// Getting file size from server
			long fileSize;
			DataInputStream dInput = (DataInputStream) getInput(DataInputStream.class);
			if((fileSize = dInput.readLong()) == -1) {
				log.error("Error getting file size from server.");
				return false;
			}
			
			log.debug("Downloading " + (fileSize / 1000.0) + "kb file...");
			log.debug("==============================================");
			
			int bytesReceived = 0;
			// Reading from the input stream and saving to a file	
			for(int bytesRead; bytesReceived < fileSize && (bytesRead = input.read(bytes)) >= 0;) {
				bOutput.write(bytes, 0, bytesRead);
				bytesReceived += bytesRead;
				log.debug("Got " + bytesRead + " bytes [" + bytesReceived + " of " + fileSize + " bytes received].");
			}
			bOutput.flush();
			fOutput.flush();
			
			log.debug("==============================================");
			log.debug("Done.");
		} catch (IOException e) {
			log.error("IO error.", e);
			return false;
		}
		log.debug("try-with-resources block executed. File streams should be closed.");
		return true;
	}
	
	public void disconnect() {
		log.debug("Closing input streams...");
		for(InputStream stream: inStreams) {
			try {
				stream.close();
				log.debug("Done.");
			} catch (IOException e) {
				log.error("Error closing input stream with server.", e);
			}
		}
		
		log.debug("Closing output streams...");
		for(OutputStream stream: outStreams) {
			try {
				stream.close();
				log.debug("Done.");
			} catch (IOException e) {
				log.error("Error closing output stream with server.", e);
			}
		}
		
		log.debug("Ending connection...");
		try {
			// Close the socket and its associated input/output streams
			socket.close();
			log.debug("Done.");
		}
		catch(IOException e) {
			log.error("Error closing socket with server.", e);
		}
	}
}