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
			new Thread(this).start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setupStreams() throws IOException {
		// Get streams to send/receive data
		System.out.print("Setting up streams... ");
		
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		output.flush();
		
		System.out.println("Done.");
	}
		
	@Override
	public void run() {
		
		
		downloadFile();
		disconnect();
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
			// Send a message to the client
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
			
			in = socket.getInputStream();
			
			fOutput = new FileOutputStream("C:/Users/Clay/Music/musicFile.mp3");
			bOutput = new BufferedOutputStream(fOutput);
			fOutput.flush();
			bOutput.flush();
			
			int bufferSize = 1024 * 8;
			
			byte[] bytes = new byte[bufferSize];
			
			System.out.println("Recieving file...");
			
			// Reading from the input stream and saving to a file			
			for(int count; (count = in.read(bytes)) >= 0;) {
				System.out.println(count + " bytes received.");
				bOutput.write(bytes, 0, count);
			}
			
			System.out.println("File recieved!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close down streams
			try {
				in.close();
				bOutput.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		// Close streams and sockets
		System.out.println("Ending connection...");
		try {
			output.close();
			input.close();
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Connection ended.");
	}
}
