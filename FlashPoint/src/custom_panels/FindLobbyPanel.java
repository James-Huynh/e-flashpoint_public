package custom_panels;

import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lobby.Lobby;
import lobby.LobbySearchEntry;

/**
 * 
 * @author James
 *
 */
public class FindLobbyPanel extends JPanel {
	private JPanel panel_main;
	private JLabel lbl_findLobby;
	private Lobby dummyLobby;	// Test
	private LobbySearchEntry dummyEntry;
	
	private LinkedList<Lobby> availLobbies;
	private LinkedList<LobbySearchEntry> lobbyEntries;
	private int nbLobbies = 10;		// arbitrary number
	
	/**
	 * Create the visible components
	 */
	public FindLobbyPanel() {
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
		//availLobbies =  server.getCurrentLobbies();
		dummyLobby = new Lobby();
		
		
		
		displayLobbies();
	}

	private void displayLobbies() {
		/* for (Lobby curLobby : availLobbies) {
		} */
		dummyEntry = new LobbySearchEntry(dummyLobby);
		dummyEntry.setVisible(true);
		panel_main.add(dummyEntry.getPanel_main());		// weird
	}
	
}
