package custom_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import game.GameState;
import personalizedlisteners.loadGameListeners.LoadGameSetUpListener;
import personalizedlisteners.createLobbyListeners.BackListener;
import personalizedlisteners.createLobbyListeners.CreateListener;
import javax.swing.JButton;

public class LoadGamePanel extends JPanel {
	private JPanel pnl_loadGame;
	private JLabel lbl_loadGame;
	private JPanel pnl_main;
	private JLabel lblNewLabel;
	private double lblX, lblY = 10, lblW = 248, lblH = 60;
	private int textAreaX = 10, textAreaW, textAreaH = 36;
	private int textAreaY = 60;
	private JLabel entryTest, entry_1, entry_2, entry_3, entry_4, entry_5;
	private ArrayList<JLabel> listEntries;
	
	private ArrayList<GameState> savedGames;
	
	private final EventListenerList REGISTERED_OBJECTS;
	
	private ClientManager clientManager;

//	private String[] fileExtensions = new String[]  {"smh"};
//	private File dir;
//	private ArrayList<File> listFiles;

	public LoadGamePanel(ClientManager clientmanager) {
		
		REGISTERED_OBJECTS = new EventListenerList();
		this.clientManager = clientmanager;
		clientmanager.savedGameListRequest();
		savedGames = clientmanager.getSavedGameStates();
		System.out.println("list length is: " + savedGames.size() );
		
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);

//		listFiles = new ArrayList<File>();
//		this.dir = folder;

		initializePanels(); // create LoadGamePanel/Load Game Label
		
		intializeEntries(); 
	}

	private void initializePanels() {
		pnl_main = new JPanel();
		pnl_main.setBackground(Color.LIGHT_GRAY);
		pnl_main.setBounds(308, 10, 455, 624);
		this.add(pnl_main);
		pnl_main.setLayout(null);

		lblX = pnl_main.getWidth()/2  - lblW/2;
		lblNewLabel = new JLabel("Load Game");
		lblNewLabel.setFont(new Font("Open Sans Semibold", Font.PLAIN, 23));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds((int) lblX, (int) lblY, (int) lblW, (int) lblH);
		pnl_main.add(lblNewLabel);
	
		createBackButton();
	}
	
	private void createBackButton() {
		JButton btn_back = new JButton("Go Back");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventBackBtn();
			}
		});
		btn_back.setFont(new Font("Open Sans", Font.PLAIN, 22));
		btn_back.setBounds(135, 652, 153, 62);
		add(btn_back);
	}


	private void intializeEntries() {
		textAreaW = pnl_main.getWidth() - 2*textAreaX;

		listEntries = new ArrayList<JLabel>();
//		listEntries.add(entry_1);
//		listEntries.add(entry_2);
//		listEntries.add(entry_3);
//		listEntries.add(entry_4);
//		listEntries.add(entry_5);
		
		for (int i = 1; i <= savedGames.size(); i++) {
//			JLabel currEntry = listEntries.get(i);
//
//			currEntry = new JLabel();
			JLabel currEntry = new JLabel();
			currEntry.setBounds(textAreaX, textAreaY + i * (textAreaH + 10), textAreaW, textAreaH);
			currEntry.setFont(new Font("Open Sans", Font.PLAIN, 14));
			currEntry.setBackground(Color.white);
			currEntry.setOpaque(true);
			currEntry.setText("GameSave: " + i); // we can give it a more distinct name to be more uniquely idetified
			
			
			
//			currEntry.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(loadGameRequest()) {
//						System.out.println("create successful");
//						raiseEventLoadGameBtn();
//					}
//				}
//			});
			currEntry.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {
						entryClicked((JLabel) e.getComponent());
						raiseEventLoadGameBtn();
					}
				}
			});
			listEntries.add(currEntry);
			pnl_main.add(currEntry);

		}



//		// Testing
//		entryTest = new JLabel();
//		entryTest.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//			}
//		});
//		entryTest.setBackground(Color.PINK);
//		entryTest.setText("TEST");
//		entryTest.setBackground(Color.white);
//		entryTest.setOpaque(true);
//		entryTest.setBounds(textAreaX, textAreaY + 0 * (textAreaH + 10) , textAreaW, textAreaH);
//		pnl_main.add(entryTest);
//		// Testing

	}
	
//	private boolean loadGameRequest() {
//		return true;
//	}

	private void entryClicked(JLabel entry) {
		
		System.out.println(entry.getText());
		clientManager.loadGameLobbyRequest(Integer.parseInt(entry.getText().replaceAll("\\D+",""))); //extract int from the entry

		// @Eric call loadMethod
	}

//	private void getGameFiles() {
//		String currFileExtension;
//
//		// need to iterate over directory
//		int i = currFile.lastIndexOf('.');
//		if (i > 0) {
//			currFileExtension = currFile.substring(i+1);
//		}
//		
//	}

	public JPanel getPnl_main() {
		return pnl_main;
	}

	public void setPnl_main(JPanel pnl_main) {
		this.pnl_main = pnl_main;
	}

	public void addSelectionPiecesListenerListener(LoadGameSetUpListener obj) {
		REGISTERED_OBJECTS.add(LoadGameSetUpListener.class, obj);
	}
	
	/**
	 * Raise an event: the create button has been clicked
	 */
	private void raiseEventLoadGameBtn() {
		for (LoadGameSetUpListener listener: REGISTERED_OBJECTS.getListeners(LoadGameSetUpListener.class)) {
			listener.clickLoadGame();
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
