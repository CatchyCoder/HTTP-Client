package music.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class GUI implements ActionListener{

	private final ConnectionImpl connection;
	
	private JFrame frame = new JFrame();
	private JPanel leftPanel, rightPanel;
	private JButton artistButton, albumButton, songButton;
	private JList<String> list;
	private JScrollPane listScroller;
	
	public GUI(ConnectionImpl connection) {
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

	@Override
	public void actionPerformed(ActionEvent object) {
		Object source = object.getSource();
		
		if(source.equals(artistButton)) {
			System.out.println("test");
			
			// Getting artists
			// TODO:
			String[] artists = new String[50];
			String s = "bjsoejlns";
			
			for(int n = 0; n < artists.length; n++) {
				int index = (int)(Math.random() * (s.length() - 1)) + 1;
				artists[n] = s.substring(0, index);
			}
			
			// Adding to list
			rightPanel.remove(listScroller);
			list = new JList<String>(artists);
			listScroller = new JScrollPane(list);
			rightPanel.add(listScroller);
			rightPanel.validate();
			rightPanel.repaint();
		}
	}
}
