package board;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
/**
 * 
 * @author zaidyahya
 *The main launcher of the game. Responsible for inserting/removing panels
 *Holds a frame, and uses panels to display 'pages' (i.e. login/menu/game etc)
 */
public class Launcher {

	
	private JFrame gameFrame;
	private JMenuBar tableMenuBar = new JMenuBar();
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(1500,800);
	private final static Dimension CENTER_PANEL_DIMENSION = new Dimension(1000,800);
	private final static Dimension RIGHT_PANEL_DIMENSION = new Dimension(300,800);
	private final static Dimension LEFT_PANEL_DIMENSION = new Dimension(200,800);
	
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
					window.gameFrame.setVisible(true);
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
		gameFrame = new JFrame();
		gameFrame.setBounds(100, 100, 450, 300);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		
		setupDummies();
		populateMenuBar();
		addMenuBar();
		setupLoginPage();
		
	}
	
	private void setupDummies() {
		dummyCenterPanel.setBackground(Color.YELLOW);
		dummyCenterPanel.setPreferredSize(CENTER_PANEL_DIMENSION);
		this.gameFrame.getContentPane().add(dummyCenterPanel, BorderLayout.CENTER);
		
		dummyRightPanel.setBackground(Color.RED);
		dummyRightPanel.setPreferredSize(RIGHT_PANEL_DIMENSION);
		this.gameFrame.getContentPane().add(dummyRightPanel, BorderLayout.EAST);
		
		dummyLeftPanel.setBackground(Color.GREEN);
		dummyLeftPanel.setPreferredSize(LEFT_PANEL_DIMENSION);
		this.gameFrame.getContentPane().add(dummyLeftPanel, BorderLayout.WEST);
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
		this.gameFrame.setJMenuBar(this.tableMenuBar);
	}
	
	private void setupLoginPage() {
		LoginPanel login = new LoginPanel(CENTER_PANEL_DIMENSION);
		this.gameFrame.getContentPane().remove(dummyCenterPanel);
		this.gameFrame.getContentPane().add(login.getLoginPanel(), BorderLayout.CENTER);
	}
	
	private void setupMenuPage() {
		
	}
	
	private void setupGamePage() {
		
	}
}
