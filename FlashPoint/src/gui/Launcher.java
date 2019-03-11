package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import client.Client;
import client.ClientInputThread;
import client.ClientManager;
import client.ClientOutputThread;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import custom_panels.CreateLobbyPanel;
import custom_panels.FindLobbyPanel;
import custom_panels.LobbyPanel;
import custom_panels.LoginPanel;
import custom_panels.MainMenuPanel;
import game.GameState;
import gui.Table.BoardPanel;
import gui.Table.LeftPanel;
import gui.Table.RightPanel;
import lobby.Lobby;
import managers.GameManager;
import personalizedlisteners.createLobbyListeners.BackListener;
import personalizedlisteners.lobbyListeners.LeaveListener;
import personalizedlisteners.lobbyListeners.StartListener;
import personalizedlisteners.loginListeners.LoginListener;
import personalizedlisteners.mainMenuListeners.MainMenuListener;
import tile.Tile;
// random comments
/**
 * 
 * @author zaidyahya
 *The main launcher of the game. Responsible for inserting/removing panels
 *Holds a frame, and uses panels to display 'pages' (i.e. login/menu/game etc)
 */
public class Launcher {

	private static Client client;
	private String ServerIP = "142.157.145.231";
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
	
	
	//Used by Ben for in game testing. Not permanent.
	private static GameManager current;
	private static GameState tester;

	String username; //@James - Ideally don't want these to be here, will grow enormously -- @Zaid
	char[] password; //

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
		userOne.setName("Zaid");
		userOne.setPassword("zzz");
		client = new Client(ServerIP, port);
		client.start();
		clientManager = new ClientManager(client.getClientInputThread(), client.getClientOutputThread());
		clientManager.connectionRequest(userOne);
//		if(sendConnectionRequest()) {
//			initialize();
//		};
		initialize();
	}
//
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
		login = new LoginPanel(CENTER_PANEL_DIMENSION);

		// James
		login.addSelectionPiecesListenerListener(new LoginListener() {
			@Override
			public void clickLogin() {
				username = login.getUsername();
				password = login.getPassword();

				if (validateCredentials() == true) {
					login.setVisible(false);
					motherFrame.remove(login);
					setupMainMenuPage();
				} else {
					System.out.println("Invalid Credentials");
				}

			}
			
		});

		contentPane.remove(dummyCenterPanel);
		contentPane.add(login, BorderLayout.CENTER);
	}
	 
	/**
	 * Validates by the user's credentials by asking the server
	 * @return @author James
	 */
	private boolean validateCredentials() {
		// @server
		return true;
	}
	//------------------------------- LOGIN
	

	//	MAIN MENU -------------------------------
	private void setupMainMenuPage() {
		mainMenu = new MainMenuPanel();
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

		});
	}
	//------------------------------- MAIN MENU
	

	//  CREATE LOBBY -------------------------------  
	private void setupCreateLobbyPage() {
		createLobby = new CreateLobbyPanel(CENTER_PANEL_DIMENSION);
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
		findLobby = new FindLobbyPanel();
		contentPane.add(findLobby);		
	}
	
	//------------------------------- FIND LOBBY
	
	
	//	LOBBY ------------------------------- 
	private void setupLobbyPage() {
		lobby = new LobbyPanel(CENTER_PANEL_DIMENSION);
		contentPane.add(lobby);
		
		lobby.addSelectionPiecesListenerListener(new StartListener() {
			public void clickStart() {
				lobby.setVisible(false);
				motherFrame.remove(lobby);
				setupGamePage();
			}
		});
		
		lobby.addSelectionPiecesListenerListener(new LeaveListener() {
			public void clickLeave() {
				lobby.setVisible(false);
				motherFrame.remove(lobby);
				setupMainMenuPage();
			}
		});
	}
	//------------------------------- LOBBY
	
	
	// GAME	------------------------------- 
	private void setupGamePage() {
		/**
		 * Ben's gamePanel comes here
		 */
		//A fake gamestate set up to allow the gui to build from something
		tester = GameState.getInstance();
		Lobby tempLobby = new Lobby();
		tester.updateGameStateFromLobby(tempLobby);
		Tile testTile = tester.returnTile(3, 1);
		Tile testTile2 = tester.returnTile(2, 4);
		Tile testTile3 = tester.returnTile(5, 6);
		current = new GameManager(tester);
		tester.placeFireFighter(tester.getFireFighterList().get(0), testTile);
		tester.placeFireFighter(tester.getFireFighterList().get(1), testTile3);
		tester.placeFireFighter(tester.getFireFighterList().get(2), testTile2);
//		testTile.getPoiList().get(0).reveal();
		current.generateAllPossibleActions();
		tester.updateActionList(current.getAllAvailableActions());
		
		Table table = new Table(tester);
		BoardPanel board = table.genBoard();
		LeftPanel LPanel = table.new LeftPanel(tester);
		RightPanel RPanel = table.new RightPanel(tester);
		
//		Table boardView = new Table(tester);
//		boardView.setVisible(true);
		contentPane.removeAll();
		contentPane.add(LPanel, BorderLayout.WEST);
		contentPane.add(board, BorderLayout.CENTER);
		contentPane.add(RPanel, BorderLayout.EAST);
		motherFrame.revalidate();
	}
	private void repaint() {
		Table table = new Table(tester);
		BoardPanel board = table.genBoard();
		LeftPanel LPanel = table.new LeftPanel(tester);
		RightPanel RPanel = table.new RightPanel(tester);
		contentPane.removeAll();
		contentPane.add(LPanel, BorderLayout.WEST);
		contentPane.add(board, BorderLayout.CENTER);
		contentPane.add(RPanel, BorderLayout.EAST);
		motherFrame.revalidate();
	}
	
//	public static void gameRepainter() {
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
	
	
	public static Client getClient() {
		return client;
	}
	
	private boolean sendConnectionRequest() {
		boolean flag = false;
//		ClientOutputThread output = client.getClientOutputThread();
//		ClientInputThread input = client.getClientInputThread();
		String username = "Zaid";
		String pword = "zzz";
//		TranObject<User> user = new TranObject<User>(TranObjectType.CONNECT);
		userOne.setName(username);
		userOne.setPassword(pword);
//		user.setObject(userOne);
//		output.setMsg(user);
		
		clientManager.connectionRequest(userOne);
		
		
		/*
		 * try { while(input.readMessage() != true) { System.out.println("waiting"); } }
		 * catch(ClassNotFoundException f) { System.out.println("Error class"); }
		 * catch(IOException k) { System.out.println("Error IO"); }
		 */
	
		flag = true;
		return flag; 
	}
}