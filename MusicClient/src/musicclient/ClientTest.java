package musicclient;

public class ClientTest {

	public static void main(String[] args) {
		// 127.0.0.1 is localhost.. this means the server is
		// on the same computer
		
		// 174.27.24.2
		new Client("127.0.0.1").start();
	}
}