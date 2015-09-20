package client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Runnable {

	private final Socket socket;
	
	// For simple communication with the client
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public Connection(Socket socket) {
		this.socket = socket;
		
		try {
			setupStreams();
			System.out.println("Setup is now finished.\n");
			
			// Using this class as a thread may not be used later,
			// the methods within this class may be called externally
			// from the clients GUI.
			new Thread(this).start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setupStreams() throws IOException {
		// Get input & output streams setup
		System.out.print("Setting up streams... ");
		
		output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
		input = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("Done.");
	}
		
	@Override
	public void run() {
		
		
		
		if(true) return;
		
		// Tell the server to send us it's list of songs
		send(0);
		
		try {
			System.out.print("Retrieving songs...");
			// Retrieve songs
			String[][] songs = (String[][]) input.readObject();
			System.out.println("Done.");
			
			// Displaying received songs
			for(int n = 0; n < songs.length; n++) {
				System.out.println(songs[n][0] + " " + songs[n][1] + " " + songs[n][2] + " " + songs[n][3]);
			}
			// Get a certain song
			send(1);
			// Say what song
			send(0);
			
			downloadFile();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		disconnect();
	}
	
	private void send(Object object) {
		try {
			// Send a message in the form of an object to the client
			output.writeObject(object);
			output.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void downloadFile() {
		// For reading the incoming file
		InputStream in = null;
		
		// For storing the incoming file (saving)
		FileOutputStream fOutput = null;
		BufferedOutputStream bOutput = null;
		
		try {
			System.out.println("Getting ready to recieve file...");
			
			// Setting up streams
			in = socket.getInputStream();
			fOutput = new FileOutputStream("C:/Users/Clay/Music/musicFile.mp3");
			bOutput = new BufferedOutputStream(fOutput);
			fOutput.flush();
			bOutput.flush();
			
			// Declaring buffer size
			int bufferSize = 1024 * 8;
			byte[] bytes = new byte[bufferSize];
			
			System.out.println("Recieving file:");
			
			// Reading from the input stream and saving to a file	
			for(int bytesRead; (bytesRead = in.read(bytes)) > -1;) {
				System.out.println("  " + bytesRead + " bytes received.");
				bOutput.write(bytes, 0, bytesRead);
			}
			
			System.out.println("File recieved!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close down all streams
			try {
				in.close();
				fOutput.close();
				bOutput.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		// Close streams and sockets
		System.out.print("Ending connection...");
		try {
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done.\nYou are no longer connected to the server.");
	}
}
