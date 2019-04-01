package custom_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import lobby.Lobby;
import personalizedlisteners.lobbyListeners.LeaveListener;
import personalizedlisteners.lobbyListeners.StartListener;
import server.Player;
/**
 * Panel for Lobby Page
 *
 */
public class LobbyPanel extends JPanel {
	
	private Lobby targetLobby; /*something needs to hold an instance of lobby (GameManager?)
							   this instance can then be called (getActiveLobby) or be passed in
							   it'll then be used here to initialize fields (e.g. getPlayers) - Zaid*/
	private ClientManager clientManager;
   	private JButton startBtn;
	private JButton leaveBtn;
	
	private JPanel headerPanel;
	private JLabel headerLabel;
	
	private JPanel lobbyDescPanel;
	private JTextArea textRules;
	private JTextArea textGameDesc;
	
	private ChatBox chatBox;
	private JPanel chatPanel;
	
	private JPanel playersPanel;
	private ArrayList<Player> currPlayerList;
	private ArrayList<Player> newPlayerList;
	
	private JLabel[] playersLabel;
	private JLabel playerOne, playerTwo, playerThree, playerFour, playerFive, playerSix;
	private int myFlagger = -1;
	
	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();

	public LobbyPanel(Dimension panelDimension, ClientManager myclientManager) {
		//setPreferredSize(panelDimension);  /* Not working */
		this.clientManager = myclientManager;
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);
		this.playersLabel = new JLabel[6];
		
