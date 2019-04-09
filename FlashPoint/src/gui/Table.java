package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import actions.Action;
import actions.ActionList;
import client.ClientManager;
import custom_panels.ChatBox;
import game.GameState;
import tile.Tile;
import token.Colour;
import token.Firefighter;
import token.POI;
import token.Speciality;
import token.Vehicle;

public class Table {

//		private final JPanel gameFrame;
		private final int NUM_TILES = 80;		// 8 x 10 (rows x columns)
		private BoardPanel boardPanel;
		private RightPanel rightPanel;
		private LeftPanel leftPanel;
		protected GameState currentBoard;
		protected Tile[][] gameTiles;
		
		private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(1500,800);
		private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(1000,800);
		private final static Dimension RIGHT_PANEL_DIMENSION = new Dimension(300,800);
		private final static Dimension LEFT_PANEL_DIMENSION = new Dimension(200,800);
		private final static Dimension NORTH_LEFT_PANEL_DIMENSION = new Dimension(200, 300);
		private final static Dimension CENTER_LEFT_PANEL_DIMENSION = new Dimension(200, 300);
		private final static Dimension SOUTH_LEFT_PANEL_DIMENSION = new Dimension(200, 300);
		private final static Dimension SUPERTILE_PANEL_DIMENSION = new Dimension(40,40);
		private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
		private static String defaultImagesPath = "img/";
		
		private Color tileColorGrey = Color.decode("#B0B0B0");
		private Color tileColorRed = Color.decode("#FF0000");
		private static Color tileColorWhite = Color.decode("#FFFFFF");
		private Color tileColorBlack = Color.decode("#000000");
		private Color tileColorGreen = Color.decode("#00900B");
		private Color tileColorAmbulance = Color.decode("#05E1FF");
		private Color tileColorEngine = Color.decode("#FFFF05");
		private Color currentFFColour = Color.decode("#05FFE8");
		private Popup advFire;
		private Popup gameTermination;
		private Popup rideRequest;
		private Popup deckGunRequest;
		private Popup dodgeRequest;
		private Popup turnNotifier;
		private static boolean placing;
		private static boolean playing;
		private static boolean selectingFireFighter;
		private static boolean selectingSpeciality;
		private ClientManager clientManager;
		private Launcher launcher;
		private int myIndex = 7;
		private int[] myFFIndexes;
		private static boolean host = false;
		ChatBox chatBox;
		private boolean loadedGame = false;
		
		private boolean refreshers;
		
		private clientThread listenerThread;
		private int desiredFFindex;
		private token.Colour desiredFFindexColour;
		
		private HashMap<Firefighter, Integer> firefighterOrder = new HashMap<Firefighter, Integer>();
		
		boolean redReRoll;
		boolean blackReRoll; 
		int redDice;
		int blackDice;
		Random ran;
		
		private boolean[] dodged;
		private boolean[] rode;
		
		
//		public Table(GameState inputBoard) {
//			this.currentBoard = inputBoard;
//			this.gameTiles = inputBoard.getMatTiles();
//			this.gameFrame = new JPanel();//("FlashPoint");
//			this.gameFrame.setLayout(new BorderLayout());
//			final JMenuBar tableMenuBar = new JMenuBar();
//			populateMenuBar(tableMenuBar);
//			//this.gameFrame.setJMenuBar(tableMenuBar);
//			this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
//			this.boardPanel = new BoardPanel();
//			this.rightPanel = new RightPanel(this.currentBoard);
//			this.leftPanel = new LeftPanel(this.currentBoard);
//			this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
//			this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
//			this.gameFrame.add(this.leftPanel,BorderLayout.WEST);
////			this.gameFrame.setVisible(true);
//		}
		
		public Table(GameState inputBoard, ClientManager updatedClientManager, Launcher launcher, clientThread myThread) {
			currentBoard = inputBoard;
			gameTiles = inputBoard.getMatTiles();
			this.clientManager = updatedClientManager;
			this.launcher = launcher;
			this.listenerThread = myThread;
			this.selectingSpeciality = clientManager.getUsersGameState().getSpecialitySelecting();
			this.playing = false;
			this.placing = false;
			this.selectingFireFighter = false;
			if(!clientManager.getUsersGameState().isExperienced()) {
				this.selectingSpeciality = false;
				this.placing = true;
			} 
			this.desiredFFindex = 0;
			this.myFFIndexes = new int[6];
			this.myIndex = 0;
			this.redReRoll = false;
			this.blackReRoll = false;
			this.ran = new Random();
			this.dodged = new boolean[6];
			this.rode = new boolean[6];

			this.chatBox = new ChatBox(300, 350, clientManager);
			for(int i = 0; i<inputBoard.getFireFighterList().size(); i++) {
				Firefighter f = inputBoard.getFireFighterList().get(i);
				firefighterOrder.put(f,i);
				if(updatedClientManager.getUserName().equals(f.getOwner().getUserName())) {
//					this.myIndex = i;
					this.myFFIndexes[this.myIndex] = i;
					this.myIndex++;
				}
			}
			
			if(clientManager.getUsersGameState().getFreeFirefighters().size() != 0 ) {
				this.loadedGame = true;
			}
			
			if(this.myFFIndexes[0] == 0) {
				this.host = true;
			} else {
				this.host = false;
			}
			
			for(; this.myIndex<6;this.myIndex++) {
				this.myFFIndexes[this.myIndex] = 7;
			}
			
			System.out.println("hello this is the free ff check " + clientManager.getUsersGameState().getFreeFirefighters().size());
			
			if(clientManager.getUsersGameState().getFreeFirefighters().size() > 0) {
				
				this.myIndex = 7;
				this.selectingFireFighter = true;
				this.selectingSpeciality = false;
				this.placing = false;
			}
			System.out.println("this is my index" + myIndex);
			
			System.out.println("specil " + this.selectingSpeciality + " place " + this.placing + "playing " +this.playing + "loadin " + this.selectingFireFighter);
		}
		
		public BoardPanel genBoard() {
			this.boardPanel = new BoardPanel();
			return this.boardPanel;
			
		}
		public LeftPanel genLeftPanel() {
			this.leftPanel = new LeftPanel(this.currentBoard);
			return this.leftPanel;
		}
		public RightPanel genRightPanel() {
			this.rightPanel = new RightPanel(this.currentBoard);
			return this.rightPanel;
		}
		
		public BoardPanel getBoard() {
			return this.boardPanel;
			
		}
		public LeftPanel getLeftPanel() {
			return this.leftPanel;
		}
		public RightPanel getRightPanel() {
			return this.rightPanel;
		}
		
		public void updateBoard(GameState newBoard) {
			this.currentBoard = newBoard;
		}
		//add to launcher
		
		public void refresh(GameState newBoard) {
			System.out.println("starting refreshing");
			this.currentBoard = newBoard;
			gameTiles = currentBoard.getMatTiles();
//			this.playing = playingchange;
//			this.placing = placingchange;
			resetDodge();
			resetRode();
			if(this.loadedGame == true && clientManager.getUsersGameState().getFreeFirefighters().size() == 0) {
				this.myIndex = 0;
				for(int i = 0; i<clientManager.getUsersGameState().getFireFighterList().size(); i++) {
					Firefighter f = clientManager.getUsersGameState().getFireFighterList().get(i);
					firefighterOrder.put(f,i);
					if(this.clientManager.getUserName().equals(f.getOwner().getUserName())) {
						this.myFFIndexes[this.myIndex] = i;
						this.myIndex++;
					}
				}
				this.selectingSpeciality = false;
				this.placing = false; 
			}
			
			
			
			if(clientManager.getUsersGameState().getFreeFirefighters().size() == 0) {
				selectingFireFighter = false;
				if(myIndex>6) {
					for(int i = 0; i<clientManager.getUsersGameState().getFireFighterList().size(); i++) {
						Firefighter f = clientManager.getUsersGameState().getFireFighterList().get(i);
						firefighterOrder.put(f,i);
						if(clientManager.getUserName().equals(f.getOwner().getUserName())) {
							this.myIndex = i;
						}
					}
				}
			} else {
				selectingFireFighter = true;
			}
			
			this.selectingSpeciality = clientManager.getUsersGameState().getSpecialitySelecting();
			if(this.selectingSpeciality) {
				for(int i = 0; i<6; i++) {
					if(this.myFFIndexes[i] != 7) {
						if(clientManager.getUsersGameState().getFireFighterList().get(i).getSpeciality() == null) {
							this.myIndex = i;
							selectingSpeciality = true;
							
						} else {
							
						}
					}
				}
			} else {
				selectingSpeciality = false;
			}
			
			for(int i = 0; i<6; i++) {
				if(this.myFFIndexes[i] != 7) {
					if(clientManager.getUsersGameState().getFireFighterList().get(this.myFFIndexes[i]).getCurrentPosition() == null) {
						this.myIndex = this.myFFIndexes[i];
						placing = true;
						break;
					} else {
						placing = false;
					}
					if(clientManager.getUsersGameState().getActiveFireFighterIndex() == this.myFFIndexes[i]) {
						this.myIndex = this.myFFIndexes[i];
						playing = true;
						break;
					} else {
						playing = false;
					}
				}
			}
			
			
//			boardPanel.drawBoard(newBoard);
//			rightPanel.drawPanel(newBoard);
//			leftPanel.drawPanel(newBoard);
			rightPanel.refreshChat();
			this.boardPanel = new BoardPanel();
			this.rightPanel = new RightPanel(this.currentBoard);
			this.leftPanel = new LeftPanel(this.currentBoard);
//			rightPanel.refreshChat();
			
//			gameFrame.add(boardPanel, BorderLayout.CENTER);
//			gameFrame.add(rightPanel, BorderLayout.EAST);
//			gameFrame.add(leftPanel,BorderLayout.WEST);
	//		gameFrame.validate();
	//		gameFrame.repaint();
	//		this.gameFrame.setVisible(true);
//			this.gameFrame.validate();
		}

		private void populateMenuBar(final JMenuBar tableMenuBar) {
			tableMenuBar.add(createFileMenu());
			
		}

