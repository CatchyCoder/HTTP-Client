package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Client {
	
	private Connection connection;
	
	private final String hostIP = "127.0.0.1";
	
	public Client() {	
		try {
			
			testCode();
			
			if(true) return;
			
			// Connect to the server
			connect();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void testCode() {
		try {
			FTPClient ftpClient = new FTPClient();
			
			// Connecting to server and configuring settings
			System.out.print("Connecting to server... ");
			ftpClient.connect(hostIP, 6789);
			System.out.println("Done.");
			//ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			// Downloading file
			File downloadFile = new File("C://test.txt");
			System.out.print("Downloading file... ");
			boolean madeIt = ftpClient.retrieveFile("C://Test/test.txt", new BufferedOutputStream(new FileOutputStream(downloadFile)));
			System.out.println("Done.");
			if(madeIt) System.out.println("Download successful.");
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() throws IOException {
		System.out.print("Attemping connection with server... ");
		
		Socket socket = null;
		boolean connected = false;
		
		// Make several attempts to connect to the server
		for(int attempts = 0; !connected && attempts < 15; attempts++) {
			try {
				// Connecting to the server using the IP address and port
				socket = new Socket(InetAddress.getByName(hostIP), 6789);
				connected = true;
			}
			catch(IOException e) {
				if(attempts == 0) System.out.println();
				System.out.print(".");
			}
		}
		
		if(socket == null) System.err.println(" Connection timeout.");
		else {
			System.out.println(" Connected.");
			connection = new Connection(socket);
		}
	}
}
