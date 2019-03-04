package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import personalizedlisteners.LoginListener;


// random comments
/**
 * 
 * @author zaidyahya
 *The main launcher of the game. Responsible for inserting/removing panels
 *Holds a frame, and uses panels to display 'pages' (i.e. login/menu/game etc)
 */
public class Launcher {

	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(1500,800);
	private final static Dimension CENTER_PANEL_DIMENSION = new Dimension(1000,800);
	private final static Dimension RIGHT_PANEL_DIMENSION = new Dimension(300,800);
	private final static Dimension LEFT_PANEL_DIMENSION = new Dimension(200,800);

	private JFrame motherFrame;
	private Container contentPane;
	private JMenuBar tableMenuBar = new JMenuBar();

	private LoginPanel login;
	private MainMenuPanel mainMenu;

	String username;
	char[] password;

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
		initialize();
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
		contentPane.add(login.getLoginPanel(), BorderLayout.CENTER);
		// @Zaid: from James, I think the getLoginPanel is redundant since login is already the panel we need
	}
	//	------------------------------- LOGIN 
	
	
// James
	/**
	 * 
	 */
	private void setupMainMenuPage() {
		mainMenu = new MainMenuPanel();
		
		
		contentPane.add(mainMenu);
	}


	// James
	/**
	 * Validates by the user's credentials by asking the server
	 * @return
	 */
	private boolean validateCredentials() {
		// @server
		return true;
	}
	//------------------------------- LOGIN

	private void setupMenuPage() {

	}

	private void setupGamePage() {

	}
}