		private JMenu createFileMenu() {
			final JMenu fileMenu = new JMenu("File");
			
			final JMenuItem openPGN = new JMenuItem("Load PGN File");
			
			openPGN.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("OPEN");
				}
			});
			fileMenu.add(openPGN);
			
			final JMenuItem exitMenuItem = new JMenuItem("exit");
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
//					System.exit(0);
				}
			});
			
			fileMenu.add(exitMenuItem);
			
			return fileMenu;
		}
		
		public class RightPanel extends JPanel {
			GameState currentBoard;
			InformationPanel infoPanel;
			JTextArea chatArea;
//			ChatBox chatBox = new ChatBox(300, 350, clientManager);
			JPanel chatPanel = chatBox.getPanel_main();
			RightPanel(GameState updatedBoard){
				super(new GridLayout(2,1));
				setPreferredSize(RIGHT_PANEL_DIMENSION);
				currentBoard = updatedBoard;
				//chatArea = new JTextArea();
				//chatArea.setLineWrap(true);
				infoPanel = new InformationPanel(currentBoard);
				add(infoPanel);
				//add(chatArea);
				add(chatPanel);
				validate();
			}
			public void drawPanel(GameState currentBoard2) {
				// TODO Auto-generated method stub
				removeAll();
				infoPanel.drawInfo(currentBoard);
				add(infoPanel);
				chatBox.refreshChatBox(true);
				chatPanel = chatBox.getPanel_main();
				add(chatPanel);
				revalidate();
				repaint();
			}
			public void refreshChat() {
				chatBox.refreshChatBox(true);
				chatPanel = chatBox.getPanel_main();
				add(chatPanel);
			}
		}
		
		private class InformationPanel extends JPanel{
			GameState currentBoard;
			InformationPanel(GameState updatedBoard){
				super(new GridLayout());
				setLayout(new GridLayout(6,1));
				this.currentBoard = updatedBoard;
				createPlayerInfo();
			}
			public void drawInfo(GameState currentBoard2) {
				// TODO Auto-generated method stub
				removeAll();
				createPlayerInfo();
				revalidate();
				repaint();
				
			}
			
			public void createPlayerInfo() {
				for(int i = 0; i<this.currentBoard.getFireFighterList().size(); i++) {
					Firefighter currentFF = this.currentBoard.getFireFighterList().get(i);
					String playerInfo = (currentFF.getOwner().getUserName() + "  AP: " + currentFF.getAP() /*+ "  Saved Ap: " + currentFF.getSavedAP()*/);
					if(clientManager.getUsersGameState().isExperienced()) {
						playerInfo = playerInfo + " SAP: " + currentFF.getSP();
						if(currentFF.getSpeciality() == null) {
							playerInfo = playerInfo + " Specialty: unselcted";
						}else {
							playerInfo = playerInfo + " Specialty: "+ currentFF.getSpeciality();
						}
					}
					
					String inputString;
					//needs fixing
					String ffColour = currentFF.getColour().toString(currentFF.getColour());
					if(this.currentBoard.getActiveFireFighterIndex() == i) {
						inputString = "<html> <font size=\"3\", color='"+ffColour+"'><b>" + playerInfo + "</b></font></html>";
						JLabel currentFFPanel = new JLabel(inputString);
						Border blackline1 = BorderFactory.createLineBorder(tileColorRed,2);
//						currentFFPanel.setBackground(currentFFColour);
						currentFFPanel.setBorder(blackline1);
						add(currentFFPanel);
						
					} 
					else {
						inputString = "<html> <font size =\"3\", color='"+ ffColour + "'>" + playerInfo + "</font></html>";
						add(new JLabel(inputString));
					}
					
//					add(new JLabel(inputString));
					
				}
				String inputString = "<html> <font size=\"5\"> Current Wall Damage: " + currentBoard.getDamageCounter() + "</font></html>";
//				JPanel walldis = new JPanel();
				JLabel wallD = new JLabel(inputString);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,3);
				wallD.setBorder(blackline);
				add(wallD);
			}
		}
		
		public class LeftPanel extends JPanel {
			final LostPoiPanel lostPoiPanel;
			final SavedPoiPanel savedPoiPanel;
			final RevealPoiPanel revealPoiPanel;
			GameState currentBoard;
			
			LeftPanel(GameState updatedBoard){
				setPreferredSize(LEFT_PANEL_DIMENSION);
				this.currentBoard = updatedBoard;
				setLayout(new BorderLayout());
				this.lostPoiPanel = new LostPoiPanel(currentBoard);
				this.savedPoiPanel = new SavedPoiPanel(currentBoard);
				this.revealPoiPanel = new RevealPoiPanel(currentBoard);
				add(this.lostPoiPanel, BorderLayout.NORTH);
				add(this.savedPoiPanel, BorderLayout.SOUTH);
				add(this.revealPoiPanel, BorderLayout.CENTER);
				validate();
			}

			public void drawPanel(GameState newBoard) {
				// TODO Auto-generated method stub
				removeAll();
				lostPoiPanel.drawBoard(newBoard);
				savedPoiPanel.drawBoard(newBoard);
				revealPoiPanel.drawBoard(newBoard);
				add(lostPoiPanel, BorderLayout.NORTH);
				add(savedPoiPanel, BorderLayout.SOUTH);
				add(revealPoiPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
				
			}
		}
		
		private class LostPoiPanel extends JPanel{
			GameState currentBoard;
			LostPoiPanel(GameState updatedBoard){
				super(new BorderLayout());
				setLayout(new GridLayout(3,2));
				this.currentBoard = updatedBoard;
				setPreferredSize(NORTH_LEFT_PANEL_DIMENSION);
				getLost();
			}
			public void drawBoard(GameState newBoard) {
				// TODO Auto-generated method stub
				removeAll();
				this.currentBoard = newBoard;
				getLost();
				revalidate();
				repaint();
				
			}
			public void getLost() {
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,5);
				setBackground(tileColorRed);
				setBorder(blackline);
				if(this.currentBoard.getLostVictimsList().size() > 0) {
					for(int i = 0; i<this.currentBoard.getLostVictimsList().size(); i++) {
						try {
							final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "VICTIM.gif"));
							add(new JLabel(new ImageIcon(POIimage)));	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if(this.currentBoard.getLostHazmat().size() > 0) {
					int numHZ = this.currentBoard.getLostHazmat().size();
					try {
						final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "HAZMAT_" + numHZ  + ".gif"));
						add(new JLabel(new ImageIcon(POIimage)));	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private class SavedPoiPanel extends JPanel{
			GameState currentBoard;
			SavedPoiPanel(GameState updatedBoard){
				super(new BorderLayout());
				setLayout(new GridLayout(4,2));
				this.currentBoard = updatedBoard;
				setPreferredSize(SOUTH_LEFT_PANEL_DIMENSION);
				getSaved();
			}
			public void drawBoard(GameState newBoard) {
				// TODO Auto-generated method stub
				removeAll();
				getSaved();
				revalidate();
				repaint();
			}
			public void getSaved() {
				setBackground(tileColorGreen);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,5);
				setBorder(blackline);
				if(this.currentBoard.getSavedVictimsList().size() > 0) {
					for(int i = 0; i<this.currentBoard.getSavedVictimsList().size(); i++) {
						try {
							final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "VICTIM.gif"));
							add(new JLabel(new ImageIcon(POIimage)));	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if(this.currentBoard.getDisposedHazmat().size() > 0) {
					int numHZ = this.currentBoard.getDisposedHazmat().size();
					try {
						final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "HAZMAT_" + numHZ + ".gif"));
						add(new JLabel(new ImageIcon(POIimage)));	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private class RevealPoiPanel extends JPanel{
			GameState currentBoard;
			RevealPoiPanel(GameState updatedBoard){
				super(new BorderLayout());
				setLayout(new GridLayout(1,2));
				currentBoard = updatedBoard;
				setPreferredSize(CENTER_LEFT_PANEL_DIMENSION);
				getRevealed();
			}
			public void drawBoard(GameState newBoard) {
				// TODO Auto-generated method stub
				removeAll();
				getRevealed();
				revalidate();
				repaint();
			}
			public void getRevealed() {
				setBackground(tileColorGrey);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,5);
				setBorder(blackline);
				int num = currentBoard.getRevealedFalseAlarmsList().size();
				try {
					final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "FALSE_ALARM_"+ num + ".gif"));
					add(new JLabel(new ImageIcon(POIimage)));	
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(currentBoard.isExperienced()) {
					int numHS = currentBoard.getHotSpot();
					try {
						final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "HOTSPOT_"+ numHS + ".gif"));
						add(new JLabel(new ImageIcon(POIimage)));	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public class BoardPanel extends JPanel {
			final List<SuperTilePanel> boardTiles;
			
			BoardPanel(){
				super(new GridLayout(8,10));
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,5);
				setBorder(blackline);
				this.boardTiles = new ArrayList<>();
				for(int i = 0; i < NUM_TILES; i++) {
					final SuperTilePanel superTilePanel = new SuperTilePanel(this, i);
					this.boardTiles.add(superTilePanel);
					add(superTilePanel);
				}
				setPreferredSize(BOARD_PANEL_DIMENSION);
				validate();
			}

			public void drawBoard(GameState currentBoard) {
				// TODO Auto-generated method stub
				removeAll();
				for(final SuperTilePanel newTile : boardTiles) {
					newTile.DrawTile(currentBoard);
					add(newTile);
				}
				revalidate();
				repaint();
			}
		}
		
		private class SuperTilePanel extends JPanel {
			private final int tileId;
			private Tile connectedTile;
			private int[] coords;
			private final TilePanel tilePanel;
			SuperTilePanel(final BoardPanel boardPanel, final int tileId){
				super (new BorderLayout());
				setPreferredSize(SUPERTILE_PANEL_DIMENSION);
				coords = calculateTileCoords(tileId);
				connectedTile = gameTiles[coords[0]][coords[1]];
				tilePanel = new TilePanel(boardPanel, tileId);
				add(tilePanel, BorderLayout.CENTER);
				this.tileId = tileId;
				assignTileWall();
				assignVehicle();
			}
			public void DrawTile(GameState currentBoard) {
				// TODO Auto-generated method stub
				removeAll();
				assignTileWall();
				assignVehicle();
				tilePanel.drawTile(currentBoard);
				add(tilePanel, BorderLayout.CENTER);
				revalidate(); 
				repaint();
				
				
			}
			private void assignTileWall() {
				//left wall
				if(connectedTile.getEdge(0).isBlank()) {
					
				} else if(connectedTile.getEdge(0).isWall()) {
					if(connectedTile.getEdge(0).getDamage() == 2) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if (connectedTile.getEdge(0).getDamage() == 1) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DAMAGED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if(connectedTile.getEdge(0).getDamage() == 0) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				} else if(connectedTile.getEdge(0).isDoor()) {
					if(connectedTile.getEdge(0).isDestroyed()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORRIGHT_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(connectedTile.getEdge(0).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORRIGHT_OPEN.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(!connectedTile.getEdge(0).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORRIGHT.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				//north wall
				if(connectedTile.getEdge(1).isBlank()) {
					
				} else if(connectedTile.getEdge(1).isWall()) {
					if(connectedTile.getEdge(1).getDamage() == 2) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if (connectedTile.getEdge(1).getDamage() == 1) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DAMAGED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if(connectedTile.getEdge(1).getDamage() == 0) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if(connectedTile.getEdge(1).isDoor()) {
					if(connectedTile.getEdge(1).isDestroyed()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORBOT_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(connectedTile.getEdge(1).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORBOT_OPEN.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(!connectedTile.getEdge(1).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORBOT.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				//east wall
				if(connectedTile.getEdge(2).isBlank()) {
					
				} else if(connectedTile.getEdge(2).isWall()) {
					if(connectedTile.getEdge(2).getDamage() == 2) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if (connectedTile.getEdge(2).getDamage() == 1) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DAMAGED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if(connectedTile.getEdge(2).getDamage() == 0) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				} else if(connectedTile.getEdge(2).isDoor()) {
					if(connectedTile.getEdge(2).isDestroyed()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORLEFT_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(connectedTile.getEdge(2).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORLEFT_OPEN.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(!connectedTile.getEdge(2).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DOORLEFT.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				//south wall
				if(connectedTile.getEdge(3).isBlank()) {
					
				} else if(connectedTile.getEdge(3).isWall()) {
					if(connectedTile.getEdge(3).getDamage() == 2) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if (connectedTile.getEdge(3).getDamage() == 1) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DAMAGED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else if(connectedTile.getEdge(3).getDamage() == 0) {
						try {
							BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageWall)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				} else if(connectedTile.getEdge(3).isDoor()) {
					if(connectedTile.getEdge(3).isDestroyed()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORTOP_DESTROYED.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(connectedTile.getEdge(3).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORTOP_OPEN.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if(!connectedTile.getEdge(3).getStatus()) {
						try {
							BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORTOP.gif"));
							add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			public void assignVehicle() {
				for(int i = 0; i< currentBoard.getAmbulances().length; i++) {
					if(currentBoard.getAmbulances()[i].getCar()) {
						int x = currentBoard.getAmbulances()[i].getTiles()[0].getX();
						int y = currentBoard.getAmbulances()[i].getTiles()[0].getY();
						if(x == connectedTile.getCoords()[0]) {
							if(connectedTile.getCoords()[0] == 0) {
								if(connectedTile.getCoords()[1] == 5) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_LEFT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[1] == 6) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_RIGHT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if(connectedTile.getCoords()[0] == 7) {
								if(connectedTile.getCoords()[1] == 3) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_LEFT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[1] == 4) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_RIGHT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						} else if (y == connectedTile.getCoords()[1]) {
							if(connectedTile.getCoords()[1] == 0) {
								if(connectedTile.getCoords()[0] == 3) {
									System.out.println("upper called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_TOP.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[0] == 4) {
									System.out.println("downer called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_BOT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if(connectedTile.getCoords()[1] == 9) {
								if(connectedTile.getCoords()[0] == 3) {
									System.out.println("upper called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_TOP.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[0] == 4) {
									System.out.println("downer called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "AMBULANCE_BOT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
					if(currentBoard.getEngines()[i].getCar()) {
						int x = currentBoard.getEngines()[i].getTiles()[0].getX();
						int y = currentBoard.getEngines()[i].getTiles()[0].getY();
						if(x == connectedTile.getCoords()[0]) {
							if(connectedTile.getCoords()[0] == 0) {
								if(connectedTile.getCoords()[1] == 7) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_LEFT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[1] == 8) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_RIGHT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if(connectedTile.getCoords()[0] == 7) {
								if(connectedTile.getCoords()[1] == 1) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_LEFT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.EAST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[1] == 2) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_RIGHT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.WEST);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						} else if (y == connectedTile.getCoords()[1]) {
							if(connectedTile.getCoords()[1] == 0) {
								if(connectedTile.getCoords()[0] == 1) {
									System.out.println("upper called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_TOP.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[0] == 2) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_BOT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else if(connectedTile.getCoords()[1] == 9) {
								if(connectedTile.getCoords()[0] == 5) {
									System.out.println("upper called");
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_TOP.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.SOUTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else if (connectedTile.getCoords()[0] == 6) {
									try {
										BufferedImage imageDoor = ImageIO.read(new File(defaultImagesPath + "ENGINE_BOT.gif"));
										add(new JLabel(new ImageIcon(imageDoor)), BorderLayout.NORTH);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
		
		private class TilePanel extends JPanel {
			private final Tile connectedTile;
			private final int tileId;
			private final int[] coords;
			//final List<TokenPanel> tokenTiles;
			
			TilePanel(final BoardPanel boardPanel, final int tileId) {
				super (new GridLayout(2,2));
				this.tileId = tileId;
				setPreferredSize(TILE_PANEL_DIMENSION);
				coords = calculateTileCoords(tileId);
				connectedTile = gameTiles[coords[0]][coords[1]];
				assignFires();
				Border blackline = BorderFactory.createLineBorder(tileColorBlack);
				setBorder(blackline);
				assignTokens();
				
				addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(final MouseEvent e) {
						if(SwingUtilities.isRightMouseButton(e)) {
							//brings up menu
							int[] check;
							if(selectingSpeciality) {
								if(connectedTile.getCoords()[0] == 0 || connectedTile.getCoords()[1] == 0 || connectedTile.getCoords()[0] == 7 || connectedTile.getCoords()[1] == 9) {
									showPopUpMenuSpeciality(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
								}
							}
							else if(placing) {
								if(connectedTile.getCoords()[0] == 0 || connectedTile.getCoords()[1] == 0 || connectedTile.getCoords()[0] == 7 || connectedTile.getCoords()[1] == 9) {
									showPopUpMenuPlace(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
								}
							}else if(selectingFireFighter){
								if(connectedTile.getCoords()[0] == 0 || connectedTile.getCoords()[1] == 0 || connectedTile.getCoords()[0] == 7 || connectedTile.getCoords()[1] == 9) {
									showPopUpMenuFFSelector(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
								}
							}else {
								if(playing && !clientManager.getUsersGameState().isGameWon() && !clientManager.getUsersGameState().isGameTerminated()) {
									check = currentBoard.getFireFighterList().get(currentBoard.getActiveFireFighterIndex()).getCurrentPosition().getCoords();
									if(connectedTile.getCoords()[0] == check[0] && connectedTile.getCoords()[1] == check[1]) {
										showPopUpMenuCurrent(e.getComponent(), e.getX(), e.getY(), currentBoard);
									} else {
										showPopUpMenuOther(e.getComponent(), e.getX(), e.getY(), currentBoard);
									}
								} 
								else {
									if(clientManager.getUsersGameState().getActiveFireFighterIndex() == -1 && clientManager.getUsersGameState().isExperienced() && host/*getMyIndex() == 0*/) {
										showPopUpMenuVehicle(e.getComponent(), e.getX(), e.getY(), currentBoard);
									}
								}
							}
						} else if(SwingUtilities.isLeftMouseButton(e)) {
							showPopUpMenuInfo(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
						}
						
					}

					@Override
					public void mousePressed(final MouseEvent e) {
					
						
					}

					@Override
					public void mouseReleased(final MouseEvent e) {
						
						
					}

					@Override
					public void mouseEntered(final MouseEvent e) {
					
						
					}

					@Override
					public void mouseExited(final MouseEvent e) {
					
						
					}
					
				});
				
				validate();
			}
			
			public void drawTile(GameState currentBoard) {
				
				removeAll();
				assignFires();
				Border blackline = BorderFactory.createLineBorder(tileColorBlack);
				setBorder(blackline);
				assignTokens();
				revalidate();
				repaint();
			}

			private void assignTokens() {
				this.removeAll();
				if(this.connectedTile.containsPOI()) {
					if(this.connectedTile.getPoiList().get(0).isRevealed()) {
						int numberPOI = this.connectedTile.getPoiList().size();
						try {
							final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "VICTIM_"+ numberPOI + ".gif"));
							add(new JLabel(new ImageIcon(POIimage)));	
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "POI.gif"));
							add(new JLabel(new ImageIcon(POIimage)));	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else {
					add(new JLabel());
				}
					
				if(this.connectedTile.containsFirefighter()) {
					
					String builder = defaultImagesPath;
					//Firefighter currentFF = currentBoard.getPlayingFirefighter();
					Firefighter currentFF;
					if(myIndex > 5) {
						currentFF = this.connectedTile.getFirefighterList().get(0);
					}else {
						currentFF = clientManager.getUsersGameState().getFireFighterList().get(myIndex);
					}
					
					Tile currentPos = currentFF.getCurrentPosition();
					Firefighter check;
					if(this.connectedTile == currentPos) {
						check = currentFF;
					} else {
						check = this.connectedTile.getFirefighterList().get(0);
					}
					
					if(clientManager.getUsersGameState().isExperienced()) {
						if(check.getSpeciality() == Speciality.CAFS) {
							try {
								builder = builder + "CAFS_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));	
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if(check.getSpeciality() == Speciality.CAPTAIN) {
							try {
								builder = builder + "CAPTAIN_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						} else if(check.getSpeciality() == Speciality.DOG) {
							try {
								builder = builder + "DOG_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						} else if(check.getSpeciality() == Speciality.DRIVER) {
							try {
								builder = builder + "DRIVER_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						} else if(check.getSpeciality() == Speciality.GENERALIST) {
							try {
								builder = builder + "WHITE_FIREMAN_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						} else if(check.getSpeciality() == Speciality.HAZMAT_TECHNICIAN) {
							try {
								builder = builder + "SHAZMAT_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if(check.getSpeciality() == Speciality.IMAGING_TECHNICIAN) {
							try {
								builder = builder + "IMAGING_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if(check.getSpeciality() == Speciality.PARAMEDIC) {
							try {
								builder = builder + "PARAMEDIC_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if(check.getSpeciality() == Speciality.RESCUE_SPECIALIST) {
							try {
								builder = builder + "RESCUE_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else if(check.getSpeciality() == Speciality.VETERAN) {
							try {
								builder = builder + "VETEREN_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							try {
								builder = builder + "GREEN_FIREMAN_";
								int numberFF = this.connectedTile.getFirefighterList().size();
								final BufferedImage FFimage = ImageIO.read(new File(builder + numberFF +".gif"));
								add(new JLabel(new ImageIcon(FFimage)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						if(check.getColour() == Colour.WHITE) {
							builder = builder + "WHITE";
						} else if(check.getColour() == Colour.BLUE) {
							builder = builder + "BLUE";
						}else if(check.getColour() == Colour.RED) {
							builder = builder + "RED";
						}else if(check.getColour() == Colour.BLACK) {
							builder = builder + "BLACK";
						}else if(check.getColour() == Colour.GREEN) {
							builder = builder + "GREEN";
						}else if(check.getColour() == Colour.PURPLE) {
							builder = builder + "PURPLE";
						}
						try {
							builder = builder + "_FIREMAN";
							int numberFF = this.connectedTile.getFirefighterList().size();
							final BufferedImage FFimage = ImageIO.read(new File(builder +"_"+ numberFF +".gif"));
							add(new JLabel(new ImageIcon(FFimage)));	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				} else {
					add(new JLabel());
				}
				
				if(this.connectedTile.containsHazmat()) {
					try {
						final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "HAZMAT.gif"));
						add(new JLabel(new ImageIcon(POIimage)));	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					add(new JLabel());
				}
				
				if(this.connectedTile.containsHotSpot()) {
					try {
						final BufferedImage POIimage = ImageIO.read(new File(defaultImagesPath + "HOTSPOT.gif"));
						add(new JLabel(new ImageIcon(POIimage)));	
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					add(new JLabel());
				}
			}
			
		

			private void assignFires(){
				if(connectedTile.checkInterior()) {
					setBackground(tileColorWhite);
				} else {
					setBackground(tileColorGreen);
					if(connectedTile.getParkingType() == Vehicle.Engine) {
						setBackground(tileColorEngine);
					}else if(connectedTile.getParkingType() == Vehicle.Ambulance) {
						setBackground(tileColorAmbulance);
					}
				}
				if(connectedTile.getFire() == 1) {
					setBackground(tileColorGrey);
				}	else if(connectedTile.getFire() == 2) {
					setBackground(tileColorRed);
				} 
			}
			
			//generates the popup menu for the available actions
			public void showPopUpMenuCurrent(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard) {
				JPopupMenu popupMenu = new JPopupMenu();
				Set<actions.Action> currentActions = currentBoard.getAvailableActions();
				for(actions.Action act: currentActions) {
					System.out.println(act.getTitle());
					System.out.println(act.getClass());
				}
				
				boolean moveCheck = false, moveWVCheck = false, completeCheck = false, toSmokeCheck = false, oneChopCheck = false, twoChopCheck = false, doorCheck = false; 
				
				JMenu moveMenu = new JMenu("Move");
			    JMenu extinguishMenu = new JMenu("Extinguish"); 
			    JMenu toSmokeMenu = new JMenu("To Smoke/Smoke");
			    JMenu completelyMenu = new JMenu("Completely");
			    JMenu chopMenu = new JMenu("Chop");
			    JMenu onceMenu = new JMenu("Once");
			    JMenu twiceMenu = new JMenu("Twice");
			    JMenu handleMenu = new JMenu("Toggle Door");
			    JMenu finishMenu = new JMenu("finish");
			    JMenu moveWithVictimMenu = new JMenu("Move With Victim");
			    
			  //advanced
			    boolean changeCheck = false, driveCheck = false, commandCheck = false, firedeckCheck = false, flipPOICheck = false, moveWithHazmatCheck = false, pickCheck =false, dropCheck = false, disposeCheck = false, healCheck = false;
			    
			    JMenu changeMenu = new JMenu("Crew Change");
			    JMenu commandMenu = new JMenu("Command");
			    JMenu fireDeckGunMenu = new JMenu("Fire deck gun");
			    JMenu driveMenu = new JMenu("Drive");
			    JMenu flipPOIMenu = new JMenu("Identify");
			    JMenu moveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu pickMenu = new JMenu("Pick Up");
			    JMenu dropMenu = new JMenu("Drop");
			    JMenu disposeMenu = new JMenu("Dispose");
			    JMenu healMenu = new JMenu("Heal");
			    
			    //commandPlayers
			    boolean player1Check = false, player2Check = false, player3Check = false, player4Check = false, player5Check = false, player6Check = false;
			    
			    //playerActionCheck
			    boolean p1move = false, p1moveH = false, p1moveV = false, p1door = false, p2move = false, p2moveH = false, p2moveV = false, p2door = false, p3move = false, p3moveH = false, p3moveV = false, p3door = false;
			    boolean p4move = false, p4moveH = false, p4moveV = false, p4door = false, p5move = false, p5moveH = false, p5moveV = false, p5door = false, p6move = false, p6moveH = false, p6moveV = false, p6door = false;
			    JMenu commandP1Menu = new JMenu();
			    JMenu commandP2Menu = new JMenu();
			    JMenu commandP3Menu = new JMenu();
			    JMenu commandP4Menu = new JMenu(); 
			    JMenu commandP5Menu = new JMenu(); 
			    JMenu commandP6Menu = new JMenu();
			    
			    JMenu p1MoveMenu = new JMenu("Move");
			    JMenu p1MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p1MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p1HandleMenu = new JMenu("Toggle Door");
			    JMenu p2MoveMenu = new JMenu("Move");
			    JMenu p2MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p2MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p2HandleMenu = new JMenu("Toggle Door");
			    JMenu p3MoveMenu = new JMenu("Move");
			    JMenu p3MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p3MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p3HandleMenu = new JMenu("Toggle Door");
			    JMenu p4MoveMenu = new JMenu("Move");
			    JMenu p4MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p4MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p4HandleMenu = new JMenu("Toggle Door");
			    JMenu p5MoveMenu = new JMenu("Move");
			    JMenu p5MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p5MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p5HandleMenu = new JMenu("Toggle Door");
			    JMenu p6MoveMenu = new JMenu("Move");
			    JMenu p6MoveWithVictimMenu = new JMenu("Move With Victim");
			    JMenu p6MoveWithHazmatMenu = new JMenu("Move With Hazmat");
			    JMenu p6HandleMenu = new JMenu("Toggle Door");
			    
			    
			    for(int pCount = 0; pCount < clientManager.getUsersGameState().getFireFighterList().size(); pCount++) {
			    	switch(pCount) {
			    		case 0:
			    			commandP1Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName() +" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    		case 1:
			    			commandP2Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName()+" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    		case 2:
			    			commandP3Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName()+" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    		case 3:
			    			commandP4Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName()+" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    		case 4:
			    			commandP5Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName()+" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    		case 5:
			    			commandP6Menu = new JMenu(clientManager.getUsersGameState().getFireFighterList().get(pCount).getOwner().getUserName()+" | "+ clientManager.getUsersGameState().getFireFighterList().get(pCount).getColour().toString());
			    			break;
			    	}
			    	
			    }
			    
			    
			    for(actions.Action a: currentActions) {
			    	ActionList actionTitle = a.getTitle();
			    	String builder = "";
			    	JMenuItem newAction;
			    	int APCost = a.getCost();
			    	if(actionTitle == ActionList.Chop) {
			    		if(clientManager.getUsersGameState().getPlayingFirefighter().getSpeciality() == Speciality.RESCUE_SPECIALIST) {
			    			if(APCost == 1) {
				    			if(a.getDirection() == 0) {
					    			builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
					    					}
					    					
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} 
				    		} else if(APCost == 2) {
				    			if(a.getDirection() == 0) {
					    			builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} 
				    		}
			    		}else {
			    			if(APCost == 2) {
				    			if(a.getDirection() == 0) {
					    			builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
					    					}
					    					
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        onceMenu.add(newAction);
					    	        oneChopCheck = true;
					    			
					    		} 
				    		} else if(APCost == 4) {
				    			if(a.getDirection() == 0) {
					    			builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        twiceMenu.add(newAction);
					    	        twoChopCheck = true;
					    			
					    		} 
				    		}
			    		}
			    		
			    		
			    	} else if(actionTitle == ActionList.Extinguish) {
			    		if(clientManager.getUsersGameState().getPlayingFirefighter().getSpeciality() == Speciality.PARAMEDIC ||
			    				clientManager.getUsersGameState().getPlayingFirefighter().getSpeciality() == Speciality.RESCUE_SPECIALIST) {
			    			if(APCost == 2) {
				    			if(a.getDirection() == 0) {
				    				builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == -1) {
					    			builder = "Current Location, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		}
				    		} else if(APCost == 4) {
				    			if(a.getDirection() == 0) {
				    				builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == -1) {
					    			builder = "Current Location, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		}
				    		}
			    		} else {
			    			if(APCost == 1) {
				    			if(a.getDirection() == 0) {
				    				builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		} else if(a.getDirection() == -1) {
					    			builder = "Current Location, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        toSmokeMenu.add(newAction);
					    	        toSmokeCheck = true;
					    		}
				    		} else if(APCost == 2) {
				    			if(a.getDirection() == 0) {
				    				builder = "Left, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == 1) {
					    			builder = "Up, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    			
					    		} else if(a.getDirection() == 2) {
					    			builder = "Right, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == 3) {
					    			builder = "Down, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		} else if(a.getDirection() == -1) {
					    			builder = "Current Location, APC: " + APCost;
					    			newAction = new JMenuItem(builder);
					    	        newAction.addActionListener(new ActionListener() {
					    				@Override
					    				public void actionPerformed(ActionEvent e) {
					    					if(sendActionRequest(a)) {
												System.out.println("this is the print that board is refreshing");
											}
					    				}
					    			});
					    	        completelyMenu.add(newAction);
					    	        completeCheck = true;
					    		}
				    		}
			    		}
			    		
			    	}  else if(actionTitle == ActionList.Finish) {
			    		builder = "End Turn";
		    			newAction = new JMenuItem(builder);
		    	        newAction.addActionListener(new ActionListener() {
		    				@Override
		    				public void actionPerformed(ActionEvent e) {
		    					if(sendEndTurnRequest()){
		    						System.out.println("this is the print that board is refreshing");
//		    						launcher.showAdvanceFireString(clientManager.getUsersGameState().getAdvFireString());
		    						if(clientManager.getUsersGameState().isGameTerminated()) {
//		    							launcher.showGameTermination();
//		    							//refresh(clientManager.getUsersGameState());
//		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						} else if(clientManager.getUsersGameState().isGameWon()) {
//		    							launcher.showGameTermination();
//		    							//refresh(clientManager.getUsersGameState());
//		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						} else {
//		    							refresh(clientManager.getUsersGameState());
//		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						}
		    					}
		    				}
		    			});
		    	        finishMenu.add(newAction);
			    	}  else if(actionTitle == ActionList.Handle) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    	        doorCheck = true;
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    	        doorCheck = true;
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    	        doorCheck = true;
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    	        doorCheck = true;
			    		} 
			    	} else if(actionTitle == ActionList.Move) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    	        moveCheck = true;
			    			
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    	        moveCheck = true;
			    			
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    	        moveCheck = true;
			    			
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    	        moveCheck = true;
			    			
			    		} 
			    	} else if(actionTitle == ActionList.MoveWithVictim) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    	        moveWVCheck = true;
			    			
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    	        moveWVCheck = true;
			    			
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    	        moveWVCheck = true;
			    			
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
			    					}
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    	        moveWVCheck = true;
			    			
			    		} 
			    	} 
			    	
			    	//advanced actions
			    	
			    	else if (clientManager.getUsersGameState().isExperienced()) {
			    		System.out.println("experienced Aciton");
		    	        if(actionTitle == ActionList.Change) {
			    			if(a.getToSpecialty() == Speciality.CAFS) {
			    				builder = "To CAFS. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.CAPTAIN) {
			    				builder = "To Captain. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.DOG) {
			    				builder = "To Dog. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.DRIVER) {
			    				builder = "To Driver. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.GENERALIST) {
			    				builder = "To Generalist. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.HAZMAT_TECHNICIAN) {
			    				builder = "To Hazmat Technician. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.IMAGING_TECHNICIAN) {
			    				builder = "To Imaging technician. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.PARAMEDIC) {
			    				builder = "To Paramedic. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.RESCUE_SPECIALIST) {
			    				builder = "To Rescue Specialist. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} else if (a.getToSpecialty() == Speciality.VETERAN) {
			    				builder = "To Veteran. APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");

										}
				    					
				    				}
				    			});
				    	        changeMenu.add(newAction);
				    	        changeCheck = true;
			    			} 
			    		} else if(actionTitle == ActionList.Command) {
//			    			Firefighter f = a.getFireFighter();
//			    			int i = firefighterOrder.get(f);
			    			int i = a.getFirefighterIndex();
			    			actions.Action toPreform = a.getAction();
			    			String commandBuilder = "";
			    			JMenuItem newCommandAction;
			    			switch (i){
			    				case 0:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveMenu.add(newCommandAction);
			    							p1move = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveMenu.add(newCommandAction);
			    							p1move = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveMenu.add(newCommandAction);
			    							p1move = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveMenu.add(newCommandAction);
			    							p1move = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithHazmatMenu.add(newCommandAction);
			    							p1moveH = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithHazmatMenu.add(newCommandAction);
			    							p1moveH = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithHazmatMenu.add(newCommandAction);
			    							p1moveH = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithHazmatMenu.add(newCommandAction);
			    							p1moveH = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithVictimMenu.add(newCommandAction);
			    							p1moveV = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithVictimMenu.add(newCommandAction);
			    							p1moveV = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithVictimMenu.add(newCommandAction);
			    							p1moveV = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1MoveWithVictimMenu.add(newCommandAction);
			    							p1moveV = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1HandleMenu.add(newCommandAction);
			    							p1door = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1HandleMenu.add(newCommandAction);
			    							p1door = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1HandleMenu.add(newCommandAction);
			    							p1door = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p1HandleMenu.add(newCommandAction);
			    							p1door = true;
			    							player1Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    				case 1:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveMenu.add(newCommandAction);
			    							p2move = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveMenu.add(newCommandAction);
			    							p2move = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveMenu.add(newCommandAction);
			    							p2move = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveMenu.add(newCommandAction);
			    							p2move = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithHazmatMenu.add(newCommandAction);
			    							p2moveH = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithHazmatMenu.add(newCommandAction);
			    							p2moveH = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithHazmatMenu.add(newCommandAction);
			    							p2moveH = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithHazmatMenu.add(newCommandAction);
			    							p2moveH = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithVictimMenu.add(newCommandAction);
			    							p2moveV = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithVictimMenu.add(newCommandAction);
			    							p2moveV = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithVictimMenu.add(newCommandAction);
			    							p2moveV = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2MoveWithVictimMenu.add(newCommandAction);
			    							p2moveV = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2HandleMenu.add(newCommandAction);
			    							p2door = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2HandleMenu.add(newCommandAction);
			    							p2door = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2HandleMenu.add(newCommandAction);
			    							p2door = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p2HandleMenu.add(newCommandAction);
			    							p2door = true;
			    							player2Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    				case 2:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveMenu.add(newCommandAction);
			    							p3move = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveMenu.add(newCommandAction);
			    							p3move = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveMenu.add(newCommandAction);
			    							p3move = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveMenu.add(newCommandAction);
			    							p3move = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithHazmatMenu.add(newCommandAction);
			    							p3moveH = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithHazmatMenu.add(newCommandAction);
			    							p3moveH = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithHazmatMenu.add(newCommandAction);
			    							p3moveH = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithHazmatMenu.add(newCommandAction);
			    							p3moveH = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithVictimMenu.add(newCommandAction);
			    							p3moveV = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithVictimMenu.add(newCommandAction);
			    							p3moveV = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithVictimMenu.add(newCommandAction);
			    							p3moveV = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3MoveWithVictimMenu.add(newCommandAction);
			    							p3moveV = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3HandleMenu.add(newCommandAction);
			    							p3door = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3HandleMenu.add(newCommandAction);
			    							p3door = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3HandleMenu.add(newCommandAction);
			    							p3door = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p3HandleMenu.add(newCommandAction);
			    							p3door = true;
			    							player3Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    				case 3:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveMenu.add(newCommandAction);
			    							p4move = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveMenu.add(newCommandAction);
			    							p4move = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveMenu.add(newCommandAction);
			    							p4move = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveMenu.add(newCommandAction);
			    							p4move = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithHazmatMenu.add(newCommandAction);
			    							p4moveH = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithHazmatMenu.add(newCommandAction);
			    							p4moveH = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithHazmatMenu.add(newCommandAction);
			    							p4moveH = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithHazmatMenu.add(newCommandAction);
			    							p4moveH = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithVictimMenu.add(newCommandAction);
			    							p4moveV = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithVictimMenu.add(newCommandAction);
			    							p4moveV = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithVictimMenu.add(newCommandAction);
			    							p4moveV = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4MoveWithVictimMenu.add(newCommandAction);
			    							p4moveV = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4HandleMenu.add(newCommandAction);
			    							p4door = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4HandleMenu.add(newCommandAction);
			    							p4door = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4HandleMenu.add(newCommandAction);
			    							p4door = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p4HandleMenu.add(newCommandAction);
			    							p4door = true;
			    							player4Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    				case 4:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveMenu.add(newCommandAction);
			    							p5move = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveMenu.add(newCommandAction);
			    							p5move = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveMenu.add(newCommandAction);
			    							p5move = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveMenu.add(newCommandAction);
			    							p5move = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithHazmatMenu.add(newCommandAction);
			    							p5moveH = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithHazmatMenu.add(newCommandAction);
			    							p5moveH = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithHazmatMenu.add(newCommandAction);
			    							p5moveH = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithHazmatMenu.add(newCommandAction);
			    							p5moveH = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithVictimMenu.add(newCommandAction);
			    							p5moveV = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithVictimMenu.add(newCommandAction);
			    							p5moveV = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithVictimMenu.add(newCommandAction);
			    							p5moveV = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5MoveWithVictimMenu.add(newCommandAction);
			    							p5moveV = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5HandleMenu.add(newCommandAction);
			    							p5door = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5HandleMenu.add(newCommandAction);
			    							p5door = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5HandleMenu.add(newCommandAction);
			    							p5door = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p5HandleMenu.add(newCommandAction);
			    							p5door = true;
			    							player5Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    				case 5:
			    					if(toPreform.getTitle() == ActionList.Move) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveMenu.add(newCommandAction);
			    							p6move = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveMenu.add(newCommandAction);
			    							p6move = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveMenu.add(newCommandAction);
			    							p6move = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveMenu.add(newCommandAction);
			    							p6move = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithHazmat) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithHazmatMenu.add(newCommandAction);
			    							p6moveH = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithHazmatMenu.add(newCommandAction);
			    							p6moveH = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithHazmatMenu.add(newCommandAction);
			    							p6moveH = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithHazmatMenu.add(newCommandAction);
			    							p6moveH = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.MoveWithVictim) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithVictimMenu.add(newCommandAction);
			    							p6moveV = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithVictimMenu.add(newCommandAction);
			    							p6moveV = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithVictimMenu.add(newCommandAction);
			    							p6moveV = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6MoveWithVictimMenu.add(newCommandAction);
			    							p6moveV = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						}
			    					} else if (toPreform.getTitle() == ActionList.Handle) {
			    						if(toPreform.getDirection() == 0) {
			    							commandBuilder = "Left APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6HandleMenu.add(newCommandAction);
			    							p6door = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 1) {
			    							commandBuilder = "Up APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6HandleMenu.add(newCommandAction);
			    							p6door = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 2) {
			    							commandBuilder = "Right APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6HandleMenu.add(newCommandAction);
			    							p6door = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						} else if(toPreform.getDirection() == 3) {
			    							commandBuilder = "Down APC: ";
			    							commandBuilder = commandBuilder + toPreform.getCost();
			    							newCommandAction = new JMenuItem(commandBuilder);
			    							newCommandAction.addActionListener(new ActionListener() {
							    				@Override
							    				public void actionPerformed(ActionEvent e) {
							    					if(sendActionRequest(a)) {
														System.out.println("this is the print that board is refreshing");

													}
							    					
							    				}
							    			});
			    							p6HandleMenu.add(newCommandAction);
			    							p6door = true;
			    							player6Check = true;
			    							commandCheck = true;
			    						}
			    					}
			    					break;
			    			}
			    			
			    		} else if(actionTitle == ActionList.Drive) {
			    			
			    			if(a.isAmbulance()) {
			    				builder = "Ambulance";
			    				if(!a.canMove()) {
			    					builder = "Call " + builder;
			    				}
			    				else {
			    					builder = "Ride " + builder;
			    				}
			    			} else {
			    				builder = "Engine ";
			    			}
			    			
			    			if(a.getDirection() == -1) {
			    				builder += "Counter ClockWise ";
			    			}else {
			    				builder += "ClockWise ";
			    			}
			    			
			    			
			    			
			    			builder = builder + "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
									}
			    				}
			    			});
			    	        driveMenu.add(newAction);
			    	        driveCheck = true;
			    		} else if(actionTitle == ActionList.FireGun) {
			    			builder = "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					
			    					if(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getSpeciality() == Speciality.DRIVER) {
			    						int[] deckGun = a.getResult();
			    						redDice = deckGun[0];
			    						blackDice = deckGun[1];
			    						redReRoll = true;
			    						blackReRoll = true;
			    						showDeckGunRequest(a);
			    					} else {
			    						if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
	
										}
			    					}
			    					
			    				}
			    			});
			    	        fireDeckGunMenu.add(newAction);
			    	        firedeckCheck = true;
			    		
			    		} else if(actionTitle == ActionList.Flip) {
			    			int[] poiCoords = a.getTileLocation();
			    			builder = "Tile " +poiCoords[0] + "," + poiCoords[1]+ " APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
									}
			    				}
			    			});
			    	        flipPOIMenu.add(newAction);
			    	        flipPOICheck = true;
			    			
			    		} else if(actionTitle == ActionList.MoveWithHazmat) {
			    			if(a.getDirection() == 0) {
				    			builder = "Left, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
										}
				    				}
				    			});
				    	        moveWithHazmatMenu.add(newAction);
				    	        moveWithHazmatCheck = true;
				    			
				    		} else if(a.getDirection() == 1) {
				    			builder = "Up, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
										}
				    				}
				    			});
				    	        moveWithHazmatMenu.add(newAction);
				    	        moveWithHazmatCheck = true;
				    			
				    		} else if(a.getDirection() == 2) {
				    			builder = "Right, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
				    					}
				    				}
				    			});
				    	        moveWithHazmatMenu.add(newAction);
				    	        moveWithHazmatCheck = true;
				    			
				    		} else if(a.getDirection() == 3) {
				    			builder = "Down, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
				    					}
				    				}
				    			});
				    	        moveWithHazmatMenu.add(newAction);
				    	        moveWithHazmatCheck = true;
				    			
				    		} 
			    		} else if(actionTitle == ActionList.Pick) {
			    			builder = "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");

									}
			    					
			    				}
			    			});
			    	        pickMenu.add(newAction);
			    	        pickCheck = true;
			    		} else if(actionTitle == ActionList.Drop){
			    			builder = "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");

									}
			    					
			    				}
			    			});
			    	        dropMenu.add(newAction);
			    	        dropCheck = true;
			    		} else if(actionTitle == ActionList.RemoveHazmat) {
			    			builder = "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");

									}
			    					
			    				}
			    			});
			    	        disposeMenu.add(newAction);
			    	        disposeCheck = true;
			    		} else if(actionTitle == ActionList.Resuscitate) {
			    			System.out.println("weve reached this stage");
			    			builder = "APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");

									}
			    					
			    				}
			    			});
			    	        healMenu.add(newAction);
			    	        healCheck = true;
			    		}
			    	}
					
				}
		         
		        JMenuItem exitMenu = new JMenuItem("exit");
		        exitMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
				
					}
				});
		        
		        
		        if(toSmokeCheck) {
			        extinguishMenu.add(toSmokeMenu);
			        extinguishMenu.addSeparator();
		        }
		        if(completeCheck)extinguishMenu.add(completelyMenu);
		        
		        if(oneChopCheck) {
		        	chopMenu.add(onceMenu);
			        chopMenu.addSeparator();
		        }
		        if(twoChopCheck)chopMenu.add(twiceMenu);
		        
		        if(toSmokeCheck || completeCheck) {
		        	popupMenu.add(extinguishMenu);
			        popupMenu.addSeparator();
		        }
		        
		        if(oneChopCheck || twoChopCheck) {
		        	popupMenu.add(chopMenu);
			        popupMenu.addSeparator();
		        }
		        
		        if(doorCheck) {
		        	popupMenu.add(handleMenu);
			        popupMenu.addSeparator();
		        }
		        
		        if(moveCheck) {
		        	popupMenu.add(moveMenu);
			        popupMenu.addSeparator();
		        }
		        
		        if(moveWVCheck) {
		        	popupMenu.add(moveWithVictimMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        //Experienced
		        
		        if(changeCheck) {
		        	popupMenu.add(changeMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(commandCheck) {
		        	if(player1Check) {
		        		if(p1move) {
		        			commandP1Menu.add(p1MoveMenu);
		        			commandP1Menu.addSeparator();
		        		}
		        		if(p1moveH) {
		        			commandP1Menu.add(p1MoveWithHazmatMenu);
		        			commandP1Menu.addSeparator();
		        		}
		        		if(p1moveV) {
		        			commandP1Menu.add(p1MoveWithVictimMenu);
		        			commandP1Menu.addSeparator();
		        		}
		        		if(p1door) {
		        			commandP1Menu.add(p1HandleMenu);
		        			commandP1Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP1Menu);
		        		commandMenu.addSeparator();
		        	}
		        	if(player2Check) {
		        		if(p2move) {
		        			commandP2Menu.add(p2MoveMenu);
		        			commandP2Menu.addSeparator();
		        		}
		        		if(p2moveH) {
		        			commandP2Menu.add(p2MoveWithHazmatMenu);
		        			commandP2Menu.addSeparator();
		        		}
		        		if(p2moveV) {
		        			commandP2Menu.add(p2MoveWithVictimMenu);
		        			commandP2Menu.addSeparator();
		        		}
		        		if(p2door) {
		        			commandP2Menu.add(p2HandleMenu);
		        			commandP2Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP2Menu);
		        		commandMenu.addSeparator();
		        	}
		        	if(player3Check) {
		        		if(p3move) {
		        			commandP3Menu.add(p3MoveMenu);
		        			commandP3Menu.addSeparator();
		        		}
		        		if(p3moveH) {
		        			commandP3Menu.add(p3MoveWithHazmatMenu);
		        			commandP3Menu.addSeparator();
		        		}
		        		if(p3moveV) {
		        			commandP3Menu.add(p3MoveWithVictimMenu);
		        			commandP3Menu.addSeparator();
		        		}
		        		if(p3door) {
		        			commandP3Menu.add(p3HandleMenu);
		        			commandP3Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP3Menu);
		        		commandMenu.addSeparator();
		        	}
		        	if(player4Check) {
		        		if(p4move) {
		        			commandP4Menu.add(p4MoveMenu);
		        			commandP4Menu.addSeparator();
		        		}
		        		if(p4moveH) {
		        			commandP4Menu.add(p4MoveWithHazmatMenu);
		        			commandP4Menu.addSeparator();
		        		}
		        		if(p4moveV) {
		        			commandP4Menu.add(p4MoveWithVictimMenu);
		        			commandP4Menu.addSeparator();
		        		}
		        		if(p4door) {
		        			commandP4Menu.add(p4HandleMenu);
		        			commandP4Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP4Menu);
		        		commandMenu.addSeparator();
		        	}
		        	if(player5Check) {
		        		if(p5move) {
		        			commandP5Menu.add(p5MoveMenu);
		        			commandP5Menu.addSeparator();
		        		}
		        		if(p5moveH) {
		        			commandP5Menu.add(p5MoveWithHazmatMenu);
		        			commandP5Menu.addSeparator();
		        		}
		        		if(p5moveV) {
		        			commandP5Menu.add(p5MoveWithVictimMenu);
		        			commandP5Menu.addSeparator();
		        		}
		        		if(p5door) {
		        			commandP5Menu.add(p5HandleMenu);
		        			commandP5Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP5Menu);
		        		commandMenu.addSeparator();
		        	}
		        	if(player6Check) {
		        		if(p6move) {
		        			commandP6Menu.add(p6MoveMenu);
		        			commandP6Menu.addSeparator();
		        		}
		        		if(p6moveH) {
		        			commandP6Menu.add(p6MoveWithHazmatMenu);
		        			commandP6Menu.addSeparator();
		        		}
		        		if(p6moveV) {
		        			commandP6Menu.add(p6MoveWithVictimMenu);
		        			commandP6Menu.addSeparator();
		        		}
		        		if(p6door) {
		        			commandP6Menu.add(p6HandleMenu);
		        			commandP6Menu.addSeparator();
		        		}
		        		commandMenu.add(commandP6Menu);
		        		commandMenu.addSeparator();
		        	}
		        	popupMenu.add(commandMenu);
			        popupMenu.addSeparator();
		        } 
		       
		        if(disposeCheck) {
		        	popupMenu.add(disposeMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(moveWithHazmatCheck) {
		        	popupMenu.add(moveWithHazmatMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(pickCheck) {
		        	popupMenu.add(pickMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(dropCheck) {
		        	popupMenu.add(dropMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(flipPOICheck) {
		        	popupMenu.add(flipPOIMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(firedeckCheck) {
		        	popupMenu.add(fireDeckGunMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(healCheck) {
		        	popupMenu.add(healMenu);
			        popupMenu.addSeparator();
		        } 
		        
		        if(driveCheck) {
		        	popupMenu.add(driveMenu);
		        	popupMenu.addSeparator();
		        }
		        
		        popupMenu.add(finishMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(exitMenu);
		        
		        
		        popupMenu.show(component, x, y);		// very important
			}
			
			//generates the popUp menu for tiles that don't contain current FF
			public void showPopUpMenuPlace(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard, int[] coords) {
				JPopupMenu popupMenu = new JPopupMenu();
		        
		        JMenuItem endTurn = new JMenuItem("placeFireFighter");
		        endTurn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(sendPlaceFFRequest(coords)) {
							
						}
					}
				});
		         
		        JMenuItem fileMenu = new JMenuItem("exit");
		        fileMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
//						System.exit(0);
					}
				});
		        popupMenu.add(endTurn);
		        popupMenu.addSeparator();
		        popupMenu.add(fileMenu);
		        
		        popupMenu.show(component, x, y);		// very important
			}
			
			//generates the popUp menu for tiles that don't contain current FF
			public void showPopUpMenuOther(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard) {
				JPopupMenu popupMenu = new JPopupMenu();
		        
		        JMenuItem endTurn = new JMenuItem("end turn");
		        endTurn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
    					if(sendEndTurnRequest()){
    						System.out.println("this is the print that board is refreshing");
    						if(clientManager.getUsersGameState().isGameTerminated()) {
//    							
    						} else if(clientManager.getUsersGameState().isGameWon()) {
//    							
    						} else {
//    							
    						}
    					}
    					
    					
    				}
				});
		         
		        JMenuItem fileMenu = new JMenuItem("exit");
		        fileMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						launcher.backToMainMenu();
						clientManager.sendEndGameRequest();
					}
				});
		        popupMenu.add(endTurn);
		        popupMenu.addSeparator();
		        popupMenu.add(fileMenu);
		        
		        popupMenu.show(component, x, y);		// very important
			}
			
			//generates the popUp menu for tiles that don't contain current FF
			public void showPopUpMenuVehicle(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard) {
				JPopupMenu popupMenu = new JPopupMenu();
		        
		        JMenu placeAmbulance = new JMenu("Place Ambulance");
		        JMenu placeEngine = new JMenu("Place Engine");
		        
		        JMenuItem ambulanceLeft = new JMenuItem("Left");
		        ambulanceLeft.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(0, Vehicle.Ambulance)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem ambulanceTop = new JMenuItem("Top");
		        ambulanceTop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(1, Vehicle.Ambulance)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem ambulanceRight = new JMenuItem("Right");
		        ambulanceRight.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(2, Vehicle.Ambulance)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem ambulanceBottom = new JMenuItem("Bottom");
		        ambulanceBottom.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(3, Vehicle.Ambulance)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineLeft = new JMenuItem("Left");
		        engineLeft.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(0, Vehicle.Engine)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineTop = new JMenuItem("Top");
		        engineTop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(1, Vehicle.Engine)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem engineRight = new JMenuItem("Right");
		        engineRight.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(2, Vehicle.Engine)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineBottom = new JMenuItem("Bottom");
		        engineBottom.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(3, Vehicle.Engine)) {
//							refresh(clientManager.getUsersGameState());
//							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		         
		        JMenuItem fileMenu = new JMenuItem("exit");
		        fileMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
//						System.exit(0);
					}
				});
		        
		        placeAmbulance.add(ambulanceLeft);
		        placeAmbulance.add(ambulanceTop);
		        placeAmbulance.add(ambulanceRight);
		        placeAmbulance.add(ambulanceBottom);
		        placeEngine.add(engineLeft);
		        placeEngine.add(engineTop);
		        placeEngine.add(engineRight);
		        placeEngine.add(engineBottom);
		        
		        popupMenu.add(placeAmbulance);
		        popupMenu.addSeparator();
		        popupMenu.add(placeEngine);
		        popupMenu.addSeparator();
		        popupMenu.add(fileMenu);
		        
		        popupMenu.show(component, x, y);		// very important
			}
			
			public void showPopUpMenuSpeciality(Component component, int x, int y, GameState currentBoard, int[] coords) {
				JPopupMenu popupMenu = new JPopupMenu();
				JMenu specialityMenu = new JMenu("Select Speciality");
				String builder = "";
				JMenuItem info;
				
				
				JMenu specialityMenu1 = new JMenu();
				JMenu specialityMenu2 = new JMenu();
				JMenu specialityMenu3 = new JMenu();
				JMenu specialityMenu4 = new JMenu();
				JMenu specialityMenu5 = new JMenu();
				JMenu specialityMenu6 = new JMenu();
				boolean menu1 = false, menu2 = false, menu3 = false, menu4 = false, menu5 =false, menu6 = false;
				String menuLabel = "";
				for(int i = 0; i<6; i++) {
					menuLabel = "";
					if(myFFIndexes[i] != 7) {
						menuLabel = clientManager.getUserName() + " " + clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[i]).getColour().toString();
					
						switch (i) {
						case 0:
							specialityMenu1.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[0])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu1.add(info);
							}
							menu1 = true;
							break;
						case 1:
							specialityMenu2.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[1])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu2.add(info);
							}
							menu2 = true;
							break;
						case 2:
							specialityMenu3.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[2])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu3.add(info);
							}
							menu3 = true;
							break;
						case 3:
							specialityMenu4.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[3])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu4.add(info);
							}
							menu4 = true;
							break;
						case 4:
							specialityMenu5.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[4])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu5.add(info);
							}
							menu5 = true;
							break;
						case 5:
							specialityMenu6.setText(menuLabel);
							for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
								builder = s.toString();
								info = new JMenuItem(builder);
								
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if(sendSpecialitySelectionRequest(s, myFFIndexes[5])) {
											System.out.println("Speciality selected is " + s.toString());
										}
									}
								});
								specialityMenu6.add(info);
							}
							menu6 = true;
							break;
						}
					}
				}
				
				if(menu1) {
					specialityMenu.add(specialityMenu1);
					specialityMenu.addSeparator();
				} 
				if(menu2) {
					specialityMenu.add(specialityMenu2);
					specialityMenu.addSeparator();
				} 
				if(menu3) {
					specialityMenu.add(specialityMenu3);
					specialityMenu.addSeparator();
				} 
				if(menu4) {
					specialityMenu.add(specialityMenu4);
					specialityMenu.addSeparator();
				} 
				if(menu5) {
					specialityMenu.add(specialityMenu5);
					specialityMenu.addSeparator();
				} 
				if(menu6) {
					specialityMenu.add(specialityMenu6);
					specialityMenu.addSeparator();
				} 
				
				
				
				
