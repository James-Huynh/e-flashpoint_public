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
import javax.swing.JTextArea;
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
	private JPanel panel_PlayersDisplay;
	private JButton btnBackToMain;
	private JButton btnStartGame;
	private JTextArea textAreaChat;
	private JPanel panelLobbyDescription;
	private JTextArea textAreaRules;
	private JTextArea textAreaMode;
	private JTextArea textAreaDifficulty;
	private JTextField txtFieldChat;
	private JPanel panel_chat;
	private JButton btnChat;
	private JLabel lblPlayer1;
	private JLabel lblPlayer2;
	private JLabel lblPlayer3;
	private JLabel lblPlayer4;
	private JLabel lblPlayer5;
	private JLabel lblPlayer6;
	
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
				createLobbySuperPanel.setVisible(false);
				frame.getContentPane().remove(createLobbySuperPanel);
				createLobbyPage();
				
			}
		});
		createBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		createBtn.setBounds(617, 623, 140, 54);
		createLobbySuperPanel.add(createBtn);
	}
	
	
	
	
	private void createLobbyPage() {
		int offsetX = 100;
		int offsetY = 100;
		//int playerSpacing = 50;
		
		
		lobbyPageSuperPanel = new JPanel();
		frame.getContentPane().add(lobbyPageSuperPanel, BorderLayout.CENTER);
		lobbyPageSuperPanel.setLayout(null);

		JPanel headingPanel = new JPanel();
		headingPanel.setBounds(46, 36, 417, 108);
		lobbyPageSuperPanel.add(headingPanel);
		headingPanel.setLayout(null);

		JLabel gameLabel = new JLabel("FLASHPOINT");
		gameLabel.setBounds(0, 0, 405, 69);
		headingPanel.add(gameLabel);
		gameLabel.setForeground(new Color(255, 0, 0));
		gameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		gameLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
		
		
		//==================================CHAT_START=====================================
		panel_chat = new JPanel();
		panel_chat.setBounds(695 + offsetX, 10 + offsetY, 337, 576);
		lobbyPageSuperPanel.add(panel_chat);
		panel_chat.setLayout(null);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 337, 537);
		panel_chat.add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaChat = new JTextArea("");
		textAreaChat.setEditable(false);
		textAreaChat.setLineWrap(true);
		scrollPane.setViewportView(textAreaChat);
		textAreaChat.setFont(new Font("Tahoma", Font.PLAIN, 22));

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
		panel_chat.add(txtFieldChat);
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
		panel_chat.add(btnChat);
		btnChat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		//==================================CHAT_END=====================================

		
		btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		btnStartGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartGame.setBounds(496 + offsetX, 508 + offsetY, 176, 78);
		lobbyPageSuperPanel.add(btnStartGame);

		btnBackToMain = new JButton("Back");
		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backToFindLobby();
				//mainMenu.setVisible(true);
				//dispose();
			}
		});
		btnBackToMain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBackToMain.setBounds(10 + offsetX, 508 + offsetY, 192, 78);
		lobbyPageSuperPanel.add(btnBackToMain);

		//=====================================
		
		
		panel_PlayersDisplay = new JPanel();
		panel_PlayersDisplay.setBounds(10 + offsetX, 10 + offsetY, 662, 372);
		lobbyPageSuperPanel.add(panel_PlayersDisplay);
		panel_PlayersDisplay.setLayout(null);

		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayers.setBounds(0, 0, 73, 28);
		panel_PlayersDisplay.add(lblPlayers);
		lblPlayers.setFont(new Font("Tahoma", Font.PLAIN, 20));

		panelLobbyDescription = new JPanel();
		panelLobbyDescription.setBounds(10 + offsetX, 394 + offsetY, 662, 84);
		lobbyPageSuperPanel.add(panelLobbyDescription);
		panelLobbyDescription.setLayout(null);

		textAreaRules = new JTextArea("Rules: ");
		textAreaRules.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textAreaRules.setBounds(0, 0, 200, 100);
		textAreaRules.setEditable(false);
		textAreaRules.setLineWrap(true);
		panelLobbyDescription.add(textAreaRules);

		textAreaMode = new JTextArea("Mode:");
		textAreaMode.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textAreaMode.setBounds(220, 0, 200, 100);
		textAreaMode.setEditable(false);
		textAreaMode.setLineWrap(true);
		panelLobbyDescription.add(textAreaMode);

		textAreaDifficulty = new JTextArea("Difficulty:");
		textAreaDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textAreaDifficulty.setBounds(440, 0, 200, 100);
		textAreaMode.setEditable(false);
		textAreaDifficulty.setLineWrap(true);
		panelLobbyDescription.add(textAreaDifficulty);

		//===================================== Player Labels ============================
		lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setOpaque(true);
		lblPlayer1.setBackground(Color.YELLOW);
		lblPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer1.setBounds(0, 38, 662, 43);
		panel_PlayersDisplay.add(lblPlayer1);
		
		lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setOpaque(true);
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer2.setBackground(Color.MAGENTA);
		lblPlayer2.setBounds(0, 92, 662, 43);
		panel_PlayersDisplay.add(lblPlayer2);

		lblPlayer3 = new JLabel("Player 3");
		lblPlayer3.setOpaque(true);
		lblPlayer3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer3.setBackground(Color.GREEN);
		lblPlayer3.setBounds(0, 145, 662, 43);
		panel_PlayersDisplay.add(lblPlayer3);
		
		lblPlayer4 = new JLabel("Player 4");
		lblPlayer4.setOpaque(true);
		lblPlayer4.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer4.setBackground(Color.RED);
		lblPlayer4.setBounds(0, 195, 662, 43);
		panel_PlayersDisplay.add(lblPlayer4);

		lblPlayer5 = new JLabel("Player 5");
		lblPlayer5.setOpaque(true);
		lblPlayer5.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer5.setBackground(Color.WHITE);
		lblPlayer5.setBounds(0, 245, 662, 43);
		panel_PlayersDisplay.add(lblPlayer5);
		
		lblPlayer6 = new JLabel("Player 6");
		lblPlayer6.setOpaque(true);
		lblPlayer6.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayer6.setBackground(Color.ORANGE);
		lblPlayer6.setBounds(0, 295, 662, 43);
		panel_PlayersDisplay.add(lblPlayer6);
		//===================================== Player Labels ============================
		
		
		updateLobbyDescription();
	}

	
	//===================================== James Methods ============================
	/**
	 * StartGame will proceed to the next phase of the application which is the main game.
	 */
	private void startGame() {
		/*
		 * @Junha and @Cao, will call all the method that will set up the game board
		 * what to pass in parameter?
		 * what object to create?
		 */
		this.frame.dispose();
	}
	
	/**
	 * backToFindLobby will return to the main lobby while disposing of itself.
	 */
	private void backToFindLobby() {
		this.frame.remove(lobbyPageSuperPanel);
		startMenu();
		
	}

	/**
	 * sendMessage will read the typed local text and send the message to the server
	 */
	private void sendMessage() {
		String playerName = "James"; // game.state.sendServer
		String message = txtFieldChat.getText();
		textAreaChat.append("[" + playerName + "]: " + message + "\n");
		txtFieldChat.setText("");

	}

	/**
	 * updateLobbyDescription will set the lobby's display accordingly to the game state and setting chosen on the previous page
	 */
	private void updateLobbyDescription() {
		// would get a string or object from the parameter and simply append it
		textAreaRules.setText("Rules:\n");
		textAreaRules.append("- Turn limit: 200\n");
		textAreaRules.append("- The winner is crowned King of The North\n");
		textAreaRules.append("- The loser is crowned Loser of The South\n");
		
		textAreaMode.setText("Mode:\n");
		textAreaMode.append("- Family\n");
		textAreaMode.append("- Easter eggs\n");
		
		
		textAreaDifficulty.setText("Difficulty:\n");
		textAreaDifficulty.append("- Impossible\n");
	}
	
	//===================================== James Methods ============================
	
}	// END
