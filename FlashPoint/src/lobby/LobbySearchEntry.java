package lobby;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Represents a search entry on the finding lobby page
 * @author James
 *
 */
public class LobbySearchEntry extends JPanel {
	private JPanel panel_main;
	private JLabel lbl_Mode;
	private JLabel lbl_nbPlayers;
	private JLabel lbl_hostName;
	private JLabel lbl_GameName;
	
	public LobbySearchEntry(Lobby lobby) {
		setLayout(null);
		
		panel_main = new JPanel();
		panel_main.setBounds(32, 35, 749, 177);
		add(panel_main);
		panel_main.setLayout(null);
		
		lbl_GameName = new JLabel("Alpha");
		lbl_GameName.setForeground(Color.GREEN);
		lbl_GameName.setFont(new Font("Open Sans", Font.PLAIN, 24));
		lbl_GameName.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_GameName.setBounds(10, 10, 729, 55);
		lbl_GameName.setBackground(Color.BLACK);
		lbl_GameName.setOpaque(true);
		panel_main.add(lbl_GameName);
		
		lbl_hostName = new JLabel("Host: ");
		lbl_hostName.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_hostName.setBounds(20, 75, 157, 34);
		panel_main.add(lbl_hostName);
		
		lbl_nbPlayers = new JLabel("Number of Players: ");
		lbl_nbPlayers.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_nbPlayers.setBounds(20, 115, 279, 34);
		panel_main.add(lbl_nbPlayers);
		
		lbl_Mode = new JLabel("Mode: ");
		lbl_Mode.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_Mode.setBounds(542, 75, 197, 34);
		panel_main.add(lbl_Mode);
		
		System.out.println("I'm here");
		
	}

	public JPanel getPanel_main() {
		return panel_main;
	}

	public void setPanel_main(JPanel panel_main) {
		this.panel_main = panel_main;
	}

	public JLabel getLbl_Mode() {
		return lbl_Mode;
	}

	public void setLbl_Mode(JLabel lbl_Mode) {
		this.lbl_Mode = lbl_Mode;
	}

	public JLabel getLbl_nbPlayers() {
		return lbl_nbPlayers;
	}

	public void setLbl_nbPlayers(JLabel lbl_nbPlayers) {
		this.lbl_nbPlayers = lbl_nbPlayers;
	}

	public JLabel getLbl_hostName() {
		return lbl_hostName;
	}

	public void setLbl_hostName(JLabel lbl_hostName) {
		this.lbl_hostName = lbl_hostName;
	}

	public JLabel getLbl_GameName() {
		return lbl_GameName;
	}

	public void setLbl_GameName(JLabel lbl_GameName) {
		this.lbl_GameName = lbl_GameName;
	}

}
