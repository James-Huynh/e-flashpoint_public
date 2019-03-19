package custom_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import lobby.Lobby;
import personalizedlisteners.createLobbyListeners.BackListener;
import personalizedlisteners.createLobbyListeners.CreateListener;
import personalizedlisteners.lobbyListeners.SearchEntrySetUpListener;
/**
 * @author zaidyahya
 *Panel for create lobby page
 */
public class CreateLobbyPanel extends JPanel {

	private static ClientManager clientManager;

	private JPanel headerPanel;
	private JLabel headerLabel;
	private JLabel createLobbyLabel;

	private JButton createBtn;
	private JButton backBtn;

	private JPanel modePanel;
	private ButtonGroup modeOpts;
	private JToggleButton familyBtn;
	private JToggleButton expBtn;
	private JLabel modeLabel;

	private JPanel difficultyPanel;
	private JLabel diffLabel;
	private ButtonGroup diffOpts;
	private JToggleButton recruitBtn;
	private JToggleButton veteranBtn;
	private JToggleButton heroicBtn;

	private JPanel rulesPanel;
	private JLabel rulesLabel;
	private JCheckBox hazardBox;
	private JCheckBox strikeBox;
	private JCheckBox checkBox;

	private JPanel playerPanel;
	private JLabel plyrLabel;
	private JSlider plyrSlider;

	private int nbPlayers;
	private String gameMode;
	private String gameDifficulty = "notSelected";
	private String lobbyName = "Loren Ipsum";

	private Lobby lobby;

	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();

	
	public CreateLobbyPanel(Dimension panelDimension, ClientManager clientManager) {
		//setPreferredSize(panelDimension);  /* Not working */
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);


		CreateLobbyPanel.clientManager = clientManager;

