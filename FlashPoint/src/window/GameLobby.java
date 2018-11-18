package window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

public class GameLobby extends JFrame {

	private JPanel contentPane;
	private JTextField lbltmpChatTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameLobby frame = new GameLobby();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameLobby() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1056, 633);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel paneltmpChatBox = new JPanel();
		paneltmpChatBox.setBackground(Color.ORANGE);
		paneltmpChatBox.setForeground(Color.WHITE);
		paneltmpChatBox.setBounds(728, 38, 283, 504);
		contentPane.add(paneltmpChatBox);
		paneltmpChatBox.setLayout(null);
		
		lbltmpChatTitle = new JTextField();
		lbltmpChatTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbltmpChatTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbltmpChatTitle.setText("Chat Box");
		lbltmpChatTitle.setBounds(61, 190, 152, 83);
		paneltmpChatBox.add(lbltmpChatTitle);
		lbltmpChatTitle.setColumns(10);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartGame.setBounds(385, 521, 85, 21);
		contentPane.add(btnStartGame);
		
		JButton btnBackToMain = new JButton("Back");
		btnBackToMain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBackToMain.setBounds(127, 521, 85, 21);
		contentPane.add(btnBackToMain);
		
		JPanel panel = new JPanel();
		panel.setBounds(57, 54, 374, 191);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setBounds(0, 0, 73, 28);
		panel.add(lblPlayers);
		lblPlayers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel panelPlayerAppearance = new JPanel();
		panelPlayerAppearance.setBounds(0, 38, 374, 153);
		panel.add(panelPlayerAppearance);
		panelPlayerAppearance.setBackground(Color.PINK);
	}
}