//				for(Speciality s : clientManager.getUsersGameState().getFreeSpecialities()) {
//					builder = s.toString();
//					info = new JMenuItem(builder);
//					
//					info.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							if(sendSpecialitySelectionRequest(s)) {
//								System.out.println("Speciality selected is " + s.toString());
//							}
//						}
//					});
//					specialityMenu.add(info);
//				}
				
				boolean flag = true;
				for(Firefighter f : clientManager.getUsersGameState().getFireFighterList()) {
					if(f.getSpeciality() == null) flag = false;
				}
				if(flag && myIndex == 0) {
					JMenuItem start = new JMenuItem("End Selection");
					
					start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendSelectionEndRequest()) {
							}
						}

						
					});
					specialityMenu.add(start);
				}
				
				popupMenu.add(specialityMenu);
				popupMenu.show(component, x, y);
			}
			
			public void showPopUpMenuFFSelector(Component component, int x, int y, GameState currentBoard, int[] coords) {
				JPopupMenu popupMenu = new JPopupMenu();
				JMenu firefighterMenu = new JMenu("Select FireFighter");
				String builder = "";
				JMenuItem info;
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 0) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(0).getOwner().getUserName() + "|" + clientManager.getUsersGameState().getFreeFirefighters().get(0).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(0).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 1) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(1).getOwner().getUserName() + "|" + clientManager.getUsersGameState().getFreeFirefighters().get(1).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(1).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 2) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(2).getOwner().getUserName() + "|" + clientManager.getUsersGameState().getFreeFirefighters().get(2).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(2).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 3) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(3).getOwner().getUserName()+ "|" + clientManager.getUsersGameState().getFreeFirefighters().get(3).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(3).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 4) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(4).getOwner().getUserName()+ "|" + clientManager.getUsersGameState().getFreeFirefighters().get(4).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(4).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				if(clientManager.getUsersGameState().getFreeFirefighters().size() > 5) {
					builder = clientManager.getUsersGameState().getFreeFirefighters().get(5).getOwner().getUserName()+ "|" + clientManager.getUsersGameState().getFreeFirefighters().get(5).getColour().toString();
					info = new JMenuItem(builder);
					
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							desiredFFindexColour = clientManager.getUsersGameState().getFreeFirefighters().get(5).getColour();
							if(sendFireFighterSelectionRequest(desiredFFindexColour)) {
								
							}
						}
					});
					firefighterMenu.add(info);
				}
				
				
				
				
				boolean flag = clientManager.getUsersGameState().getFreeFirefighters().size() == 0;
				
				if(flag) {
					JMenuItem start = new JMenuItem("End Selection");
					
					start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendSelectionEndRequest()) {
							}
						}

						
					});
					firefighterMenu.add(start);
				}
				
				popupMenu.add(firefighterMenu);
				popupMenu.show(component, x, y);
			}
			
			//generates the popUp menu for tiles that don't contain current FF
				public void showPopUpMenuInfo(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard, int[] coords) {
					JPopupMenu popupMenu = new JPopupMenu();
					JMenu poiMenu = new JMenu("POIs");
					JMenu firefighterMenu = new JMenu("Firefighters");
					JMenuItem hazmatMenu = new JMenuItem("Hazmat");
					JMenuItem hotSpotMenu = new JMenuItem("HotSpot");
					JMenuItem ambulanceMenu = new JMenuItem("Ambulance");
					JMenuItem engineMenu = new JMenuItem("Engine");
					Tile myTile = currentBoard.returnTile(coords[0], coords[1]);
			        String builder = "";
			        JMenuItem info;
					for(POI p : myTile.getPoiList()) {
						if(p.isRevealed()) {
							if(p.isVictim()) {
								builder = "Victim";
							}else {
								builder = "False Alarm";
							}
						}else {
							builder = "Point of Interest";
						}
						info = new JMenuItem(builder);
						poiMenu.add(info);
					}
					for(Firefighter f: myTile.getFirefighterList()) {
						if(clientManager.getUsersGameState().isExperienced()) {
							builder = f.getOwner().getUserName() + "    Speciality: " + f.getSpeciality().toString() + " AP: "+ f.getAP() + " SAP: " + f.getSavedAP();
							info = new JMenuItem(builder);
							firefighterMenu.add(info);
						} else {
							builder = f.getOwner().getUserName() + ": " + f.getColour().toString() + " fireman." + " AP: "+ f.getAP();
							info = new JMenuItem(builder);
							firefighterMenu.add(info);
						}
					}
					if(myTile.containsPOI()) {
						 popupMenu.add(poiMenu);
					      popupMenu.addSeparator();
					}
					if(myTile.containsFirefighter()) {
						 popupMenu.add(firefighterMenu);
						 popupMenu.addSeparator();
					}
			        if(clientManager.getUsersGameState().isExperienced()) {
			        	if(myTile.containsHazmat()) {
			        		popupMenu.add(hazmatMenu);
				        	popupMenu.addSeparator();
			        	}
			        	if(myTile.containsHotSpot()) {
			        		popupMenu.add(hotSpotMenu);
			        	}
			        	if(myTile.getParkingSpot() != null) {
			        		if(myTile.getParkingSpot().getCar()) {
			        			if(myTile.getParkingType() == Vehicle.Ambulance) {
			        				popupMenu.add(ambulanceMenu);
			        			}
			        			if(myTile.getParkingType() == Vehicle.Engine) {
			        				popupMenu.add(engineMenu);
			        			}
			        		}
			        	}
			        }
			        
			        popupMenu.show(component, x, y);		// very important
				}
			
		}
		
		public static int[] calculateTileCoords(int tileId) {
			int i = tileId/10;
			int j = tileId%10;
			int[] result = {i,j};
			
			return result;
		}
		
		public static void setPlacing(boolean update) {
			placing = update;
		}
		
		public static void setPlaying(boolean update) {
			playing = update;
		}
		
		public void showGameTermination() {
			gameTermination = null;
			PopupFactory gameT = new PopupFactory();
			JPanel gameTPanel = new JPanel();
			gameTPanel.setLayout(null);
			
			if(clientManager.getUsersGameState().isGameTerminated()) {
				if(clientManager.getUsersGameState().getDamageCounter() >= 24) {
					
					gameTPanel.setLayout(new BorderLayout());
					gameTPanel.setPreferredSize(new Dimension(300,300));
					gameTPanel.setBackground(tileColorRed);
					Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
					gameTPanel.setBorder(blackline);
					
					JTextArea text = new JTextArea();
					String gameOverPrompt = "   Game over.\n   The house has collapsed.";
					text.setText(gameOverPrompt);
					text.setBackground(tileColorRed);
					
					JButton backToMenuButton = new JButton("Back to Menu");
					backToMenuButton.setPreferredSize(new Dimension(40,40));
					backToMenuButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameTermination.hide();
							launcher.backToMainMenu();
							clientManager.sendEndGameRequest();
							//return back to menu
						}
					});
					
					gameTPanel.add(text, BorderLayout.NORTH);
					gameTPanel.add(backToMenuButton, BorderLayout.SOUTH);
					gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
					
				} else if(clientManager.getUsersGameState().getLostVictimsList().size() >= 4) {
					gameTPanel.setLayout(new BorderLayout());
					gameTPanel.setPreferredSize(new Dimension(300,300));
					gameTPanel.setBackground(tileColorRed);
					Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
					gameTPanel.setBorder(blackline);
					
					JTextArea text = new JTextArea();
					String gameOverPrompt = "   Game over.\n   4 victims were lost.";
					text.setText(gameOverPrompt);
					text.setBackground(tileColorRed);
					
					JButton backToMenuButton = new JButton("Back to Menu");
					backToMenuButton.setPreferredSize(new Dimension(40,40));
					backToMenuButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameTermination.hide();
							launcher.backToMainMenu();
							clientManager.sendEndGameRequest();
							//return back to menu
						}
					});
					
					gameTPanel.add(text, BorderLayout.NORTH);
					gameTPanel.add(backToMenuButton, BorderLayout.SOUTH);
					gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
				}
			} else if(clientManager.getUsersGameState().isGameWon()) {
				gameTPanel.setLayout(new BorderLayout());
				gameTPanel.setPreferredSize(new Dimension(300,300));
				gameTPanel.setBackground(tileColorGreen);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
				gameTPanel.setBorder(blackline);
				
				JTextArea text = new JTextArea();
				String gameOverPrompt = "   Game over.\n   7 victims were saved in time.";
				text.setText(gameOverPrompt);
				text.setBackground(tileColorGreen);
				
				JButton backToMenuButton = new JButton("Back to Menu");
				backToMenuButton.setPreferredSize(new Dimension(40,40));
				backToMenuButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gameTermination.hide();
						launcher.backToMainMenu();
						clientManager.sendEndGameRequest();
						//return back to menu
					}
				});
				
				gameTPanel.add(text, BorderLayout.NORTH);
				gameTPanel.add(backToMenuButton, BorderLayout.SOUTH);
				gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
			}
			
			gameTermination.show();
		}
		
		public void hideGameTermination() {
			if(gameTermination != null) {
				gameTermination.hide();
			}
		}
		
		public void showAdvanceFireString(String message) {
			advFire = null;
			PopupFactory gameLogPopup = new PopupFactory();
			JPanel gameLog = new JPanel(new BorderLayout());
			JTextArea logtext = new JTextArea();
			logtext.append(message);
			logtext.setLineWrap(true);
			
			JScrollPane scroll = new JScrollPane (logtext, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setPreferredSize(new Dimension(40,230));
			
			JButton okButton = new JButton("ok");
			okButton.setPreferredSize(new Dimension(20,20));
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					advFire.hide();
					advFire = gameLogPopup.getPopup(rightPanel, gameLog, 1100, 50);
				}
			});
			gameLog.setPreferredSize(new Dimension(350,400));
			gameLog.setBackground(tileColorWhite);
			Border blackline = BorderFactory.createLineBorder(tileColorBlack,10);
			gameLog.setBorder(blackline);
			gameLog.add(scroll, BorderLayout.NORTH);
			gameLog.add(okButton, BorderLayout.SOUTH);
			advFire = gameLogPopup.getPopup(rightPanel, gameLog, 1100, 50);
			
			advFire.show();
		}
		
		public void showTurnNotifier(int i) {
			turnNotifier = null;
			PopupFactory turnPopUp = new PopupFactory();
			JPanel turnLog = new JPanel(new BorderLayout());
			JTextArea turnText = new JTextArea();
			String notification = "It's your turn!" + "\n" + "Please make a turn for Firefighter " + clientManager.getUsersGameState().getFireFighterList().get(i).getColour().toString(); //Edit this to have FF details
			turnText.append(notification);
			turnText.setFont(new Font("AvantGarde", Font.PLAIN, 20));
			turnText.setLineWrap(true);
			
			JButton okButton = new JButton("ok");
			okButton.setPreferredSize(new Dimension(20,20));
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					turnNotifier.hide();
					turnNotifier = turnPopUp.getPopup(rightPanel, turnLog, 450, 200);
				}
			});
			
			turnLog.setPreferredSize(new Dimension(500,150));
			turnLog.setBackground(tileColorWhite);
			turnLog.add(okButton, BorderLayout.SOUTH);
			turnLog.add(turnText, BorderLayout.CENTER);
			turnNotifier = turnPopUp.getPopup(rightPanel, turnLog, 450, 200);
			
			turnNotifier.show();
		}
		
		public void showTurnPopUp() {
			for(int i=0;i<6;i++) {
				if(myFFIndexes[i] == currentBoard.getActiveFireFighterIndex()) {
					showTurnNotifier(i);
				}
			}
		}
		
		public void hideAdvPanel() {
			if(advFire != null) {
				advFire.hide();
			}
			
		}
		
		public void hideTurnPanel() {
			if(turnNotifier != null) {
				turnNotifier.hide();
			}
		}
		
		public void showRideRequest() {
			rideRequest = null;
			PopupFactory gameT = new PopupFactory();
			JPanel gameTPanel = new JPanel(new GridLayout(7,1));
			gameTPanel.setPreferredSize(new Dimension(700,700));
			gameTPanel.setBackground(tileColorWhite);
			Border blackline = BorderFactory.createLineBorder(tileColorBlack,10);
			gameTPanel.setBorder(blackline);
			JTextArea text = new JTextArea();
			
			
			JPanel FF0 = new JPanel(new GridLayout(3,1));
			JPanel FF1 = new JPanel(new GridLayout(3,1));
			JPanel FF2 = new JPanel(new GridLayout(3,1));
			JPanel FF3 = new JPanel(new GridLayout(3,1));
			JPanel FF4 = new JPanel(new GridLayout(3,1));
			JPanel FF5 = new JPanel(new GridLayout(3,1));
			
			JTextArea text0 = new JTextArea();
			JTextArea text1 = new JTextArea();
			JTextArea text2 = new JTextArea();
			JTextArea text3 = new JTextArea();
			JTextArea text4 = new JTextArea();
			JTextArea text5 = new JTextArea();
			JTextArea text6 = new JTextArea();
			
			String rideReq = "You have firefighters who can ride. Please respond for each of them.";
			String header0 = "";
			String header1 = ""; 
			String header2 = "";
			String header3 = ""; 
			String header4 = "";
			String header5 = ""; 
			
			String rideInform = "We are waiting on the ride responses";
			System.out.println("myyyyyy index " + myIndex);
			
			boolean menu0 = false, menu1 = false, menu2 = false, menu3 = false, menu4 = false, menu5 = false;
			for(int i = 0; i< 6; i ++) {
				if(myFFIndexes[i] != 7) {
					switch (i) {
						case 0:
							menu0 = true;
							header0 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[0]).getColour().toString() + " Fireman";
							break;
						case 1:
							menu1 = true;
							header1 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[1]).getColour().toString() + " Fireman";
							break;
						case 2:
							menu2 = true;
							header2 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[2]).getColour().toString() + " Fireman";
							break;
						case 3:
							menu3 = true;
							header3 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[3]).getColour().toString() + " Fireman";
							break;
						case 4:
							menu4 = true;
							header4 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[4]).getColour().toString() + " Fireman";
							break;
						case 5:
							menu5 = true;
							header5 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[5]).getColour().toString() + " Fireman";
							break;
					}
				} else {
					rode[i] = true;
				}
			}
			
			if(menu0 || menu1 || menu2 || menu3 || menu4 || menu5) {
				text.setText(rideReq);
				gameTPanel.add(text);
			}
			
			if(menu0) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[0])) {
					text0.setText(header0);
					FF0.add(text0);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("helloashf");
							if(sendRideResponse(true, myFFIndexes[0])) {
								rode[0] = true;
								System.out.println("hello");
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[0]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[0])) {
								rode[0] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[0]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
									
								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF0.add(yesButton);
					FF0.add(noButton);
					if(!rode[0]) {
						gameTPanel.add(FF0);
					}
					
					
				}
			} 
			
			if(menu1) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[1])) {
					text1.setText(header1);
					FF1.add(text1);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(true, myFFIndexes[1])) {
								rode[1] = true;
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[1]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[1])) {
								rode[1] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[1]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF1.add(yesButton);
					FF1.add(noButton);
					if(!rode[1]) {
						gameTPanel.add(FF1);
					}
					
				}
			}
			if(menu2) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[2])) {
					text2.setText(header2);
					FF2.add(text2);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(true, myFFIndexes[2])) {
								rode[2] = true;
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[2]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[2])) {
								rode[2] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[2]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF2.add(yesButton);
					FF2.add(noButton);
					if(!rode[2]) {
						gameTPanel.add(FF2);
					}
					
				}
			}
			if(menu3) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[3])) {
					text3.setText(header3);
					FF3.add(text3);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(true, myFFIndexes[3])) {
								rode[3] = true;
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[3]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[3])) {
								rode[3] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[3]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF3.add(yesButton);
					FF3.add(noButton);
					if(!rode[3]) {
						gameTPanel.add(FF3);
					}
					
				}
			}
			if(menu4) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[4])) {
					text4.setText(header4);
					FF4.add(text4);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(true, myFFIndexes[4])) {
								rode[4] = true;
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[4]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[4])) {
								rode[4] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[4]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {

								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF4.add(yesButton);
					FF4.add(noButton);
					if(!rode[4]) {
						gameTPanel.add(FF4);
					}
				}
			}
			if(menu5) {
				if(clientManager.getUsersGameState().toDisplayRidePopUp(myFFIndexes[5])) {
					text5.setText(header5);
					FF5.add(text5);
					JButton yesButton = new JButton("Yes");
					yesButton.setPreferredSize(new Dimension(20,20));
					yesButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(true, myFFIndexes[5])) {
								rode[5] = true;
								clientManager.getUsersGameState().setRideOption(true, myFFIndexes[5]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					

					JButton noButton = new JButton("No");
					noButton.setPreferredSize(new Dimension(20,20));
					noButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(sendRideResponse(false, myFFIndexes[5])) {
								rode[5] = true;
								clientManager.getUsersGameState().setRideOption(false, myFFIndexes[5]);
								rideRequest.hide();
								rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
								if(clientManager.getUsersGameState().hasEveryoneResponded()) {
								} else {
									showRideRequest();
								}
							}
						}
					});
					
					FF5.add(yesButton);
					FF5.add(noButton);
					if(!rode[5]) {
						gameTPanel.add(FF5);
					}
				}
			}
			
			if(rode[0] && rode[1] && rode[2] && rode[3] && rode[4] && rode[5]) {
				System.out.println(clientManager.getUsersGameState().toDisplayRidePopUp(myIndex));
				text.setText(rideInform);
				
				JButton okButton = new JButton("ok");
				okButton.setPreferredSize(new Dimension(75,75));
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rideRequest.hide();
						rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
					}
				});
				
				gameTPanel.add(text, BorderLayout.NORTH);
				gameTPanel.add(okButton, BorderLayout.SOUTH);
			}
			
			
			
					
			
			
			
