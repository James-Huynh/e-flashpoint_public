import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RunFlashPoint {

	private JFrame frame;
	//Define big panels corresponding to each 'page'
	private JPanel loginSuperPanel;
	private JPanel startMenuSuperPanel;
	private JPanel createLobbySuperPanel;
	private JPanel findLobbySuperPanel; 

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
//		login();s
//		startMenu();  //Use this space to write the method (i.e. the panel) you want to be displayed in 'Design'
//		createLobby(); //In final version (with current design), this method will only have the first two lines
		findLobby();
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
				startMenuSuperPanel.setVisible(false);
				frame.getContentPane().remove(startMenuSuperPanel);
				findLobby(); 
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
			}
		});
		createBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		createBtn.setBounds(764, 623, 140, 54);
		createLobbySuperPanel.add(createBtn);
		
		JButton backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createLobbySuperPanel.setVisible(false);
				frame.getContentPane().remove(createLobbySuperPanel);
				startMenu();
			}
		});
		backBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		backBtn.setBounds(432, 623, 140, 54);
		createLobbySuperPanel.add(backBtn);
	}
	
	private void findLobby() {
		findLobbySuperPanel = new JPanel();
		frame.getContentPane().add(findLobbySuperPanel, BorderLayout.CENTER);
		findLobbySuperPanel.setLayout(null);
		
		JPanel headingPanel = new JPanel();
		headingPanel.setBounds(46, 36, 417, 108);
		findLobbySuperPanel.add(headingPanel);
		headingPanel.setLayout(null);
		
		JLabel gameLabel = new JLabel("FLASHPOINT");
		gameLabel.setBounds(0, 0, 405, 69);
		headingPanel.add(gameLabel);
		gameLabel.setForeground(new Color(255, 0, 0));
		gameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		gameLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
		
		JLabel findLobbyLabel = new JLabel("Select a lobby");
		findLobbyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		findLobbyLabel.setBounds(593, 202, 216, 53);
		findLobbySuperPanel.add(findLobbyLabel);
		findLobbyLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
		
		JPanel lobbiesPanel = new JPanel();
		lobbiesPanel.setBounds(347, 267, 696, 392);
		findLobbySuperPanel.add(lobbiesPanel);
		lobbiesPanel.setLayout(null);
		
		JPanel lobbyOne = new JPanel();
		lobbyOne.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				lobbyPage();
			}
		});
		lobbyOne.setBackground(UIManager.getColor("ToolTip.background"));
		lobbyOne.setBounds(0, 57, 696, 68);
		lobbiesPanel.add(lobbyOne);
		lobbyOne.setLayout(null);
		
		JLabel lobOneName = new JLabel("The Killaz");
		lobOneName.setBounds(6, 6, 160, 38);
		lobOneName.setOpaque(true);
		lobbyOne.add(lobOneName);
		lobOneName.setBackground(new Color(255, 127, 80));
		lobOneName.setFont(new Font("Menlo", Font.ITALIC, 25));
		
		JLabel lobOneHost = new JLabel("Host: matwillsaveya");
		lobOneHost.setHorizontalAlignment(SwingConstants.CENTER);
		lobOneHost.setOpaque(true);
		lobOneHost.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobOneHost.setBackground(new Color(255, 215, 0));
		lobOneHost.setBounds(6, 48, 147, 14);
		lobbyOne.add(lobOneHost);
		
		JLabel lobOneMode = new JLabel("Mode: Legendary");
		lobOneMode.setOpaque(true);
		lobOneMode.setHorizontalAlignment(SwingConstants.CENTER);
		lobOneMode.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobOneMode.setBackground(new Color(255, 215, 0));
		lobOneMode.setBounds(180, 48, 147, 14);
		lobbyOne.add(lobOneMode);
		
		JLabel lobOnePlayers = new JLabel("Players: 5/6");
		lobOnePlayers.setOpaque(true);
		lobOnePlayers.setHorizontalAlignment(SwingConstants.CENTER);
		lobOnePlayers.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobOnePlayers.setBackground(new Color(255, 215, 0));
		lobOnePlayers.setBounds(365, 48, 147, 14);
		lobbyOne.add(lobOnePlayers);
		
		JLabel lobOneSpec = new JLabel("Speciality: Available");
		lobOneSpec.setOpaque(true);
		lobOneSpec.setHorizontalAlignment(SwingConstants.CENTER);
		lobOneSpec.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobOneSpec.setBackground(new Color(255, 215, 0));
		lobOneSpec.setBounds(537, 48, 153, 14);
		lobbyOne.add(lobOneSpec);
		
		JPanel lobbyTwo = new JPanel();
		lobbyTwo.setLayout(null);
		lobbyTwo.setBackground(UIManager.getColor("ToolTip.background"));
		lobbyTwo.setBounds(0, 137, 696, 68);
		lobbiesPanel.add(lobbyTwo);
		
		JLabel lobTwoName = new JLabel("Fire in the Hole");
		lobTwoName.setOpaque(true);
		lobTwoName.setFont(new Font("Menlo", Font.ITALIC, 25));
		lobTwoName.setBackground(new Color(255, 127, 80));
		lobTwoName.setBounds(6, 6, 252, 38);
		lobbyTwo.add(lobTwoName);
		
		JLabel lobTwoHost = new JLabel("Host: caotherescuer");
		lobTwoHost.setOpaque(true);
		lobTwoHost.setHorizontalAlignment(SwingConstants.CENTER);
		lobTwoHost.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobTwoHost.setBackground(new Color(255, 215, 0));
		lobTwoHost.setBounds(6, 48, 147, 14);
		lobbyTwo.add(lobTwoHost);
		
		JLabel lobTwoMode = new JLabel("Mode: Family");
		lobTwoMode.setOpaque(true);
		lobTwoMode.setHorizontalAlignment(SwingConstants.CENTER);
		lobTwoMode.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobTwoMode.setBackground(new Color(255, 215, 0));
		lobTwoMode.setBounds(180, 48, 147, 14);
		lobbyTwo.add(lobTwoMode);
		
		JLabel lobTwoPlayers = new JLabel("Players: FULL");
		lobTwoPlayers.setOpaque(true);
		lobTwoPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		lobTwoPlayers.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobTwoPlayers.setBackground(new Color(255, 215, 0));
		lobTwoPlayers.setBounds(365, 48, 147, 14);
		lobbyTwo.add(lobTwoPlayers);
		
		JLabel lobTwoSpec = new JLabel("Speciality: None");
		lobTwoSpec.setOpaque(true);
		lobTwoSpec.setHorizontalAlignment(SwingConstants.CENTER);
		lobTwoSpec.setFont(new Font("Menlo", Font.ITALIC, 12));
		lobTwoSpec.setBackground(new Color(255, 215, 0));
		lobTwoSpec.setBounds(537, 48, 153, 14);
		lobbyTwo.add(lobTwoSpec);
		
		JPanel lobbyThree = new JPanel();
		lobbyThree.setLayout(null);
		lobbyThree.setBackground(UIManager.getColor("ToolTip.background"));
		lobbyThree.setBounds(0, 217, 696, 68);
		lobbiesPanel.add(lobbyThree);
		
		JPanel lobbyFour = new JPanel();
		lobbyFour.setLayout(null);
		lobbyFour.setBackground(UIManager.getColor("ToolTip.background"));
		lobbyFour.setBounds(0, 297, 696, 68);
		lobbiesPanel.add(lobbyFour);
		
		JButton backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findLobbySuperPanel.setVisible(false);
				frame.getContentPane().remove(findLobbySuperPanel);
				startMenu();
			}
		});
		backBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		backBtn.setBounds(609, 692, 140, 54);
		findLobbySuperPanel.add(backBtn);


		
		
	}
}