		createCreateButton();
		createBackButton();
		createHeaderPanel();
		createModePanel();
		createDifficultyPanel();
		createRulesPanel();
		createPlayerPanel();
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setBounds(6,18,533,107);
		this.add(headerPanel);
		createHeader();
		createSubHeader();
	}

	private void createHeader() {
		headerLabel = new JLabel("FLASHPOINT");
		headerLabel.setBounds(6, 6, 533, 84);
		headerLabel.setForeground(new Color(255, 0, 0));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
		headerPanel.add(headerLabel);	
	}

	private void createSubHeader() {
		createLobbyLabel = new JLabel("Create Lobby");
		createLobbyLabel.setBounds(56, 156, 216, 71);
		createLobbyLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
		this.add(createLobbyLabel);
	}

	private void createModePanel() {
		modePanel = new JPanel();
		modePanel.setBounds(316, 291, 230, 54);
		modePanel.setLayout(null);
		this.add(modePanel);

		createToggleButtons();
		createModeHeader();
	}

	private void createToggleButtons() {
		familyBtn = new JToggleButton("Family");
		familyBtn.setBounds(0, 25, 117, 29);
		familyBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					GameModeSelected (familyBtn);
				}
			}
		});
		modePanel.add(familyBtn);

		expBtn = new JToggleButton("Experienced");
		expBtn.setBounds(113, 25, 117, 29);
		expBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					GameModeSelected (expBtn);
				}
			}
		});
		modePanel.add(expBtn);

		modeOpts = new ButtonGroup();
		modeOpts.add(familyBtn);
		modeOpts.add(expBtn);
	}

	private void createModeHeader() {
		modeLabel = new JLabel("Mode");
		modeLabel.setBounds(6, 0, 209, 20);
		modeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		modeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modePanel.add(modeLabel);
	}

	private void GameModeSelected (JToggleButton button) {
		gameMode = button.getText();
	}
	
	private void GameDifficultySelected (JToggleButton button) {
		gameDifficulty = button.getText();
	}

	private void createDifficultyPanel() {
		difficultyPanel = new JPanel();
		difficultyPanel.setLayout(null);
		difficultyPanel.setBounds(254, 391, 342, 54);
		this.add(difficultyPanel);
		createDifficultyHeader();
		createDifficultyButtons();
	}

	private void createDifficultyHeader() {
		diffLabel = new JLabel("Difficulty");
		diffLabel.setHorizontalAlignment(SwingConstants.CENTER);
		diffLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		diffLabel.setBounds(6, 0, 336, 20);
		difficultyPanel.add(diffLabel);
	}

	private void createDifficultyButtons() {
		recruitBtn = new JToggleButton("Recruit");
		recruitBtn.setBounds(0, 25, 117, 29);
		recruitBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					GameDifficultySelected (recruitBtn);
				}
			}
		});
		difficultyPanel.add(recruitBtn);
		
		veteranBtn = new JToggleButton("Veteran");
		veteranBtn.setBounds(113, 25, 117, 29);
		veteranBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					GameDifficultySelected (veteranBtn);
				}
			}
		});
		difficultyPanel.add(veteranBtn);

		heroicBtn = new JToggleButton("Heroic");
		heroicBtn.setBounds(225, 25, 117, 29);
		heroicBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if(ev.getStateChange()==ItemEvent.SELECTED){
					GameDifficultySelected (heroicBtn);
				}
			}
		});
		difficultyPanel.add(heroicBtn);

		diffOpts = new ButtonGroup();
		diffOpts.add(recruitBtn);
		diffOpts.add(veteranBtn);
		diffOpts.add(heroicBtn);
	}

	private void createRulesPanel() {
		rulesPanel = new JPanel();
		rulesPanel.setBounds(139, 493, 202, 98);
		rulesPanel.setLayout(null);
		this.add(rulesPanel);
		createRulesHeader();
		createCheckBoxes();
	}

	private void createRulesHeader() {
		rulesLabel = new JLabel("Additional Rules");
		rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rulesLabel.setBounds(36, 6, 116, 16);
		rulesPanel.add(rulesLabel);
	}

	private void createCheckBoxes() {
		hazardBox = new JCheckBox("Unmarked hazards");
		hazardBox.setBounds(6, 23, 169, 23);
		rulesPanel.add(hazardBox);

		strikeBox = new JCheckBox("Murphy strikes");
		strikeBox.setBounds(6, 42, 128, 23);
		rulesPanel.add(strikeBox);

		checkBox = new JCheckBox("No fire");
		checkBox.setBounds(6, 61, 128, 23);
		rulesPanel.add(checkBox);
	}

	private void createPlayerPanel() {
		playerPanel = new JPanel();
		playerPanel.setLayout(null);
		playerPanel.setBounds(519, 493, 202, 71);
		this.add(playerPanel);

		createPlayerHeader();
		createSlider();
	}

	private void createPlayerHeader() {
		plyrLabel = new JLabel("Number of Players :- 0");
		plyrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		plyrLabel.setBounds(6, 6, 190, 16);
		playerPanel.add(plyrLabel);
	}

	private void createSlider() {
		plyrSlider = new JSlider();
		plyrSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				nbPlayers = plyrSlider.getValue();
				plyrLabel.setText("Number of Players :- " + nbPlayers);
			}
		});
		plyrSlider.setPaintTicks(true);
		plyrSlider.setValue(0);
		plyrSlider.setMaximum(6);
		plyrSlider.setBounds(10, 28, 190, 29);
		playerPanel.add(plyrSlider);
	}


	// @James
	private void createCreateButton() {
		createBtn = new JButton("CREATE");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(createLobbyRequest()) {
					System.out.println("create successful");
					raiseEventCreateBtn();
				}
			}
		});
		createBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		createBtn.setBounds(561, 623, 140, 54);
		this.add(createBtn);
	}

	private void createBackButton() {
		backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventBackBtn();
			}
		});
		backBtn.setFont(new Font("Lao MN", Font.PLAIN, 22));
		backBtn.setBounds(188, 623, 140, 54);
		this.add(backBtn);
	}

	// James
	private boolean createLobbyRequest() {
		return clientManager.createLobbyRequest(lobbyName, gameMode, nbPlayers, gameDifficulty);
	}

	public void addSelectionPiecesListenerListener(CreateListener obj) {
		REGISTERED_OBJECTS.add(CreateListener.class, obj);
	}

	public void addSelectionPiecesListenerListener(BackListener obj) {
		REGISTERED_OBJECTS.add(BackListener.class, obj);
	}

	/**
	 * Raise an event: the create button has been clicked
	 */
	private void raiseEventCreateBtn() {
		for (CreateListener listener: REGISTERED_OBJECTS.getListeners(CreateListener.class)) {
			listener.clickCreate();
		}
	}
	/**
	 * Raise an event: the back button has been clicked
	 */
	private void raiseEventBackBtn() {
		for (BackListener listener: REGISTERED_OBJECTS.getListeners(BackListener.class)) {
			listener.clickBack();
		}
	}

}