//			if(clientManager.getUsersGameState().toDisplayRidePopUp(myIndex)) {
//				System.out.println(clientManager.getUsersGameState().toDisplayRidePopUp(myIndex));
//				text.setText(rideReq);
//				
//				JButton yesButton = new JButton("Yes");
//				yesButton.setPreferredSize(new Dimension(40,40));
//				yesButton.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						if(sendRideResponse(true, myIndex)) {
//							rideRequest.hide();
//							rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
//						}
//					}
//				});
//				
//
//				JButton noButton = new JButton("No");
//				noButton.setPreferredSize(new Dimension(40,40));
//				noButton.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						if(sendRideResponse(false, myIndex)) {
//							rideRequest.hide();
//							rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
//						}
//					}
//				});
//				
//				JPanel responsePanel = new JPanel();
//				responsePanel.setLayout(new GridLayout(2,1));
//				responsePanel.add(yesButton);
//				responsePanel.add(noButton);
//				gameTPanel.add(text);
//				gameTPanel.add(responsePanel);
//			} 
//			else {
//				System.out.println(clientManager.getUsersGameState().toDisplayRidePopUp(myIndex));
//				text.setText(rideInform);
//				
//				JButton okButton = new JButton("ok");
//				okButton.setPreferredSize(new Dimension(75,75));
//				okButton.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						rideRequest.hide();
//						rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
//					}
//				});
//				gameTPanel.setPreferredSize(new Dimension(700,700));
//				gameTPanel.setBackground(tileColorWhite);
//				Border blackline = BorderFactory.createLineBorder(tileColorBlack,10);
//				gameTPanel.setBorder(blackline);
//				gameTPanel.add(text, BorderLayout.NORTH);
//				gameTPanel.add(okButton, BorderLayout.SOUTH);
//				
//			}
			
			rideRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
			
			rideRequest.show();
		}
		
		public void hideRidePanel() {
			if(rideRequest != null) {
				rideRequest.hide();
			}
			
		}	
		
		
		
		public void showDodgeRequest() {
			System.out.println("show dodge request trigged");
			if(dodgeRequest!= null) {
				dodgeRequest.hide();
			}
			dodgeRequest = null;
			PopupFactory gameT = new PopupFactory();
			JPanel gameTPanel = new JPanel(new GridLayout(7,1));
			JTextArea text = new JTextArea();
			String dodgeReq = "You are in danger!! Which direction would you like to dodge?";
			text.setText(dodgeReq);
			
			gameTPanel.setPreferredSize(new Dimension(700,700));
			gameTPanel.setBackground(tileColorWhite);
			Border blackline = BorderFactory.createLineBorder(tileColorBlack,10);
			gameTPanel.setBorder(blackline);
			gameTPanel.add(text);
			
			
			JPanel FF0 = new JPanel(new GridLayout(6,1));
			JPanel FF1 = new JPanel(new GridLayout(6,1));
			JPanel FF2 = new JPanel(new GridLayout(6,1));
			JPanel FF3 = new JPanel(new GridLayout(6,1));
			JPanel FF4 = new JPanel(new GridLayout(6,1));
			JPanel FF5 = new JPanel(new GridLayout(6,1));
			
			JTextArea text0 = new JTextArea();
			JTextArea text1 = new JTextArea();
			JTextArea text2 = new JTextArea();
			JTextArea text3 = new JTextArea();
			JTextArea text4 = new JTextArea();
			JTextArea text5 = new JTextArea();
			
			String header0 = "";
			String header1 = ""; 
			String header2 = "";
			String header3 = ""; 
			String header4 = "";
			String header5 = ""; 
			
			ArrayList<actions.Action> dodgeOptions0 = new ArrayList<actions.Action>();
			ArrayList<actions.Action> dodgeOptions1 = new ArrayList<actions.Action>();
			ArrayList<actions.Action> dodgeOptions2 = new ArrayList<actions.Action>();
			ArrayList<actions.Action> dodgeOptions3 = new ArrayList<actions.Action>();
			ArrayList<actions.Action> dodgeOptions4 = new ArrayList<actions.Action>();
			ArrayList<actions.Action> dodgeOptions5 = new ArrayList<actions.Action>();
			
			boolean menu0 = false, menu1 = false, menu2 = false, menu3 = false, menu4 = false, menu5 = false;
			
			boolean refresh = false;
			boolean isendTurner = false;
			for(int i = 0; i< 6; i ++) {
				if(myFFIndexes[i] != 7) {
					switch (i) {
						case 0:
							menu0 = true;
							refresh = true;
							header0 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[0]).getColour().toString() + " Fireman";
							dodgeOptions0 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[0]));
//							dodged[0] = false;
							if(dodgeOptions0.size() == 0) {
								menu0 = false;
								dodged[0] = true;
							}
							break;
						case 1:
							menu1 = true;
							refresh = true;
							header1 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[1]).getColour().toString() + " Fireman";
							dodgeOptions1 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[1]));
