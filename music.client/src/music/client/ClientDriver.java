package music.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Storage;

public class ClientDriver {
	
	private static final Logger log = LogManager.getLogger(ClientDriver.class);
	
	private JFrame frame = new JFrame();

	public static void main(String[] args) {
		new ClientDriver();
	}
	
	public ClientDriver() {
		log.debug("Creating startup GUI.");
		createStartupGUI();
		log.debug("Done.");
		
		log.debug("Starting up music client.");
		new Client().run();
		// Remove loading frame after client GUI is up.
		frame.setVisible(false);
	}
	
	public void createStartupGUI() {
		Dimension size = new Dimension(500, 100);
		frame.setSize(size);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		JPanel loadingPanel = new JPanel();
		loadingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadingPanel.setPreferredSize(size);
		frame.add(loadingPanel);
		
		JLabel loadingMessage = new JLabel();
		loadingMessage.setFont(new Font("Arial", Font.BOLD, 18));
		loadingMessage.setText("Loading application...");
		loadingMessage.setOpaque(true); // Allow JLabel to paint its background
		loadingMessage.setForeground(Color.WHITE);
		loadingMessage.setBackground(Color.DARK_GRAY);
		loadingMessage.setHorizontalAlignment(SwingConstants.CENTER);
		loadingMessage.setPreferredSize(size);
		loadingPanel.add(loadingMessage);
		
		frame.setVisible(true);
	}
}
