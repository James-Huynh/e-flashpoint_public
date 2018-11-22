package window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * GameLobby will manage all information related to the real game lobby in the game Flashpoint. The user can quit and chat in this phase
 * @author huynh
 *
 */
public class GameLobby extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JButton btnBackToMain;
	private JButton btnStartGame;
	private JLabel lblChat;
	private JPanel panelLobbyDescription;
	private JLabel lblRules;
	private JTextField txtFieldChat;
	private JPanel panel_1;
	private JButton btnChat;
	private JFrame mainMenu;
	private JLabel lblPlayer1;
	private JLabel lblPlayer2;
	private JLabel lblPlayer3;
	private JLabel lblPlayer4;
	private JLabel lblPlayer5;
	private JLabel lblPlayer6;
	private JLabel lblMode;
	private JLabel lblDifficulty;
	private JLabel[] arrLblPlayers = {lblPlayer1, lblPlayer2, lblPlayer3, lblPlayer4, lblPlayer5, lblPlayer6};

	private ArrayList<String> listPlayers = new ArrayList<String>();
	private InGame inGame;
	/**
	 * 
	 * @param mainMenu
	 */
	public GameLobby(JFrame mainMenu/*, GameState parameter here, string HostName*/) {
		this.mainMenu = mainMenu;
		initializeBasicStuff();


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1056, 633);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBounds(695, 10, 337, 576);
		contentPane.add(panel_1);
		panel_1.setLayout(null);


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 337, 537);
		panel_1.add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		lblChat = new JLabel("Chat");
		lblChat.setVerticalAlignment(SwingConstants.TOP);
		scrollPane.setViewportView(lblChat);
		lblChat.setHorizontalAlignment(SwingConstants.LEFT);
		lblChat.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblChat.setText("<html>");

		txtFieldChat = new JTextField();
		txtFieldChat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		txtFieldChat.setBounds(0, 536, 289, 40);
		panel_1.add(txtFieldChat);
		txtFieldChat.setBackground(Color.ORANGE);
		txtFieldChat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtFieldChat.setColumns(10);

		btnChat = new JButton(">");
		btnChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		btnChat.setBounds(288, 536, 50, 40);
		panel_1.add(btnChat);
		btnChat.setFont(new Font("Tahoma", Font.PLAIN, 20));

		btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		btnStartGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartGame.setBounds(496, 508, 176, 78);
		contentPane.add(btnStartGame);

		btnBackToMain = new JButton("Back");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				dispose();
			}
		});
		btnBackToMain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBackToMain.setBounds(10, 508, 192, 78);
		contentPane.add(btnBackToMain);

		panel = new JPanel();
		panel.setBounds(10, 10, 662, 372);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayers.setBounds(0, 0, 73, 28);
		panel.add(lblPlayers);
		lblPlayers.setFont(new Font("Tahoma", Font.PLAIN, 20));

		lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setBackground(Color.YELLOW);
		lblPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer1.setBounds(0, 38, 662, 43);
		panel.add(lblPlayer1);

		panelLobbyDescription = new JPanel();
		panelLobbyDescription.setBounds(10, 394, 662, 84);
		contentPane.add(panelLobbyDescription);
		panelLobbyDescription.setLayout(null);

		lblRules = new JLabel("Rules: ");
		lblRules.setVerticalAlignment(SwingConstants.TOP);
		lblRules.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRules.setBounds(0, 0, 125, 84);
		panelLobbyDescription.add(lblRules);

		lblMode = new JLabel("Mode:");
		lblMode.setVerticalAlignment(SwingConstants.TOP);
		lblMode.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMode.setBounds(242, 0, 125, 84);
		panelLobbyDescription.add(lblMode);

		lblDifficulty = new JLabel("Difficulty:");
		lblDifficulty.setVerticalAlignment(SwingConstants.TOP);
		lblDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDifficulty.setBounds(537, 0, 125, 84);
		panelLobbyDescription.add(lblDifficulty);


		initializeBasicStuff();

		lblPlayer1.setOpaque(true);

		lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setOpaque(true);
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer2.setBackground(Color.MAGENTA);
		lblPlayer2.setBounds(0, 92, 662, 43);
		panel.add(lblPlayer2);

		lblPlayer3 = new JLabel("Player 3");
		lblPlayer3.setOpaque(true);
		lblPlayer3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer3.setBackground(Color.GREEN);
		lblPlayer3.setBounds(0, 145, 662, 43);
		panel.add(lblPlayer3);


		verifyLobbyConditions();

		//loop();
	}

	private void loop() {
		while (true) {

		}
	}


	private boolean verifyLobbyConditions() {
		if (this.listPlayers.size() < 3) {
			btnStartGame.setEnabled(false);
			return false;
		} else {
			btnStartGame.setEnabled(false);
			return true;
		}
	}

	private void startGame() {
		inGame = new InGame();
		inGame.setVisible(true);
		inGame.setLocationRelativeTo(null);
		dispose();

	}


	private void sendMessage() {
		String playerName; // game.state.sendServer
		String message = txtFieldChat.getText();
		lblChat.setText(lblChat.getText()+ message + "<br>");
		txtFieldChat.setText("");

	}

	/**
	 * for testing
	 */
	private void initializeBasicStuff() {
		listPlayers.add("James");
		listPlayers.add("Zaid");
		listPlayers.add("Ben");		
	}

	private void updateGameLobby() {
		// OBSERVER DP: get state, set state

	}
	
	private void displayPlayers() {
		for(int i = 0; i < listPlayers.size(); i++) {
			
		}
	}

}



/*
DO FIND LOBBY, connected to actual game lobby

*/