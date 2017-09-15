package music.client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Storage;

/**
 * A JPanel that acts as a viewing page for artists, albums and songs by utilizing <code>javax.swing.JList</code>.
 */
public abstract class Page extends JPanel implements ActionListener, ListSelectionListener {
	
	private static final Logger log = LogManager.getLogger(Page.class);
	
	private static final long serialVersionUID = 1L;
	
	public static enum PAGE_TYPE {ARTIST_ALL_ALBUMS, ARTIST_ALL_SONGS, ALBUM_SONGS, ALL_ARTISTS, ALL_ALBUMS, ALL_SONGS, ERROR};
	protected PAGE_TYPE pageType; // The type of data this page is displaying
	
	protected GUI gui;
	protected Storage storage;
	
	protected JList<String> list;
	protected JScrollPane listScroller;
	
	/**
	 * Initializes the JPanel that will act as a viewing page for server or client content. PAGE_TYPE tells the Page what kind
	 * of data is being viewed, and therefore what to display next when an item is selected.
	 * 
	 * <p>For example if viewing an artist the PAGE_TYPE will be ARTIST_ALL_ALBUMS and when a value is selected it will then
	 * create a new page with a PAGE_TYPE of ALBUM_SONGS.
	 * 
	 * @param pageType The type of data displayed on the page
	 * @param values the data to display
	 * @param storage the storage object where client and server files can be found
	 */
	public Page(PAGE_TYPE pageType, ArrayList<String> values, Storage storage, GUI gui) {
		this.pageType = pageType;
		this.storage = storage;
		this.gui = gui;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setBackground(Color.BLACK);
		
		// Converting ArrayList to array
		String[] arrayValues = values.toArray(new String[values.size()]);
		updateList(arrayValues);
	}
	
	private void updateList(String[] values) {
		if(listScroller != null) remove(listScroller);
		list = new JList<String>(values);
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setForeground(Color.WHITE);
		list.setBackground(Color.BLACK);
		
		// Setting up scroll pane for list
		listScroller = new JScrollPane(list);
		listScroller.setBackground(Color.BLUE);
		listScroller.setForeground(Color.YELLOW);
		
		// Adding back to panel
		add(listScroller);
		validate();
		repaint();
	}

	
}
