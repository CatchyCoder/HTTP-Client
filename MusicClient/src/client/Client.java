package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField userText;
	private JTextArea chatWindow;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Socket connection;
	
	private String message = "";
	private String serverIP;
	
	
	private boolean gotFile = false;
	
	public Client(String host) {
		super("Client BETA");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		
		chatWindow = new JTextArea();
		add(userText, BorderLayout.NORTH);
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void start() {
		try {
			connect();
			setupStreams();
			chat();
		}
		catch(EOFException e) {
			showMessage("Disconnected from host.");
		}
		catch(IOException e) {
			showMessage("A fatal error occurred!");
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
	}
	
	public void connect() throws IOException {
		showMessage("Attemping connection with server...");
		// Connection to the server using the IP address and port
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connection to server " + connection.getInetAddress().getHostName() + " established!");
	}
	
	private void setupStreams() throws IOException{
		showMessage("Initializing streams...");
		input = new ObjectInputStream(connection.getInputStream());
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		showMessage("Streams setup successfully.");
	}
	
	private void chat() throws IOException {
		ableToType(true);
		//File file;
		do {
			if(gotFile) break;
			
			showMessage("Getting ready to recieve file...");
			// For reading the incoming file
			InputStream input = connection.getInputStream();
			
			// For storing the incoming file (saving)
			FileOutputStream fOutput = new FileOutputStream("C:/Users/clay/Documents/SONG.mp3");
			BufferedOutputStream bOutput = new BufferedOutputStream(fOutput);
			
			int bufferSize = 1024 * 4;
			
			byte[] bytes = new byte[bufferSize];
			
			showMessage("Recieving file...");
						
			int count;
			
			while((count = input.read(bytes)) >= 0) {
				showMessage(count + " bytes recieved.");
				bOutput.write(bytes, 0, count);
			}
			
			fOutput.flush();
			bOutput.flush();
			
			input.close();
			fOutput.close();
			bOutput.close();
			
			showMessage("File recieved!");
			gotFile = true;
			
		}
		while(!message.equals("[SERVER] END"));
		
		ableToType(false);
	}
	
	public void disconnect() {
		showMessage("Disconnecting from server...");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
			showMessage("Disconnected successfully.\n==========================\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String message) {
		try {
			output.writeObject("[CLIENT] " + message);
			output.flush();
			showMessage("\n[CLIENT] " + message);
		}
		catch(IOException e) {
			showMessage("Message could not be sent!");
			e.printStackTrace();
		}
		
	}
	
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						chatWindow.append("\n" + message);
					}
				}
			);
	}
	
	private void ableToType(final boolean canType) {
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					userText.setEditable(canType);
				}
			}
		);
	}
}
