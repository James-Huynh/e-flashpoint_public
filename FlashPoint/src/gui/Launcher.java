package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SpringLayout;

import client.Client;
import client.ClientManager;
import commons.bean.User;
import custom_panels.CreateLobbyPanel;
import custom_panels.FindLobbyPanel;
import custom_panels.LobbyPanel;
import custom_panels.LoginPanel;
import custom_panels.MainMenuPanel;
import game.GameState;
import gui.Table.BoardPanel;
import gui.Table.LeftPanel;
import gui.Table.RightPanel;
import managers.GameManager;
import personalizedlisteners.createLobbyListeners.BackListener;
import personalizedlisteners.lobbyListeners.LeaveListener;
import personalizedlisteners.lobbyListeners.SearchEntrySetUpListener;
import personalizedlisteners.lobbyListeners.StartListener;
import personalizedlisteners.loginListeners.LoginListener;
import personalizedlisteners.mainMenuListeners.MainMenuListener;
/**
 * 
 * @author zaidyahya
 *The main launcher of the game. Responsible for inserting/removing panels
 *Holds a frame, and uses panels to display 'pages' (i.e. login/menu/game etc)
 */
public class Launcher {
	private String EricIP = "142.157.30.157";
	private String JamesIP = "142.157.105.75";
	private String JunhazIP = "142.157.65.31";
	private String ZaidIP = "142.157.145.202";
	private String BenIP = "142.157.58.216";
	private String MatIP = "142.157.63.60";
	
	private static Client client;
	private String ServerIP = BenIP;

	int port = 8888;
	User userOne = new User();
	private ClientManager clientManager;
	
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(1500,800);
	private final static Dimension CENTER_PANEL_DIMENSION = new Dimension(1000,800);
	private final static Dimension RIGHT_PANEL_DIMENSION = new Dimension(300,800);
	private final static Dimension LEFT_PANEL_DIMENSION = new Dimension(200,800);

	private JFrame motherFrame;
	private Container contentPane;
	private JMenuBar tableMenuBar = new JMenuBar();

	private LoginPanel login;
	private MainMenuPanel mainMenu;
	private CreateLobbyPanel createLobby;
	private FindLobbyPanel findLobby;
	private LobbyPanel lobby;
	private Table table;
	private clientThread listenerThread;

	// Saving and Loading stuff
	private String folderPath;
	
	
	//Used by Ben for in game testing. Not permanent.
	private static GameManager current;
	private static GameState tester;
	

	/**
	 * Below is for to get around a problem. Wanted to specify an outline for the frame
	 * And use that to pull in frames
	 * These are used to set up panels the size that we wanted and then we remove them
	 * before inserting a new in it's place (see setupLoginPage)
	 * NOT ideal -- need better way - Zaid
	 */
	private JPanel dummyCenterPanel = new JPanel(); 
	private JPanel dummyRightPanel = new JPanel();
	private JPanel dummyLeftPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher window = new Launcher();
					window.motherFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Launcher() {
		SpringLayout sl_dummyCenterPanel = new SpringLayout();
		dummyCenterPanel.setLayout(sl_dummyCenterPanel);
		userOne.setName("Zaid");
		userOne.setPassword("zzz");
		client = new Client(ServerIP, port);
		client.start();
		clientManager = new ClientManager(client.getClientInputThread(), client.getClientOutputThread(), this);
		listenerThread = new clientThread(this, clientManager, true);
		if(sendConnectionRequest()) {	
			initialize();
		};
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		motherFrame = new JFrame();
		contentPane = motherFrame.getContentPane(); // @Zaid : from James, you're welcome for shortcutting the call :))
		motherFrame.setBounds(100, 100, 450, 300);
		motherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		motherFrame.setSize(OUTER_FRAME_DIMENSION);
		motherFrame.setTitle("You're a god if you recognized this quote: \"Whether you think you can, or you think you can't--you're right.\"");
		contentPane.setLayout(new BorderLayout(0, 0));

		setupDummies();
		populateMenuBar();
		addMenuBar();
		setupLoginPage();

	}
	

	private void setupDummies() {
		dummyCenterPanel.setBackground(Color.YELLOW);
		dummyCenterPanel.setPreferredSize(CENTER_PANEL_DIMENSION);
		contentPane.add(dummyCenterPanel, BorderLayout.CENTER);

		dummyRightPanel.setBackground(Color.RED);
		dummyRightPanel.setPreferredSize(RIGHT_PANEL_DIMENSION);
		contentPane.add(dummyRightPanel, BorderLayout.EAST);

		dummyLeftPanel.setBackground(Color.GREEN);
		dummyLeftPanel.setPreferredSize(LEFT_PANEL_DIMENSION);
		contentPane.add(dummyLeftPanel, BorderLayout.WEST);
	}

