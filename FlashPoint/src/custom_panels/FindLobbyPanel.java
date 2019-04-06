package custom_panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import lobby.Lobby;
import lobby.LobbySearchEntry;
import personalizedlisteners.createLobbyListeners.BackListener;
import personalizedlisteners.lobbyListeners.SearchEntryListener;
import personalizedlisteners.lobbyListeners.SearchEntrySetUpListener;

/**
 * 
 * @author James
 *
 */
public class FindLobbyPanel extends JPanel {
	ClientManager clientManager;
	
	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();
	
	private JPanel panel_main;
	private JLabel lbl_findLobby;
	private Lobby dummyLobby;	// Test
	private LobbySearchEntry dummyEntry;
	private LobbySearchEntry currEntry;
	
	private ArrayList<Lobby> availLobbies;
	private ArrayList<LobbySearchEntry> lobbyEntries;
	private int nbLobbies = 10;		// arbitrary number
	private int positionMultiplier = 150;

	private JButton backBtn;
	
	
	/**
	 * Create the visible components
	 */
	public FindLobbyPanel(ClientManager myClientManager) {
		
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
		availLobbies =  clientManager.getLobbyList();
		
		createBackButton();
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
	
	private void createBackButton() {
		backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventBackBtn();
			}
		});
		backBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		backBtn.setBounds(22, 560, 140, 54);
		panel_main.add(backBtn);
	}

	private void displaySearchEntries() {
		 for (int i = 0 ; i < lobbyEntries.size(); i ++) {
			 LobbySearchEntry entry = lobbyEntries.get(i);
			 JPanel entryPanel = entry.getPanel_main();
	
			 entryPanel.setLocation(new Point (0, positionMultiplier * i));
			 panel_main.add(entryPanel);
			 // how to place the entries in the right location?
		} 
		 
		 
//		dummyEntry = new LobbySearchEntry(dummyLobby, clientManager);
//		dummyEntry.setVisible(true);
//		panel_main.add(dummyEntry.getPanel_main());		// weird
	}
	
	public void addSelectionPiecesListenerListener(SearchEntrySetUpListener obj) {
		REGISTERED_OBJECTS.add(SearchEntrySetUpListener.class, obj);
	}
	
	public void addSelectionPiecesListenerListener(BackListener obj) {
		REGISTERED_OBJECTS.add(BackListener.class, obj);
	}

	private void raiseEventCallSetUpLobby() {
		for (SearchEntrySetUpListener listener: REGISTERED_OBJECTS.getListeners(SearchEntrySetUpListener.class)) {
			listener.SearchEntrySetUp();
		}
	}
	
	/**
	 * Raise an event: the back button has been clicked
	 */
	private void raiseEventBackBtn() {
		for (BackListener listener: REGISTERED_OBJECTS.getListeners(BackListener.class)) {
			listener.clickBack();
		}
	}
	
}
