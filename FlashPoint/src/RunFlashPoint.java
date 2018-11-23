import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RunFlashPoint {

	private JFrame frame;
	//Define big panels corresponding to each 'page'
	private JPanel loginSuperPanel;
	private JPanel startMenuSuperPanel;
	private JPanel createLobbySuperPanel;
	private JPanel findLobbySuperPanel;
	private JPanel lobbyPageSuperPanel;



	//========================================JAMES STUFF===========================================//
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
	//========================================JAMES STUFF===========================================//


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunFlashPoint window = new RunFlashPoint();
					window.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RunFlashPoint() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1250, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Sets the master frame to visible and does the first step i.e. login
	private void run() {
		frame.setVisible(true);
		login();
		//		startMenu();  //Use this space to write the method (i.e. the panel) you want to be displayed in 'Design'
		//		createLobby(); //In final version (with current design), this method will only have the first two lines

	}

	//Sets up login and all related panel/headers/labels/buttons
	private void login() {
		loginSuperPanel = new JPanel();
		frame.getContentPane().add(loginSuperPanel, BorderLayout.CENTER);
		loginSuperPanel.setLayout(null);	

		JPanel headingPanel = new JPanel();
		headingPanel.setBounds(369, 197, 552, 213);
		loginSuperPanel.add(headingPanel);
		headingPanel.setLayout(null);

		JLabel gameLabel = new JLabel("FLASHPOINT");
		gameLabel.setBounds(6, 6, 540, 201);
		headingPanel.add(gameLabel);
		gameLabel.setForeground(new Color(255, 0, 0));
		gameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 99));

		JPanel loginPanel = new JPanel();
		loginPanel.setBounds(487, 414, 354, 206);
		loginSuperPanel.add(loginPanel);
		loginPanel.setLayout(null);

		JPasswordField pword = new JPasswordField(10);
		pword.setBounds(162, 53, 166, 35);
		loginPanel.add(pword); 

		JLabel pwdLabel = new JLabel("Password");
		pwdLabel.setFont(new Font("Copperplate", Font.PLAIN, 20));
		pwdLabel.setBounds(25, 62, 103, 16);
		loginPanel.add(pwdLabel);

		JLabel usrLabel = new JLabel("Username");
		usrLabel.setFont(new Font("Copperplate", Font.PLAIN, 20));
		usrLabel.setBounds(25, 14, 103, 16);
		loginPanel.add(usrLabel);

		JTextField userNameField = new JTextField();
		userNameField.setFont(new Font("Avenir", Font.PLAIN, 13));
		userNameField.setBounds(162, 6, 166, 35);
		loginPanel.add(userNameField);
		userNameField.setColumns(10);

		JButton loginBtn = new JButton("Login");
		loginBtn.addActionListener(new ActionListener() {
			//When there is a state/page change, the idea is to set the current Panel false -> dispose it -> call the next method which would
			//set the next appropriate panel
			public void actionPerformed(ActionEvent e) {
				loginSuperPanel.setVisible(false);
				frame.getContentPane().remove(loginSuperPanel);
				startMenu();
			}
		});
		loginBtn.setFont(new Font("Avenir", Font.PLAIN, 20));
		loginBtn.setBounds(41, 153, 117, 35);
		loginPanel.add(loginBtn);

		JButton registerBtn = new JButton("Register");
		registerBtn.setFont(new Font("Avenir", Font.PLAIN, 20));
		registerBtn.setBounds(199, 153, 117, 35);
		loginPanel.add(registerBtn);

		JToggleButton tglBtn = new JToggleButton("Remember Me");
		tglBtn.setSelected(true);
		tglBtn.setBounds(111, 112, 117, 29);
		loginPanel.add(tglBtn);
		
	}

	//Sets up start menu and all related panel/headers/labels/buttons
	private void startMenu() {
		startMenuSuperPanel = new JPanel();
		frame.getContentPane().add(startMenuSuperPanel, BorderLayout.CENTER);
		startMenuSuperPanel.setLayout(null);

		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(395, 182, 540, 297);
		startMenuSuperPanel.add(menuPanel);
		menuPanel.setLayout(null);

		JButton createBtn = new JButton("Create Lobby");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startMenuSuperPanel.setVisible(false);
				frame.getContentPane().remove(startMenuSuperPanel);
				createLobby();	
			}
		});
		createBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		createBtn.setBounds(67, 35, 423, 49);
		menuPanel.add(createBtn);

		JButton findBtn = new JButton("Find Lobby");
		findBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				findLobby(); #To be implemented by Zaid
			}
		});
		findBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		findBtn.setBounds(67, 120, 423, 49);
		menuPanel.add(findBtn);

		JButton rulesBtn = new JButton("Rules");
		rulesBtn.setBounds(67, 206, 423, 49);
		menuPanel.add(rulesBtn);
		rulesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));

		JPanel headingPanel = new JPanel();
		headingPanel.setBounds(46, 36, 417, 69);
		startMenuSuperPanel.add(headingPanel);
		headingPanel.setLayout(null);

		JLabel gameLabel = new JLabel("FLASHPOINT");
		gameLabel.setBounds(0, 0, 405, 69);
		headingPanel.add(gameLabel);
		gameLabel.setForeground(new Color(255, 0, 0));
		gameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		gameLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
	}

	//Sets up create lobby page and all related panel/headers/labels/buttons
	private void createLobby() {
		createLobbySuperPanel = new JPanel();
		frame.getContentPane().add(createLobbySuperPanel, BorderLayout.CENTER);
		createLobbySuperPanel.setLayout(null);

		JPanel headingPanel = new JPanel();
		headingPanel.setBounds(46, 36, 417, 108);
		createLobbySuperPanel.add(headingPanel);
		headingPanel.setLayout(null);

		JLabel gameLabel = new JLabel("FLASHPOINT");
		gameLabel.setBounds(0, 0, 405, 69);
		headingPanel.add(gameLabel);
		gameLabel.setForeground(new Color(255, 0, 0));
		gameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		gameLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));

		JPanel modePanel = new JPanel();
		modePanel.setBounds(557, 290, 230, 54);
		createLobbySuperPanel.add(modePanel);
		modePanel.setLayout(null);

		JToggleButton familyBtn = new JToggleButton("Family");
		familyBtn.setBounds(0, 25, 117, 29);
		modePanel.add(familyBtn);

		JToggleButton expBtn = new JToggleButton("Experienced");
		expBtn.setBounds(113, 25, 117, 29);
		modePanel.add(expBtn);

		ButtonGroup modeOpts = new ButtonGroup();
		modeOpts.add(familyBtn);
		modeOpts.add(expBtn);

		JLabel modeLabel = new JLabel("Mode");
		modeLabel.setBounds(6, 0, 209, 20);
		modePanel.add(modeLabel);
		modeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		modeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel diffPanel = new JPanel();
		diffPanel.setLayout(null);
		diffPanel.setBounds(508, 390, 342, 54);
		createLobbySuperPanel.add(diffPanel);

		JLabel diffLabel = new JLabel("Difficulty");
		diffLabel.setHorizontalAlignment(SwingConstants.CENTER);
		diffLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		diffLabel.setBounds(6, 0, 336, 20);
		diffPanel.add(diffLabel);

		JToggleButton recruitBtn = new JToggleButton("Recruit");
		recruitBtn.setBounds(0, 25, 117, 29);
		diffPanel.add(recruitBtn);

		JToggleButton veteranBtn = new JToggleButton("Veteran");
		veteranBtn.setBounds(113, 25, 117, 29);
		diffPanel.add(veteranBtn);

		JToggleButton heroicBtn = new JToggleButton("Heroic");
		heroicBtn.setBounds(225, 25, 117, 29);
		diffPanel.add(heroicBtn);

		ButtonGroup diffOpts = new ButtonGroup();
		diffOpts.add(recruitBtn);
		diffOpts.add(veteranBtn);
		diffOpts.add(heroicBtn);

		JPanel rulePanel = new JPanel();
		rulePanel.setBounds(400, 493, 202, 98);
		createLobbySuperPanel.add(rulePanel);
		rulePanel.setLayout(null);

		JLabel rulesLabel = new JLabel("Additional Rules");
		rulesLabel.setBounds(6, 6, 116, 16);
		rulePanel.add(rulesLabel);

		JCheckBox hazardBox = new JCheckBox("Unmarked hazards");
		hazardBox.setBounds(6, 23, 169, 23);
		rulePanel.add(hazardBox);

		JCheckBox strikeBox = new JCheckBox("Murphy strikes");
		strikeBox.setBounds(6, 42, 128, 23);
		rulePanel.add(strikeBox);

		JCheckBox checkBox = new JCheckBox("No fire");
		checkBox.setBounds(6, 69, 128, 23);
		rulePanel.add(checkBox);

		JPanel plyrPanel = new JPanel();
		plyrPanel.setLayout(null);
		plyrPanel.setBounds(735, 493, 202, 71);
		createLobbySuperPanel.add(plyrPanel);

		JLabel plyrLabel = new JLabel("Number of Players :- 0");
		plyrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		plyrLabel.setBounds(6, 6, 190, 16);
		plyrPanel.add(plyrLabel);

		JSlider plyrSlider = new JSlider();
		plyrSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				plyrLabel.setText("Number of Players :- " + plyrSlider.getValue());
			}
		});
		plyrSlider.setPaintTicks(true);
		plyrSlider.setValue(0);
		plyrSlider.setMaximum(6);
		plyrSlider.setBounds(10, 28, 190, 29);
		plyrPanel.add(plyrSlider);

		JLabel createLobbyLabel = new JLabel("Create Lobby");
		createLobbyLabel.setBounds(56, 156, 216, 71);
		createLobbySuperPanel.add(createLobbyLabel);
		createLobbyLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JButton createBtn = new JButton("CREATE");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				lobbyPage(); #To be implemented by James
				createLobbyPage();
				
			}
		});
		createBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		createBtn.setBounds(617, 623, 140, 54);
		createLobbySuperPanel.add(createBtn);
	}
	
	
	
	
	private void createLobbyPage() {
		createLobbySuperPanel.setVisible(false);
		
		
		lobbyPageSuperPanel = new JPanel();
		lobbyPageSuperPanel.setBounds(70, 44, 1126, 746);
		loginSuperPanel.add(lobbyPageSuperPanel, BorderLayout.CENTER);
		lobbyPageSuperPanel.setLayout(null);
		
		
		lobbyPageSuperPanel.setVisible(true);
		
		
		panel_1 = new JPanel();
		panel_1.setBounds(695, 10, 337, 576);
		lobbyPageSuperPanel.add(panel_1);
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
					//sendMessage();
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
				//sendMessage();
			}
		});
		btnChat.setBounds(288, 536, 50, 40);
		panel_1.add(btnChat);
		btnChat.setFont(new Font("Tahoma", Font.PLAIN, 20));

		btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//startGame();
			}
		});
		btnStartGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartGame.setBounds(496, 508, 176, 78);
		lobbyPageSuperPanel.add(btnStartGame);

		btnBackToMain = new JButton("Back");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//mainMenu.setVisible(true);
				//dispose();
			}
		});
		btnBackToMain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBackToMain.setBounds(10, 508, 192, 78);
		lobbyPageSuperPanel.add(btnBackToMain);

		panel = new JPanel();
		panel.setBounds(10, 10, 662, 372);
		lobbyPageSuperPanel.add(panel);
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
		lobbyPageSuperPanel.add(panelLobbyDescription);
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

		
	}
	
	
}	// END
