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
	
	int x = 0;
	
	@Override
	public void run() {
		try{Thread.sleep(2500);}
		catch(Exception e){e.printStackTrace();}
		
		// Tell the server to send us it's list of songs
		send(x++);
		if(true) run();
		
		try {
			System.out.println("Retrieving songs...");
			// Retrieve those songs
			String[][] songs = (String[][]) input.readObject();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void send(int message) {
		try {
			// Send a message to the client
			output.writeObject(message);
			output.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void getFile() {
		try {
			System.out.println("Getting ready to recieve file...");
			// For reading the incoming file
			InputStream input = socket.getInputStream();
			
			// For storing the incoming file (saving)
			FileOutputStream fOutput = new FileOutputStream("C:/Users/clay/Documents/SONG.mp3");
			BufferedOutputStream bOutput = new BufferedOutputStream(fOutput);
			
			int bufferSize = 1024 * 4;
			
			byte[] bytes = new byte[bufferSize];
			
			System.out.println("Recieving file...");
						
			// Reading from the input stream and saving to a file
			for(int count; (count = input.read(bytes)) >= 0;) {
				System.out.println(count + " bytes recieved.");
				bOutput.write(bytes, 0, count);
			}
			
			// Close down streams
			input.close();
			fOutput.close();
			bOutput.close();
			
			System.out.println("File recieved!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		// Close streams and sockets
		System.out.println("Ending connection...");
		try {
			input.close();
			output.close();
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Connection ended.\n==========================\n");
	}
}
