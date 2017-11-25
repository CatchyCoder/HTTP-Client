package music.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.client.Page.PAGE_TYPE;
import music.core.Storage;

public class ClientPage extends Page {
	
	private static final Logger log = LogManager.getLogger(ClientPage.class);

	private static final long serialVersionUID = 1L;

	public ClientPage(PAGE_TYPE pageType, ArrayList<String> values, GUI gui) {
		super(pageType, values, gui);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		// A selection was made in the list, by checking getValueIsAdjusting()
		// the code will run right when an event was created, and ignore upcoming related events.
		if(event.getValueIsAdjusting()) {
			String value = list.getSelectedValue();
			ClientPage page = null; // The page to be displayed next, it's value will be determined based on current page content
			
			// Load a new page depending on the data type currently displayed
			switch(pageType) {
			case ARTIST_ALL_ALBUMS:
				/*ArrayList<Track> tracks = GUI.search(storage.getBinaryTree(), );
				
				ClientPage page = new ClientPage(PAGE_TYPE.ALBUM_SONGS, test, storage, gui);
				gui.loadPage(page);*/
				break;
			case ARTIST_ALL_SONGS:
				/*ArrayList<String> songsFromArtist = GUI.getSongsFromArtist(storage.getBinaryTree(), value);
				
				ClientPage page = new ClientPage(PAGE_TYPE.ALBUM_SONGS, test, storage, gui);
				gui.loadPage(page);*/
				break;
			case ALBUM_SONGS:
				break;
			case ALL_ALBUMS:
				break;
			case ALL_ARTISTS:
				break;
			case ALL_SONGS:
				break;
			case ERROR:
				break;
			default:
				log.error("An error occured attempting to switch to a new Page based on the current pageType value.");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
