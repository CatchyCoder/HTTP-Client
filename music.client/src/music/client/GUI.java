package music.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Storage;
import music.core.Track;
import music.core.binarytree.BinaryTree;

public class GUI extends Client implements ActionListener, ListSelectionListener {
	
	private static final Logger log = LogManager.getLogger(GUI.class);
	
	// Loading screen
	private JFrame loadingFrame = new JFrame();
	private JPanel loadingPanel = new JPanel();
	private JLabel loadingMessage = new JLabel();
	
	// Main GUI
	private JFrame frame = new JFrame();
	private JPanel center;
	private final Font font = new Font("Arial", Font.PLAIN, 24);
	private JButton client, server, downloads;
	
	public GUI() {
		// Setup and deploy loading screen
		loadingSetup();

		// Setup and deploy main GUI
		mainGUISetup();
		
		this.connection.test_readStream("C://test");
	}
	
	private void loadingSetup() {
		Dimension size = new Dimension(500, 100);
		loadingFrame.setSize(size);
		loadingFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadingFrame.setUndecorated(true);
		loadingFrame.setLocationRelativeTo(null);
		loadingFrame.setResizable(false);
		
		loadingPanel = new JPanel();
		loadingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadingPanel.setPreferredSize(size);
		loadingFrame.add(loadingPanel);
		
		loadingMessage.setFont(new Font("Arial", Font.BOLD, 18));
		loadingMessage.setText("Loading application  |  configuring files.");
		loadingMessage.setOpaque(true); // Allow JLabel to paint its background
		loadingMessage.setForeground(Color.WHITE);
		loadingMessage.setBackground(Color.DARK_GRAY);
		loadingMessage.setHorizontalAlignment(SwingConstants.CENTER);
		loadingMessage.setPreferredSize(size);
		loadingPanel.add(loadingMessage);
		
		loadingFrame.setVisible(true);
		
		try {
			Thread.sleep(10);
		} catch(Exception e) {}
		
		// Setting up application storage
		storage = new Storage("C:/music_client", "/database", "/download");
		
		loadingMessage.setText("Connecting to server...");
		
		// Connecting client to server
		connection = connect();
		
		loadingMessage.setText("Done.");
		loadingFrame.setVisible(false);
	}
	
	private void mainGUISetup() {
		frame.setSize(950, 700);
		frame.setLayout(new BorderLayout(0, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// Top JPanel for North border buttons
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 0, 0, 0)); // 1 row with as many columns as necessary
		topPanel.setBackground(Color.DARK_GRAY);
		// Client button
		client = new JButton("Client");
		client.addActionListener(this);
		client.setBackground(Color.DARK_GRAY);
		client.setForeground(Color.WHITE);
		client.setBorderPainted(false); // Do not paint a border around button when hovering over with mouse
		client.setFocusPainted(false); // Do not paint a border around text when button is selected
		client.setHorizontalTextPosition(SwingConstants.CENTER);
		client.setFont(font);
		client.setDisabledIcon(null);
		client.setEnabled(false); // Client is shown by default, and is therefore disabled
		topPanel.add(client);
		// Downloads button
		downloads = new JButton("Downloads");
		downloads.addActionListener(this);
		downloads.setBackground(Color.DARK_GRAY);
		downloads.setForeground(Color.WHITE);
		downloads.setBorderPainted(false); // Do not paint a border around button when hovering over with mouse
		downloads.setFocusPainted(false); // Do not paint a border around text when button is selected
		downloads.setHorizontalTextPosition(SwingConstants.CENTER);
		downloads.setFont(font);
		topPanel.add(downloads);
		// Server button
		server = new JButton("Server");
		server.addActionListener(this);
		server.setBackground(Color.DARK_GRAY);
		server.setForeground(Color.WHITE);
		server.setBorderPainted(false); // Do not paint a border around button when hovering over with mouse
		server.setFocusPainted(false); // Do not paint a border around text when button is selected
		server.setHorizontalTextPosition(SwingConstants.CENTER);
		server.setFont(font);
		topPanel.add(server);
		frame.add(topPanel, BorderLayout.NORTH);
		
		// CENTER PANEL
		ArrayList<String> artists = getArtists(storage.getBinaryTree());
		ClientPage defaultPage = new ClientPage(Page.PAGE_TYPE.ALL_ARTISTS, artists, storage, this);
		loadPage(defaultPage);
		
		frame.setVisible(true);
	}
	
