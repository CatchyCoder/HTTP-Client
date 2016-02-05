package music.client;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	private final ConnectionImpl connection;
	
	private JFrame frame = new JFrame();
	
	public GUI(ConnectionImpl connection) {
		this.connection = connection;
		
		// Setting up client GUI
		frame.setSize(700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		frame.setVisible(true);
		
	}
}
