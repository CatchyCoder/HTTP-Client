package music.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientConnectionImpl extends ClientConnection {
	
	private static final Logger log = LogManager.getLogger(ClientConnectionImpl.class);
	
	public ClientConnectionImpl(Socket socket) {
		super(socket);
	}
}