	private void populateMenuBar() {
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createOptionsMenu());

	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");

		final JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sendSaveGameRequest()) {
					System.out.println("this is the print that board is refreshing");
				}
			}
		});
		
		
		fileMenu.add(saveMenuItem);

		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});

		fileMenu.add(exitMenuItem);
		return fileMenu;
	}

	private JMenu createOptionsMenu() {
		final JMenu optionsMenu = new JMenu("Options");
		final JMenuItem helpMenuItem = new JMenuItem("Help");
		optionsMenu.add(helpMenuItem);

		return optionsMenu;
	}

	private void addMenuBar() {
		this.motherFrame.setJMenuBar(this.tableMenuBar);
	}

	//	LOGIN -------------------------------
	private void setupLoginPage() {
		login = new LoginPanel(CENTER_PANEL_DIMENSION, clientManager);

		login.addSelectionPiecesListenerListener(new LoginListener() {
			@Override
			public void clickLogin() {
				login.setVisible(false);
				motherFrame.remove(login);
				setupMainMenuPage();

			}
			
			public void clickRegister() {
				System.out.println("Back in login page");
			}
		});
		
		contentPane.remove(dummyCenterPanel);
		contentPane.add(login, BorderLayout.CENTER);
	}

	//------------------------------- LOGIN
	

	//	MAIN MENU -------------------------------
	private void setupMainMenuPage() {
		mainMenu = new MainMenuPanel(clientManager);
		contentPane.add(mainMenu);
		
		mainMenu.addSelectionPiecesListenerListener(new MainMenuListener() {
			@Override
			public void clickCreate() {
				mainMenu.setVisible(false);
				motherFrame.remove(mainMenu);
				setupCreateLobbyPage();
			}
			
			// James
			@Override
			public void clickFind() {
				mainMenu.setVisible(false);
				motherFrame.remove(mainMenu);
				setupFindLobbyPage();
			}

			// James
			@Override
			public void clickLoad() {
				generatePopupMenu();
				
			}

			private void generatePopupMenu() {
				
				
			}

		});
	}
	//------------------------------- MAIN MENU
	

	//  CREATE LOBBY -------------------------------  
	private void setupCreateLobbyPage() {
		createLobby = new CreateLobbyPanel(CENTER_PANEL_DIMENSION, clientManager);
		contentPane.add(createLobby);		
		/**
		 * I have to make an exact use of the class name here because of import problems 
		 * due to same name (CreateListener) if I import at top - Zaid
		 */
		createLobby.addSelectionPiecesListenerListener(new personalizedlisteners.createLobbyListeners.CreateListener() {
			public void clickCreate() {
				createLobby.setVisible(false);
				motherFrame.remove(createLobby);
				setupLobbyPage();
			}
		});
		
		
		createLobby.addSelectionPiecesListenerListener(new BackListener() {
			public void clickBack() {
				createLobby.setVisible(false);
				motherFrame.remove(createLobby);
				setupMainMenuPage();
			}	
		});
	}
	//------------------------------- CREATE LOBBY
	
	
	// James
	//  FIND LOBBY -------------------------------  
	private void setupFindLobbyPage() {
		findLobby = new FindLobbyPanel(clientManager);
		
		findLobby.addSelectionPiecesListenerListener(new SearchEntrySetUpListener() {
			@Override
			public void SearchEntrySetUp() {
				findLobby.setVisible(false);
				motherFrame.remove(findLobby);
				setupLobbyPage();
			}
		});
		
		contentPane.add(findLobby);		
	}
	
	//------------------------------- FIND LOBBY
	
	
	//	LOBBY ------------------------------- 
	private void setupLobbyPage() {
		lobby = new LobbyPanel(CENTER_PANEL_DIMENSION,this.clientManager);
		contentPane.add(lobby);
		listenerThread.begin();
		if(clientManager.getLobby().getPlayers().get(0).getUserName().equals(clientManager.getUserName())) {
			lobby.addSelectionPiecesListenerListener(new StartListener() {
				public void clickStart(boolean flag) {

					if(!clientManager.getLobby().getPlayers().get(0).getUserName().equals(clientManager.getUserName())) {
	//					lobby.setVisible(false);
	//					motherFrame.remove(lobby);
	//					setupGamePage();
					}
					else {
						if(sendGameStateRequest()) {
	//						lobby.setVisible(false);
	//						motherFrame.remove(lobby);
	//						setupGamePage();
						} else {
							System.out.println("faileddddd");
						}
					}				
				}
			});
		}
		
		lobby.addSelectionPiecesListenerListener(new LeaveListener() {
			public void clickLeave() {
//				lobby.setVisible(false);
//				motherFrame.remove(lobby);
//				lobby.refreshDisplay();
//				System.out.println("Trying to update lobby page");
//				contentPane.add(lobby);
//				lobby.setVisible(true);
			}
		});
		
	}
	
	public void refreshLobby() {
		lobby.setVisible(false);
		motherFrame.remove(lobby);
		lobby.refreshDisplay();
		contentPane.add(lobby);
		lobby.setVisible(true);
		motherFrame.revalidate();
		
	}
	
	public void startGame() {
		lobby.setVisible(false);
		motherFrame.remove(lobby);
//		try {
//			listenerThread.holdUp();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setupGamePage();
	}
	//------------------------------- LOBBY
	
	
	// GAME	------------------------------- 
	private void setupGamePage() {
		/**
		 * Ben's gamePanel comes here
		 */
		//A fake gamestate set up to allow the gui to build from something

//		current = new GameManager(tester);
		current = new GameManager(clientManager.getLobby());
		
		table = new Table(clientManager.getUsersGameState(), clientManager, this, listenerThread);
		BoardPanel board = table.genBoard();
		LeftPanel LPanel = table.genLeftPanel();
		RightPanel RPanel = table.genRightPanel();
		
//		Table boardView = new Table(tester);
//		boardView.setVisible(true);
		contentPane.removeAll();
		contentPane.add(LPanel, BorderLayout.WEST);
		contentPane.add(board, BorderLayout.CENTER);
		contentPane.add(RPanel, BorderLayout.EAST);
		motherFrame.revalidate();
	}
	public void repaint(boolean placingChange, boolean playingChange) {
		//Table table = new Table(tester, clientManager);
		table.setPlaying(playingChange);
		table.setPlacing(placingChange);
		System.out.println("started repainting");
		BoardPanel board = table.getBoard();
		LeftPanel LPanel = table.getLeftPanel();
		RightPanel RPanel = table.getRightPanel();
		contentPane.removeAll();
		contentPane.add(LPanel, BorderLayout.WEST);
		contentPane.add(board, BorderLayout.CENTER);
		contentPane.add(RPanel, BorderLayout.EAST);
		motherFrame.revalidate();
	}
	
	public void refreshBoard() {
		if(clientManager.getEndTurnTrigger()) {
			table.hideAdvPanel();
			showAdvanceFireString(clientManager.getUsersGameState().getAdvFireString());
			clientManager.setEndTurnTrigger(false);
		}
		table.refresh(clientManager.getUsersGameState());
		repaint(clientManager.getUsersGameState().getFireFighterList().get(table.getMyIndex()).getCurrentPosition()==null, table.getMyIndex() == clientManager.getUsersGameState().getActiveFireFighterIndex());
	}
	
	public void showAdvanceFireString(String advFireString) {
		table.showAdvanceFireString(advFireString);
		
	}

	public void showGameTermination() {
		table.showGameTermination();
		
	}
	