//							dodged[1] = false;
							if(dodgeOptions1.size() == 0) {
								menu1 = false;
								dodged[1] = true;
							}
							break;
						case 2:
							menu2 = true;
							refresh = true;
							header2 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[2]).getColour().toString() + " Fireman";
							dodgeOptions2 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[2]));
//							dodged[2] = false;
							if(dodgeOptions2.size() == 0) {
								menu2 = false;
								dodged[2] = true;
							}
							break;
						case 3:
							menu3 = true;
							refresh = true;
							header3 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[3]).getColour().toString() + " Fireman";
							dodgeOptions3 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[3]));
//							dodged[3] = false;
							if(dodgeOptions3.size() == 0) {
								menu3 = false;
								dodged[3] = true;
							}
							break;
						case 4:
							menu4 = true;
							refresh = true;
							header4 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[4]).getColour().toString() + " Fireman";
							dodgeOptions4 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[4]));
//							dodged[4] = false;
							if(dodgeOptions4.size() == 0) {
								menu4 = false;
								dodged[4] = true;
							}
							break;
						case 5:
							menu5 = true;
							refresh = true;
							header5 = clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[5]).getColour().toString() + " Fireman";
							dodgeOptions5 = clientManager.getUsersGameState().getDodgingHashMap().get(clientManager.getUsersGameState().getFireFighterList().get(myFFIndexes[5]));
