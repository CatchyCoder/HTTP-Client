package music.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import music.core.Storage;

public class ServerPage extends Page {
	
	private static final Logger log = LogManager.getLogger(ServerPage.class);

	private static final long serialVersionUID = 1L;

	public ServerPage(PAGE_TYPE pageType, ArrayList<String> values, Storage storage, GUI gui) {
		super(pageType, values, storage, gui);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