//	public void gameRepainter() {
//		Table table = new Table(tester);
//		BoardPanel board = table.genBoard();
//		LeftPanel LPanel = table.new LeftPanel(tester);
//		RightPanel RPanel = table.new RightPanel(tester);
//		contentPane.removeAll();
//		contentPane.add(LPanel, BorderLayout.WEST);
//		contentPane.add(board, BorderLayout.CENTER);
//		contentPane.add(RPanel, BorderLayout.EAST);
//		motherFrame.revalidate();
//	}
	//	------------------------------- GAME 
	
	private boolean sendGameStateRequest() {
		
		return clientManager.gameStateRequest(userOne);
	}
	
	public void updateGameState(GameState updated) {
		this.tester = updated;
	}
	
	public boolean sendSaveGameRequest() {
		return clientManager.saveGameRequest();
	}
	
	public static Client getClient() {
		return client;
	}
	
	private boolean sendConnectionRequest() {
//		boolean flag = false;
//		ClientOutputThread output = client.getClientOutputThread();
//		ClientInputThread input = client.getClientInputThread();
//		String username = "Zaid";
//		String pword = "zzz";
//		TranObject<User> user = new TranObject<User>(TranObjectType.CONNECT);
//		userOne.setName(username);
//		userOne.setPassword(pword);
//		user.setObject(userOne);
//		output.setMsg(user);
		
//		return clientManager.connectionRequest(userOne);
		return clientManager.connectionRequest(userOne);
		
		
		/*
		 * try { while(input.readMessage() != true) { System.out.println("waiting"); } }
		 * catch(ClassNotFoundException f) { System.out.println("Error class"); }
		 * catch(IOException k) { System.out.println("Error IO"); }
		 */
//	
//		flag = true;
//		return flag; 
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