//							dodged[5] = false;
							if(dodgeOptions5.size() == 0) {
								menu5 = false;
								dodged[5] = true;
							}
							break;
					}
				}
				else {
					dodged[i] = true;
				}
				
				if(myFFIndexes[i] == clientManager.getUsersGameState().getActiveFireFighterIndex()) {
					isendTurner = true;
				}
			}
			
			if(menu0) {
				text0.setText(header0);
				FF0.add(text0);
				JButton dodgeButtonLeft0 = new JButton("Dodge left");
				JButton dodgeButtonUp0 = new JButton("Dodge up");
				JButton dodgeButtonRight0 = new JButton("Dodge right");
				JButton dodgeButtonDown0 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions0) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft0.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft0.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[0]);
								dodged[0] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF0.add(dodgeButtonLeft0);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp0.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp0.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[0]);
								dodged[0] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF0.add(dodgeButtonUp0);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight0.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight0.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[0]);
								dodged[0] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF0.add(dodgeButtonRight0);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown0.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown0.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[0]);
								dodged[0] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF0.add(dodgeButtonDown0);
					}
				}
				
				if(flag) {
					JButton okButton0 = new JButton("Don't Dodge");
					okButton0.setPreferredSize(new Dimension(20,20));
					okButton0.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[0]);
							dodged[0] = true;
							showDodgeRequest();
						}
					});
					FF0.add(okButton0);
				}
			}
			if(menu1) {
				text1.setText(header1);
				FF1.add(text1);
				JButton dodgeButtonLeft1 = new JButton("Dodge left");
				JButton dodgeButtonUp1 = new JButton("Dodge up");
				JButton dodgeButtonRight1 = new JButton("Dodge right");
				JButton dodgeButtonDown1 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions1) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft1.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[1]);
								dodged[1] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF1.add(dodgeButtonLeft1);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp1.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[1]);
								dodged[1] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF1.add(dodgeButtonUp1);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight1.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[1]);
								dodged[1] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF1.add(dodgeButtonRight1);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown1.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[1]);
								dodged[1] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF1.add(dodgeButtonDown1);
					}
				}
				
				if(flag) {
					JButton okButton1 = new JButton("Don't Dodge");
					okButton1.setPreferredSize(new Dimension(20,20));
					okButton1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[1]);
							dodged[1] = true;
							showDodgeRequest();
						}
					});
					FF1.add(okButton1);
				}
			}
			if(menu2) {
				text2.setText(header2);
				FF2.add(text2);
				JButton dodgeButtonLeft2 = new JButton("Dodge left");
				JButton dodgeButtonUp2 = new JButton("Dodge up");
				JButton dodgeButtonRight2 = new JButton("Dodge right");
				JButton dodgeButtonDown2 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions2) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft2.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft2.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[2]);
								dodged[2] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF2.add(dodgeButtonLeft2);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp2.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp2.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[2]);
								dodged[2] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF2.add(dodgeButtonUp2);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight2.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight2.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[2]);
								dodged[2] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF2.add(dodgeButtonRight2);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown2.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown2.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[2]);
								dodged[2] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF2.add(dodgeButtonDown2);
					}
				}
				
				if(flag) {
					JButton okButton2 = new JButton("Don't Dodge");
					okButton2.setPreferredSize(new Dimension(20,20));
					okButton2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[2]);
							dodged[2] = true;
							showDodgeRequest();
						}
					});
					FF2.add(okButton2);
				}
			}
			if(menu3) {
				text3.setText(header3);
				FF3.add(text3);
				JButton dodgeButtonLeft3 = new JButton("Dodge left");
				JButton dodgeButtonUp3 = new JButton("Dodge up");
				JButton dodgeButtonRight3 = new JButton("Dodge right");
				JButton dodgeButtonDown3 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions3) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft3.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft3.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[3]);
								dodged[3] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF3.add(dodgeButtonLeft3);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp3.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp3.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[3]);
								dodged[3] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF3.add(dodgeButtonUp3);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight3.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight3.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[3]);
								dodged[3] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF3.add(dodgeButtonRight3);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown3.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown3.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[3]);
								dodged[3] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF3.add(dodgeButtonDown3);
					}
				}
				
				if(flag) {
					JButton okButton3 = new JButton("Don't Dodge");
					okButton3.setPreferredSize(new Dimension(20,20));
					okButton3.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[3]);
							dodged[3] = true;
							showDodgeRequest();
						}
					});
					FF3.add(okButton3);
				}
			}
			if(menu4) {
				text4.setText(header4);
				FF4.add(text4);
				JButton dodgeButtonLeft4 = new JButton("Dodge left");
				JButton dodgeButtonUp4 = new JButton("Dodge up");
				JButton dodgeButtonRight4 = new JButton("Dodge right");
				JButton dodgeButtonDown4 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions0) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft4.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft4.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[4]);
								dodged[4] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF4.add(dodgeButtonLeft4);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp4.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp4.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[4]);
								dodged[4] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF4.add(dodgeButtonUp4);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight4.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight4.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[4]);
								dodged[4] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF4.add(dodgeButtonRight4);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown4.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown4.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[4]);
								dodged[4] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF4.add(dodgeButtonDown4);
					}
				}
				
				if(flag) {
					JButton okButton4 = new JButton("Don't Dodge");
					okButton4.setPreferredSize(new Dimension(20,20));
					okButton4.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[4]);
							dodged[4] = true;
							showDodgeRequest();
						}
					});
					FF4.add(okButton4);
				}
			}
			if(menu5) {
				text5.setText(header5);
				FF5.add(text5);
				JButton dodgeButtonLeft5 = new JButton("Dodge left");
				JButton dodgeButtonUp5 = new JButton("Dodge up");
				JButton dodgeButtonRight5 = new JButton("Dodge right");
				JButton dodgeButtonDown5 = new JButton("Dodge down");
				
				boolean flag = false;
				for(actions.Action a : dodgeOptions0) {
					System.out.println("dodging buttons");
					if(a.getDirection() == 0) {
						dodgeButtonLeft5.setPreferredSize(new Dimension(20,20));
						dodgeButtonLeft5.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[5]);
								dodged[5] = true;
								showDodgeRequest();
							}
	
						});
						flag = true;
						FF5.add(dodgeButtonLeft5);		
					} else if (a.getDirection() == 1) {
						dodgeButtonUp5.setPreferredSize(new Dimension(20,20));
						dodgeButtonUp5.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[5]);
								dodged[5] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF5.add(dodgeButtonUp5);
					} else if (a.getDirection() == 2) {
						dodgeButtonRight5.setPreferredSize(new Dimension(20,20));
						dodgeButtonRight5.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[5]);
								dodged[5] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF5.add(dodgeButtonRight5);
					} else if (a.getDirection() == 3) {
						dodgeButtonDown5.setPreferredSize(new Dimension(20,20));
						dodgeButtonDown5.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("this should not happen");
								dodgeRequest.hide();
								dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
								sendDodgeAnswer(a,myFFIndexes[5]);
								dodged[5] = true;
								showDodgeRequest();
							}
						});
						flag = true;
						FF5.add(dodgeButtonDown5);
					}
				}
				
				if(flag) {
					JButton okButton5 = new JButton("Don't Dodge");
					okButton5.setPreferredSize(new Dimension(20,20));
					okButton5.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("OK BUTTON CLICKED");
							dodgeRequest.hide();
							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
							sendDodgeAnswer(null,myFFIndexes[5]);
							dodged[5] = true;
							showDodgeRequest();
						}
					});
					FF5.add(okButton5);
				}
			}
			
			
			if(menu0 && !dodged[0]){
				System.out.println("d0" + dodged[0]);
				gameTPanel.add(FF0);
			}
			if(menu1 && !dodged[1]){
				System.out.println("d1" + dodged[1]);
				gameTPanel.add(FF1);
			}
			if(menu2 && !dodged[2]){
				System.out.println("d2" + dodged[2]);
				gameTPanel.add(FF2);
			}
			if(menu3 && !dodged[3]){
				System.out.println("d3" + dodged[3]);
				gameTPanel.add(FF3);
			}
			if(menu4 && !dodged[4]){
				System.out.println("d4" + dodged[4]);
				gameTPanel.add(FF4);
			}
			if(menu5 && !dodged[5]){
				System.out.println("d5" + dodged[5]);
				gameTPanel.add(FF5);
			}
			