		initialize();
		createHeaderPanel();
		createStartButton();
		createLeaveButton();
		createLobbyDescription();
		createPlayersPanel();
		createChatBox();
	}

	// James
	private void initialize() {
		
		
		targetLobby = clientManager.getLobby();
//		createPlayers();
	}
	
	
	private void createChatBox() {
		chatBox = new ChatBox(300, 300, clientManager);
		chatPanel = chatBox.getPanel_main();
		chatPanel.setLocation(new Point(500, 50));
		this.add(chatPanel);
	}
	
	
	private void createHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setBounds(6,18,533,107);
		this.add(headerPanel);
		createHeader();
	}
	
	private void createHeader() {
		headerLabel = new JLabel("FLASHPOINT");
		headerLabel.setBounds(6, 6, 533, 84);
		headerLabel.setForeground(new Color(255, 0, 0));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
		headerPanel.add(headerLabel);	
	}
	
	private void createStartButton() {
		startBtn = new JButton("START");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*Server request with selected details*/
				raiseEventStartBtn();
			}
		});
		startBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		startBtn.setBounds(482, 682, 140, 54);
		this.add(startBtn);
		
	}
	
	private void createLeaveButton() {
		leaveBtn = new JButton("REFRESH");
		leaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listen();
				raiseEventLeaveBtn();
			}
		});
		leaveBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		leaveBtn.setBounds(177, 682, 140, 54);
		this.add(leaveBtn);
	}
	
	private void createLobbyDescription() {
		lobbyDescPanel = new JPanel();
		lobbyDescPanel.setBounds(195, 563, 426, 84);
		lobbyDescPanel.setLayout(null);
		this.add(lobbyDescPanel);
		
		createTextBoxes();
	}
	
	private void createTextBoxes() {
		
		textRules = new JTextArea("Rules: ");
		textRules.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textRules.setBounds(0, 0, 200, 100);
		textRules.setEditable(false);
		textRules.setLineWrap(true);
		lobbyDescPanel.add(textRules);
		
		String mode = "Mode: " + targetLobby.getMode();
		String difficulty = "Difficulty: " + targetLobby.getDifficulty();
		String board = "Board: " + targetLobby.getBoard();
		textGameDesc = new JTextArea(mode + "\n" + difficulty + "\n" + board);
//		textGameDesc = new JTextArea(mode + "\n" + "hello");
		textGameDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textGameDesc.setBounds(220, 0, 200, 100);
		textGameDesc.setEditable(false);
		textGameDesc.setLineWrap(true);
		lobbyDescPanel.add(textGameDesc);
	}
	
	private void createPlayersPanel() {
		playersPanel = new JPanel();
		playersPanel.setBounds(16, 142, 662, 386);
		playersPanel.setLayout(null);
		this.add(playersPanel);
		
		createPlayers();
	}
	
	private void createPlayers() {
		/**
		 * setting will be done based on the Lobby object i.e. Lobby.getPlayerslist()
		 * parameter below would be String as well
		 */
		for(int i=0;i<targetLobby.getPlayers().size();i++) {
			setPlayer(i);
		}
		
		System.out.println("tester for create lobby" + targetLobby.getPlayers().size());
		
	}
	
	private void setPlayer(int i) {
		switch(i) {
		
		
		
		case 0:
			String playerOneName = targetLobby.getPlayers().get(i).getUserName();
			playerOne = new JLabel(playerOneName);
			playerOne.setOpaque(true);
			playerOne.setBackground(Color.GREEN);
			playerOne.setHorizontalAlignment(SwingConstants.CENTER);
			playerOne.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerOne.setBounds(0, 0, 662, 43);
			playersPanel.add(playerOne);
			playersLabel[i] = playerOne;
			break;
			
		case 1:
			System.out.println("Rewriting!!!");
			String playerTwoName = targetLobby.getPlayers().get(i).getUserName();
			playerTwo = new JLabel(playerTwoName);
			playerTwo.setOpaque(true);
			playerTwo.setHorizontalAlignment(SwingConstants.CENTER);
			playerTwo.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerTwo.setBackground(Color.GRAY);
			playerTwo.setBounds(0, 55, 662, 43);
			playersPanel.add(playerTwo);
			playersLabel[i] = playerTwo;
			break;
			
		case 2:
			String playerThreeName = targetLobby.getPlayers().get(i).getUserName();
			playerThree = new JLabel(playerThreeName);
			playerThree.setOpaque(true);
			playerThree.setHorizontalAlignment(SwingConstants.CENTER);
			playerThree.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerThree.setBackground(Color.WHITE);
			playerThree.setBounds(0, 110, 662, 43);
			playersPanel.add(playerThree);
			playersLabel[i] = playerThree;
			break;
			
		case 3:
			String playerFourName = targetLobby.getPlayers().get(i).getUserName();
			playerFour = new JLabel(playerFourName);
			playerFour.setOpaque(true);
			playerFour.setHorizontalAlignment(SwingConstants.CENTER);
			playerFour.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerFour.setBackground(Color.RED);
			playerFour.setBounds(0, 165, 662, 43);
			playersPanel.add(playerFour);
			playersLabel[i] = playerFour;
			break;
			
		case 4:
			String playerFiveName = targetLobby.getPlayers().get(i).getUserName();
			playerFive = new JLabel(playerFiveName);
			playerFive.setOpaque(true);
			playerFive.setHorizontalAlignment(SwingConstants.CENTER);
			playerFive.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerFive.setBackground(Color.MAGENTA);
			playerFive.setBounds(0, 220, 662, 43);
			playersPanel.add(playerFive);
			playersLabel[i] = playerFive;
			break;
			
		case 5:
			String playerSixName = targetLobby.getPlayers().get(i).getUserName();
			playerSix = new JLabel(playerSixName);
			playerSix.setOpaque(true);
			playerSix.setHorizontalAlignment(SwingConstants.CENTER);
			playerSix.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerSix.setBackground(Color.BLUE);
			playerSix.setBounds(0, 275, 662, 43);
			playersPanel.add(playerSix);
			playersLabel[i] = playerSix;
			break;
		}	
		
	}
	
	
	// James
	/**
	 * Updates the Lobby Panel to display changes from the lobby object
	 * @param newLobby Updated Lobby object from the server
	 */
	private void updateLobby(Lobby newLobby) {
		newPlayerList = newLobby.getPlayers();
		currPlayerList = newPlayerList;
		
		targetLobby = newLobby; //added by Zaid
//		refreshDisplay();
	}
	
	// James
	private void resetLabels() {
		for (JLabel lbl : playersLabel) {
			lbl = null;
		}
	}
	
	// James
	public void refreshDisplay() {
		JLabel currLabel;
		Player currPlayer;
		
		updateLobby(clientManager.getLobby());
		resetLabels();
		createPlayers();
		chatBox.refreshChatBox();
		add(chatBox);
		
		
		
//		revalidate();
//		for (int i = 0; i < currPlayerList.size(); i++) {
//			currLabel = playersLabel[i];
//			currPlayer = currPlayerList.get(i);
//			
//			
//			currLabel.setText(currPlayer.getUserName());
//			setPlayer(i);
//		}
	}

	public void addSelectionPiecesListenerListener(StartListener obj) {
		REGISTERED_OBJECTS.add(StartListener.class, obj);
	}
	
	public void addSelectionPiecesListenerListener(LeaveListener obj) {
		REGISTERED_OBJECTS.add(LeaveListener.class, obj);
	}
	
	/**
	 * Raise an event: the start button has been clicked
	 */
	private void raiseEventStartBtn() {
		System.out.println("Start button clicked to start game wohoo");
		for (StartListener listener: REGISTERED_OBJECTS.getListeners(StartListener.class)) {
			if(myFlagger == 2) {
				listener.clickStart(true);
			}
			else {
				listener.clickStart(false);
			}
			
		}
	}
	/**
	 * Raise an event: the leave button has been clicked
	 */
	private void raiseEventLeaveBtn() {
		for (LeaveListener listener: REGISTERED_OBJECTS.getListeners(LeaveListener.class)) {
			listener.clickLeave();
		}
	}
	
	public void listen() {
		System.out.println("I'm in listen");
		int flag = 0;
		while(flag == 0) {
			flag = clientManager.listenForResponses();
			if(flag == 1) {
				updateLobby(clientManager.getLobby());
			}
			else if(flag == 2) {
				myFlagger = flag;
//				raiseEventStartBtn();
				raiseEventLeaveBtn();
				startBtn.doClick();
			}
		}
		
	}
}
