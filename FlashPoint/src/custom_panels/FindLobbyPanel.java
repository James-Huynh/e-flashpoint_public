package custom_panels;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import lobby.Lobby;
import lobby.LobbySearchEntry;
import personalizedlisteners.lobbyListeners.SearchEntryListener;
import personalizedlisteners.lobbyListeners.SearchEntrySetUpListener;

/**
 * 
 * @author James
 *
 */
public class FindLobbyPanel extends JPanel {
	ClientManager clientManager;
	
	private JPanel panel_main;
	private JLabel lbl_findLobby;
	private Lobby dummyLobby;	// Test
	private LobbySearchEntry dummyEntry;
	private LobbySearchEntry currEntry;
	
	private ArrayList<Lobby> availLobbies;
	private ArrayList<LobbySearchEntry> lobbyEntries;
	private int nbLobbies = 10;		// arbitrary number
	
	private final EventListenerList REGISTERED_OBJECTS;
	
	/**
	 * Create the visible components
	 */
	public FindLobbyPanel(ClientManager myClientManager) {
		
		REGISTERED_OBJECTS = new EventListenerList();
		this.clientManager = myClientManager;
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);

		panel_main = new JPanel();
		panel_main.setBounds(193, 71, 617, 641);
		panel_main.setLayout(null);

		lbl_findLobby = new JLabel("Select a lobby");
		lbl_findLobby.setBounds(393, 10, 216, 53);
		lbl_findLobby.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_findLobby.setFont(new Font("SansSerif", Font.PLAIN, 24));
		
		this.add(lbl_findLobby);
		this.add(panel_main);
		
		initialize();
	}

	private void initialize() {
		// @server
		availLobbies =  clientManager.getLobbyList();
//		dummyLobby = availLobbies.get(0);
		
		
		createSearchEntries();
		
		
		displaySearchEntries();
	}

	private void createSearchEntries() {
		lobbyEntries = new ArrayList<LobbySearchEntry>();
		
		for (Lobby lobby: availLobbies ) {
			
			currEntry = new LobbySearchEntry(lobby, clientManager);
			currEntry.addSelectionPiecesListenerListener(new SearchEntryListener() {
				@Override
				public void clickSearchEntry() {
					raiseEventCallSetUpLobby();
				}
			});
			
			lobbyEntries.add(currEntry);
		}
		
	}

	private void displaySearchEntries() {
		 for (LobbySearchEntry entry : lobbyEntries) {
			 entry.setVisible(true);
			 panel_main.add(entry.getPanel_main());
			 // how to place the entries in the right location
		} 
		 
		 
//		dummyEntry = new LobbySearchEntry(dummyLobby, clientManager);
//		dummyEntry.setVisible(true);
//		panel_main.add(dummyEntry.getPanel_main());		// weird
	}
	
	public void addSelectionPiecesListenerListener(SearchEntrySetUpListener obj) {
		REGISTERED_OBJECTS.add(SearchEntrySetUpListener.class, obj);
	}

	private void raiseEventCallSetUpLobby() {
		for (SearchEntrySetUpListener listener: REGISTERED_OBJECTS.getListeners(SearchEntrySetUpListener.class)) {
			listener.SearchEntrySetUp();
		}
	}
	
}