//			JButton dodgeButton0 = new JButton("Dodge left");
//			JButton dodgeButton1 = new JButton("Dodge up");
//			JButton dodgeButton2 = new JButton("Dodge right");
//			JButton dodgeButton3 = new JButton("Dodge down");
//			
//			JPanel responsePanel = new JPanel();
//			responsePanel.setLayout(new GridLayout(6,1));
//			boolean flag = false;
//			for(actions.Action a : dodgeOptions0) {
//				System.out.println("dodging buttons");
//				if(a.getDirection() == 0) {
//					dodgeButton0.setPreferredSize(new Dimension(10,10));
//					dodgeButton0.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							System.out.println("this should not happen");
//							dodgeRequest.hide();
//							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
//							sendDodgeAnswer(a,myIndex);
//						}
//
//					});
//					flag = true;
//					responsePanel.add(dodgeButton0);		
//				} else if (a.getDirection() == 1) {
//					dodgeButton1.setPreferredSize(new Dimension(10,10));
//					dodgeButton1.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							System.out.println("this should not happen");
//							dodgeRequest.hide();
//							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
//							sendDodgeAnswer(a,myIndex);
//						}
//					});
//					flag = true;
//					responsePanel.add(dodgeButton1);
//				} else if (a.getDirection() == 2) {
//					dodgeButton2.setPreferredSize(new Dimension(10,10));
//					dodgeButton2.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							System.out.println("this should not happen");
//							dodgeRequest.hide();
//							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
//							sendDodgeAnswer(a,myIndex);
//						}
//					});
//					flag = true;
//					responsePanel.add(dodgeButton2);
//				} else if (a.getDirection() == 3) {
//					dodgeButton3.setPreferredSize(new Dimension(10,10));
//					dodgeButton3.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							System.out.println("this should not happen");
//							dodgeRequest.hide();
//							dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
//							sendDodgeAnswer(a,myIndex);
//						}
//					});
//					flag = true;
//					responsePanel.add(dodgeButton3);
//				}
//			}
//			
//			if(flag) {
//				JButton okButton = new JButton("Don't Dodge");
//				okButton.setPreferredSize(new Dimension(10,10));
//				okButton.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						System.out.println("OK BUTTON CLICKED");
//						dodgeRequest.hide();
//						dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
//						sendDodgeAnswer(null,myIndex);
//					}
//				});
//				responsePanel.add(okButton);
//			} 
			
			
			if(dodged[0] && dodged[1] && dodged[2] && dodged[3] && dodged[4] && dodged[5]){
				JButton okButton; 
				if(refresh && isendTurner) {
					okButton = new JButton("REFRESH");
					refreshers = true;
				} else {
					okButton = new JButton("OK");
					refreshers = false;
				}
				okButton.setPreferredSize(new Dimension(10,10));
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("OK BUTTON CLICKED");
						dodgeRequest.hide();
						dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
						if(refreshers) {
							sendRefreshRequest();
						}
						
					}
				});
				gameTPanel.add(okButton);
			}
		
			
			dodgeRequest = gameT.getPopup(rightPanel, gameTPanel, 400, 50);
			
			dodgeRequest.show();
			refreshers = false;
		}
		
		public void hideDodgePanel() {
			System.out.println("this should not be called");
			if(dodgeRequest != null) {
				dodgeRequest.hide();
			}
			
		}	
		
		
		public void showDeckGunRequest(actions.Action a) {
			deckGunRequest = null;
			PopupFactory gameT = new PopupFactory();
			JPanel gameTPanel = new JPanel(new BorderLayout());
			JTextArea text = new JTextArea();
			String deckGunPrompt = "The result of the die roll was red: "+ redDice +  " black: " + blackDice + ". \nWould you like to reroll either dice?";
			
			text.setText(deckGunPrompt);
			text.setLineWrap(true);
			JPanel responsePanel = new JPanel();
			responsePanel.setLayout(new GridLayout(3,1));
			responsePanel.setPreferredSize(new Dimension(500,500));
			
			if(redReRoll) {
				JButton redButton = new JButton("Reroll Red Dice");
				redButton.setPreferredSize(new Dimension(40,40));
				redButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deckGunRequest.hide();
						deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
						if(rerollDice(1, a)) {
//							deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
						}
					}

				});
				responsePanel.add(redButton);
			}
			
			if(blackReRoll) {
				JButton blackButton = new JButton("Reroll Black Dice");
				blackButton.setPreferredSize(new Dimension(40,40));
				blackButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deckGunRequest.hide();
						deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
						if(rerollDice(2, a)) {
//							deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
						}
					}

				});
				responsePanel.add(blackButton);
			}
			
			JButton noButton = new JButton("No");
			noButton.setPreferredSize(new Dimension(75,75));
			noButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deckGunRequest.hide();
					deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
					if(rerollDice(0, a)) {
//						deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
					}
				}
			});
			responsePanel.add(noButton);
			gameTPanel.add(text);
			gameTPanel.add(responsePanel);
			
			deckGunRequest = gameT.getPopup(rightPanel, gameTPanel, 500, 50);
			
			deckGunRequest.show();
		}
		
		public void hideDeckGunPanel() {
			if(deckGunRequest != null) {
				deckGunRequest.hide();
			}
			
		}	
		
		public int getMyIndex() {
			return this.myIndex;
		}
		
		private boolean sendPlaceFFRequest(int [] coords) {
			return(clientManager.placeFFRequest(coords));
		}
		
		private boolean sendActionRequest(actions.Action a) {
			return(clientManager.ActionRequest(a));
		}
		
		
		public int listening(){
			return clientManager.listenForResponses();
		}
		
		public boolean sendEndTurnRequest() {
			return clientManager.endTurnRequest();
		}
		
		public boolean sendPlaceVehicleRequest(int direction, Vehicle type) {
			return clientManager.placeVehicleRequest(direction, type);
		}
		
		public boolean sendRideRequests(Vehicle type) {
			return clientManager.sendRideRequests(type);
		}
		
		private boolean sendSpecialitySelectionRequest(Speciality s, int i) {
			return clientManager.sendSpecialitySelectionRequest(s, i);
		}
		
		private boolean sendSelectionEndRequest() {
			return clientManager.sendSelectionEndRequest();
		}
		
		private boolean sendFireFighterSelectionRequest(token.Colour i) {
			return clientManager.fireFighterSelectionRequest(i);
		}
		
		private boolean sendRideResponse(boolean b, int i) {
			return clientManager.sendRideResponse(b, i);
		}
		
		private boolean fireDeckGun(actions.Action a) {
//			redDice = Math.random() * 6 + 1;
//			blackDice = Math.random() * 8 + 1;

			
			if(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getSpeciality() == Speciality.DRIVER) {
				redReRoll = true;
				blackReRoll = true;
			}
			
			
			if(!redReRoll && !blackReRoll) {
				sendActionRequest(a);
				return true;
			}
			
			showRideRequest();
			
			return true;
		}
		
		private boolean rerollDice(int i, actions.Action a) {
			int[] quadCoords = clientManager.getUsersGameState().getPlayingFirefighter().getCurrentPosition().getCoords();
			int xShift = 0;
			int yShift = 0;
			System.out.println("init: red die" + redDice + " black die " + blackDice);
			if(quadCoords[0] > 3) {
				if(quadCoords[1] > 4) {
					//quad 4
					System.out.println("quad4");
					xShift = 3;
					yShift = 4;
				} else {
					//quad 3
					xShift = 3;
					System.out.println("quad3");
				}
			} else {
				if(quadCoords[1] > 4) {
					//quad 2
					yShift = 4;
					System.out.println("quad2");
				} else {
					//quad 1
					System.out.println("quad1");
				}
			}
			switch(i) {
			case 0:
				redReRoll = false;
				blackReRoll = false;
				if(!redReRoll & !blackReRoll) {
					int[] update = {(int) redDice, (int) blackDice};
					a.setResult(update);
					sendActionRequest(a);
				}
				break;
			case 1:
				redDice = (int) (Math.random() * 3.0 + 1);
				redDice += xShift;
				redReRoll = false;
				if(!redReRoll & !blackReRoll) {
					int[] update = {(int) redDice, (int) blackDice};
					a.setResult(update);
					sendActionRequest(a);
				} else {
					showDeckGunRequest(a);
				}
				break;
			case 2:
				blackDice = (int) (Math.random() * 4.0 + 1);
				blackDice += yShift;
				blackReRoll = false;
				if(!redReRoll & !blackReRoll) {
					int[] update = {(int) redDice, (int) blackDice};
					a.setResult(update);
					sendActionRequest(a);
				} else {
					showDeckGunRequest(a);
				}
				
				break;
			}
			
			return true;
		}
		
		private boolean sendDodgeAnswer(Action a, int myIndex) {
			return clientManager.dodgeAnswer(a,myIndex);
			
		}
		
		private void sendRefreshRequest() {
			clientManager.sendRefreshRequest();
		}
		
		public void resetDodge() {
			dodged = new boolean[6];
		}
		
		public void resetRode() {
			rode = new boolean[6];
		}
}
