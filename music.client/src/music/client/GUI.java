package music.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import music.core.Track;
import music.core.binarytree.BinaryTree;

public class GUI implements ActionListener{

	private final ClientConnectionImpl connection;
	
	private JFrame frame = new JFrame();
	private JPanel leftPanel, rightPanel;
	private JButton artistButton, albumButton, songButton;
	private JList<String> list;
	private JScrollPane listScroller;
	
	public GUI(ClientConnectionImpl connection) {
		this.connection = connection;
		
		// Setting up client GUI
		frame.setSize(700, 500);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// Left panel setup
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.GRAY);
		Dimension size = new Dimension((int)(frame.getWidth() * 0.3) - 5, frame.getHeight());
		leftPanel.setSize(size);
		leftPanel.setPreferredSize(size);
		populateLeftPanel();
		frame.add(leftPanel, BorderLayout.WEST);
		
		// Right panel setup
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		Dimension size2 = new Dimension((int)(frame.getWidth() * 0.7), frame.getHeight());
		rightPanel.setSize(size2);
		rightPanel.setPreferredSize(size2);
		populateRightPanel();
		frame.add(rightPanel, BorderLayout.EAST);
		
		frame.setVisible(true);
		/*
		// Container for top and bottom area
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("panel"));
		frame.add(panel);
		
		// Top area
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.setBackground(Color.RED);
		top.add(new JLabel("top"));
		panel.add(top);
		// Top left
		JPanel topLeft = new JPanel();
		topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.X_AXIS));
		topLeft.add(new JLabel("topLeft"));
		top.add(topLeft);
		*/
	}
	
	private void populateLeftPanel() {
		artistButton = new JButton("Artists");
		albumButton = new JButton("Albums");
		songButton = new JButton("Songs");
		artistButton.addActionListener(this);
		albumButton.addActionListener(this);
		songButton.addActionListener(this);
		leftPanel.add(artistButton);
		leftPanel.add(albumButton);
		leftPanel.add(songButton);
	}
	
	private void populateRightPanel() {
		String[] str = new String[1];
		str[0] = "Please select an option from the right.";
		list = new JList<String>(str);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		// Setting up scroll pane for list
		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(null);
		
		rightPanel.add(listScroller);
		
	}
	
	private void updateList(ArrayList<String> values) {
		rightPanel.remove(listScroller);
		list = new JList<String>(values.toArray(new String[values.size()]));
		listScroller = new JScrollPane(list);
		rightPanel.add(listScroller);
		rightPanel.validate();
		rightPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent object) {
		Object source = object.getSource();
		
		if(source.equals(artistButton)) {
			// Getting artists from binary tree
			BinaryTree tree = connection.readTree();
			ArrayList<Track> tracks = tree.preOrderTraversal();
			ArrayList<String> artists = new ArrayList<String>();
			for(int n = 0; n < tracks.size(); n++) {
				String artist = tracks.get(n).getArtist();
				if(!artists.contains(artist)) artists.add(artist);
			}
			
			// Adding to list
			updateList(artists);
		}
		else if(source.equals(albumButton)) {
			// Getting artists from binary tree
			BinaryTree tree = connection.readTree();
			ArrayList<Track> tracks = tree.preOrderTraversal();
			ArrayList<String> albums = new ArrayList<String>();
			for(int n = 0; n < tracks.size(); n++) {
				String album = tracks.get(n).getAlbum();
				if(!albums.contains(album)) albums.add(album);
			}
			
			// Adding to list
			updateList(albums);
		}
		else if(source.equals(songButton)) {
			// Getting artists from binary tree
			BinaryTree tree = connection.readTree();
			ArrayList<Track> tracks = tree.preOrderTraversal();
			ArrayList<String> titles = new ArrayList<String>();
			for(int n = 0; n < tracks.size(); n++) {
				String title = tracks.get(n).getTitle();
				if(!titles.contains(title)) titles.add(title);
			}
			
			// Adding to list
			updateList(titles);
		}
	}
}
