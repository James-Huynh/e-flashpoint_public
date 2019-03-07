package custom_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import lobby.Lobby;
import personalizedlisteners.lobbyListeners.StartListener;
import personalizedlisteners.lobbyListeners.LeaveListener;
/**
 * Panel for Lobby Page
 * @author zaidyahya
 */
public class LobbyPanel extends JPanel {
	
	private Lobby targetLobby; /*something needs to hold an instance of lobby (GameManager?)
							   this instance can then be called (getActiveLobby) or be passed in
							   it'll then be used here to initialize fields (e.g. getPlayers) - Zaid*/
   	private JButton startBtn;
	private JButton leaveBtn;
	
	private JPanel headerPanel;
	private JLabel headerLabel;
	
	private JPanel lobbyDescPanel;
	private JTextArea textRules;
	private JTextArea textMode;
	private JTextArea textDifficulty;
	
	private JPanel playersPanel;
	private JLabel playerOne;
	private JLabel playerTwo;
	private JLabel playerThree;
	private JLabel playerFour;
	private JLabel playerFive;
	private JLabel playerSix;
	
	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();

	public LobbyPanel(Dimension panelDimension) {
		//setPreferredSize(panelDimension);  /* Not working */
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);
		
		createHeaderPanel();
		createStartButton();
		createLeaveButton();
		createLobbyDescription();
		createPlayersPanel();
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
		leaveBtn = new JButton("LEAVE");
		leaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventLeaveBtn();
			}
		});
		leaveBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		leaveBtn.setBounds(177, 682, 140, 54);
		this.add(leaveBtn);
	}
	
	private void createLobbyDescription() {
		lobbyDescPanel = new JPanel();
		lobbyDescPanel.setBounds(123, 563, 662, 84);
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
		
		textMode = new JTextArea("Mode:");
		textMode.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textMode.setBounds(220, 0, 200, 100);
		textMode.setEditable(false);
		textMode.setLineWrap(true);
		lobbyDescPanel.add(textMode);
		
		textDifficulty = new JTextArea("Difficulty:");
		textDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textDifficulty.setBounds(440, 0, 200, 100);
		textDifficulty.setEditable(false);
		textDifficulty.setLineWrap(true);
		lobbyDescPanel.add(textDifficulty);
	}
	
	private void createPlayersPanel() {
		playersPanel = new JPanel();
		playersPanel.setBounds(105, 172, 662, 330);
		playersPanel.setLayout(null);
		this.add(playersPanel);
		
		createPlayers();
	}
	
	private void createPlayers() {
		/**
		 * setting will be done based on the Lobby object i.e. Lobby.getPlayerslist()
		 * parameter below would be String as well
		 */
		for(int i=0;i<6;i++) {
			setPlayer(i);
		}
	}
	
	private void setPlayer(int i) {
		switch(i) {
		case 0:
			playerOne = new JLabel("James Huynh");
			playerOne.setOpaque(true);
			playerOne.setBackground(Color.YELLOW);
			playerOne.setHorizontalAlignment(SwingConstants.CENTER);
			playerOne.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerOne.setBounds(0, 0, 662, 43);
			playersPanel.add(playerOne);
			break;
		case 1:
			playerTwo = new JLabel("Cao Ruoycu");
			playerTwo.setOpaque(true);
			playerTwo.setHorizontalAlignment(SwingConstants.CENTER);
			playerTwo.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerTwo.setBackground(Color.MAGENTA);
			playerTwo.setBounds(0, 55, 662, 43);
			playersPanel.add(playerTwo);
			break;
		case 2:
			playerThree = new JLabel("Ben Ruddock");
			playerThree.setOpaque(true);
			playerThree.setHorizontalAlignment(SwingConstants.CENTER);
			playerThree.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerThree.setBackground(Color.GREEN);
			playerThree.setBounds(0, 110, 662, 43);
			playersPanel.add(playerThree);
			break;
		case 3:
			playerFour = new JLabel("Junha Park");
			playerFour.setOpaque(true);
			playerFour.setHorizontalAlignment(SwingConstants.CENTER);
			playerFour.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerFour.setBackground(Color.RED);
			playerFour.setBounds(0, 165, 662, 43);
			playersPanel.add(playerFour);
			break;
		case 4:
			playerFive = new JLabel("Mateusz Pyla");
			playerFive.setOpaque(true);
			playerFive.setHorizontalAlignment(SwingConstants.CENTER);
			playerFive.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerFive.setBackground(Color.WHITE);
			playerFive.setBounds(0, 220, 662, 43);
			playersPanel.add(playerFive);
			break;
		case 5:
			playerSix = new JLabel("Zaid Yahya");
			playerSix.setOpaque(true);
			playerSix.setHorizontalAlignment(SwingConstants.CENTER);
			playerSix.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerSix.setBackground(Color.ORANGE);
			playerSix.setBounds(0, 275, 662, 43);
			playersPanel.add(playerSix);
			break;
		}	
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
		for (StartListener listener: REGISTERED_OBJECTS.getListeners(StartListener.class)) {
			listener.clickStart();
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
}
