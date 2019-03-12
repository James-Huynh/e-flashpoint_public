package lobby;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import client.ClientManager;

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
	private ClientManager clientManager;
	
	public LobbySearchEntry(Lobby lobby, ClientManager myClientManager) {
		setLayout(null);
		clientManager = myClientManager;
		panel_main = new JPanel();
		panel_main.setBounds(32, 35, 749, 177);
		add(panel_main);
		panel_main.setLayout(null);
		
		lbl_GameName = new JLabel(lobby.getName());
		lbl_GameName.setForeground(Color.GREEN);
		lbl_GameName.setFont(new Font("Open Sans", Font.PLAIN, 24));
		lbl_GameName.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_GameName.setBounds(10, 10, 729, 55);
		lbl_GameName.setBackground(Color.BLACK);
		lbl_GameName.setOpaque(true);
		panel_main.add(lbl_GameName);
		
		String hostName = "HostName: " +  lobby.getPlayers().get(0).getUserName();
		lbl_hostName = new JLabel(hostName);
		lbl_hostName.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_hostName.setBounds(20, 75, 157, 34);
		panel_main.add(lbl_hostName);
		
		String numPlayers = "Number of Players: " + lobby.getPlayers().size();
		lbl_nbPlayers = new JLabel(numPlayers);
		lbl_nbPlayers.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_nbPlayers.setBounds(20, 115, 279, 34);
		panel_main.add(lbl_nbPlayers);
		
		String mode = "Mode: " + lobby.getMode();
		lbl_Mode = new JLabel(mode);
		lbl_Mode.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl_Mode.setBounds(542, 75, 197, 34);
		panel_main.add(lbl_Mode);
		
		panel_main.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				sendJoinLobbyRequest(lobby);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
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

	private void sendJoinLobbyRequest(Lobby lobby) {
		clientManager.joinLobbyRequest(lobby);
		
	}

}