	public void loadPage(Page page) {
		if(center != null) frame.remove(center);
		center = page;
		frame.add(center, BorderLayout.CENTER);
		// Re-validate components and repaint
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * Does a search on the passed in binary tree to find Track objects that match the passed
	 * in artist, album, and track specifications. If artist, album, or track are null then the
	 * filter isn't used.
	 * 
	 * @param tree
	 * @param artist return tracks with this artist
	 * @param album return tracks with this album
	 * @param title return tracks with this title
	 * @return the Track objects that meet all three specifications.
	 */
	public static ArrayList<Track> search(BinaryTree tree, String artist, String album, String title) {
		// Getting artists from binary tree
		ArrayList<Track> all_elements = tree.preOrderTraversal();
		ArrayList<Track> elements = new ArrayList<Track>();
		for(int n = 0; n < all_elements.size(); n++) {
			Track track = all_elements.get(n);
			if(
				(track.getArtist().equals(artist) || artist == null) && // Check for artist match
				(track.getAlbum().equals(album) || album == null) && // Check for album match
				(track.getTitle().equals(title) || title == null)) { // Check for title match
				elements.add(track);
			}
		}
		return elements;
	}
	
	public static ArrayList<String> getArtists(BinaryTree tree) {
		// Getting artists from binary tree
		ArrayList<Track> all_elements = tree.preOrderTraversal();
		ArrayList<String> artists = new ArrayList<String>();
		for(int n = 0; n < all_elements.size(); n++) {
			String artist = all_elements.get(n).getArtist();
			if(!artists.contains(artist)) artists.add(artist);
		}
		return artists;
	}
	
	public static ArrayList<String> getAlbums(BinaryTree tree) {
		// Getting artists from binary tree
		ArrayList<Track> all_elements = tree.preOrderTraversal();
		ArrayList<String> albums = new ArrayList<String>();
		for(int n = 0; n < all_elements.size(); n++) {
			String album = all_elements.get(n).getAlbum();
			if(!albums.contains(album)) albums.add(album);
		}
		return albums;
	}
	
	public static ArrayList<String> getSongs(BinaryTree tree) {
		// Getting artists from binary tree
		ArrayList<Track> all_elements = tree.preOrderTraversal();
		ArrayList<String> songs = new ArrayList<String>();
		for(int n = 0; n < all_elements.size(); n++) {
			String song = all_elements.get(n).getTitle();
			if(!songs.contains(song)) songs.add(song);
		}
		return songs;
	}
	
	public static ArrayList<String> getSongsFromArtist(BinaryTree tree, String artist) {
		ArrayList<Track> all_elements = tree.preOrderTraversal();
		ArrayList<String> songs = new ArrayList<String>();
		for(int n = 0; n < all_elements.size(); n++) {
			Track track = all_elements.get(n);
			String trackArtist = track.getArtist();
			String trackTitle = track.getTitle();
			// Check if the track is by the specified artist, if so add to the ArrayList that will be returned
			if(trackArtist.equals(artist) && !songs.contains(trackTitle)) songs.add(trackTitle);
		}
		return songs;
	}

	@Override
	public void actionPerformed(ActionEvent object) {
		Object source = object.getSource();
		
		// Enable all buttons and only disable the selected one
		client.setEnabled(true);
		downloads.setEnabled(true);
		server.setEnabled(true);
		
		if(source.equals(client)) {
			client.setEnabled(false);
			
			// Update database and search tree, then retrieve updated tree
			storage.update();
			BinaryTree tree = storage.getBinaryTree();
			
			// Get artists from tree and display artists
			ArrayList<String> artists = getArtists(tree);
			ClientPage page = new ClientPage(Page.PAGE_TYPE.ALL_ARTISTS, artists, storage, this);
			loadPage(page);
			
		} else if(source.equals(downloads)) {
			downloads.setEnabled(false);
			// TODO: Code downloads page - will need to sync up with download methods
		} else if(source.equals(server)) {
			server.setEnabled(false);
			
			// If client was able to connect to server
			if(connection != null) {
				// Grabbing tree from server (assuming server is listening for next command)
				BinaryTree tree = connection.readTree();
				
				// Get artists from tree and display artists
				ArrayList<String> artists = getArtists(tree);
				ServerPage page = new ServerPage(Page.PAGE_TYPE.ALL_ARTISTS, artists, storage, this);
				loadPage(page);
			} else {
				// Inform user that client could not connect to server
				ArrayList<String> message = new ArrayList<String>();
				message.add("Client could not connect to server.");
				ServerPage page = new ServerPage(Page.PAGE_TYPE.ERROR, message, storage, this);
				loadPage(page);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent object) {
		// Have a class that is for viewing an artists page,
		// it can then populate classes that are for viewing the artists
		// albums or songs
		
		// The list will either be the regular list of all artists, albums, songs OR 
		// be showing a list that is handled by the artist class
		
		
		
		/*
		// A selection was made in the list, by checking getValueIsAdjusting()
		// the code will run right when an event was created, and ignore the last event.
		if(object.getValueIsAdjusting()) {
			String value = list.getSelectedValue();
			
			// Gather all albums in the tree that have the String
			// value as their artist
			BinaryTree tree = null;
			if(viewingServer) {				
				// Grabbing tree from server (assuming server is listening for next command)
				tree = connection.readTree();
			} else {
				// Update database and search tree, then retrieve updated tree
				storage.update();
				tree = storage.getBinaryTree();
			}
			ArrayList<Track> all_elements = tree.preOrderTraversal();
			ArrayList<String> albums = new ArrayList<String>();
			for(int n = 0; n < all_elements.size(); n++) {
				String artist = all_elements.get(n).getArtist();
				if(artist.equals(value)) {
					String album = all_elements.get(n).getAlbum();
					albums.add(album);
				}
			}
			
			
		}
		*/
	}
}
