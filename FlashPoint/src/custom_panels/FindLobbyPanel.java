package custom_panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lobby.Lobby;

/**
 * 
 * @author James
 *
 */
public class FindLobbyPanel extends JPanel {
	private JPanel panel_main;
	private JLabel lbl_findLobby;
	private Lobby dummyLobby;	// Test
	
	private LinkedList<Lobby> availLobbies;
	private int nbLobbies = 10;		// arbitrary number
	
	/**
	 * Create the visible components
	 */
	public FindLobbyPanel() {
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);

		panel_main = new JPanel();
		panel_main.setBounds(199, 71, 617, 641);
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
		
		
		
		
		displayLobbies();
	}

	private void displayLobbies() {
		for (Lobby curLobby : availLobbies) {
			
		}
	}
	
}
