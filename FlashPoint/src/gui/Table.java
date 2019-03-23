package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.util.List;
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
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import actions.ActionList;
import client.ClientManager;
import game.GameState;
import tile.Tile;
import token.Colour;
import token.Firefighter;
import token.POI;
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
		private Popup advFire;
		private Popup gameTermination;
		private static boolean placing = true;
		private static boolean playing = true;
		private ClientManager clientManager;
		private Launcher launcher;
		private int myIndex = 7;
		
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
		
		public Table(GameState inputBoard, ClientManager updatedClientManager, Launcher launcher) {
			currentBoard = inputBoard;
			gameTiles = inputBoard.getMatTiles();
			this.clientManager = updatedClientManager;
			this.launcher = launcher;
			for(int i = 0; i<inputBoard.getFireFighterList().size(); i++) {
				Firefighter f = inputBoard.getFireFighterList().get(i);
				if(updatedClientManager.getUserName().equals(f.getOwner().getUserName())) {
					this.myIndex = i;
				}
			}
//			System.out.println("this is the my index: " + this.myIndex);
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
//			this.playing = playingchange;
//			this.placing = placingchange;
			this.gameTiles = newBoard.getMatTiles();
//			boardPanel.drawBoard(newBoard);
//			rightPanel.drawPanel(newBoard);
//			leftPanel.drawPanel(newBoard);
			this.boardPanel = new BoardPanel();
			this.rightPanel = new RightPanel(this.currentBoard);
			this.leftPanel = new LeftPanel(this.currentBoard);
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
			RightPanel(GameState updatedBoard){
				super(new GridLayout(2,1));
				setPreferredSize(RIGHT_PANEL_DIMENSION);
				currentBoard = updatedBoard;
				chatArea = new JTextArea();
				chatArea.setLineWrap(true);
				infoPanel = new InformationPanel(currentBoard);
				add(infoPanel);
				add(chatArea);
				validate();
			}
			public void drawPanel(GameState currentBoard2) {
				// TODO Auto-generated method stub
				removeAll();
				infoPanel.drawInfo(currentBoard);
				add(infoPanel);
				add(chatArea);
				revalidate();
				repaint();
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
					if(1 == 2 /*currentBoard.mode == advanced*/) {
						//playerInfo = playerInfo + "Specialty: "+ currentFF.getSpeciality.toString();
					}
					
					String inputString;
					//needs fixing
					String ffColour = currentFF.getColour().toString(currentFF.getColour());
					if(this.currentBoard.getActiveFireFighterIndex() == i) {
						inputString = "<html> <font size=\"8\", color='"+ffColour+"'><b>" + playerInfo + "</b></font></html>";
					} 
					else {
						inputString = "<html> <font size =\"5\", color='"+ ffColour + "'>" + playerInfo + "</font></html>";
					}
					
					add(new JLabel(inputString));
					
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
				setLayout(new GridLayout(2,2));
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
			}
		}
		
		private class RevealPoiPanel extends JPanel{
			GameState currentBoard;
			RevealPoiPanel(GameState updatedBoard){
				super(new BorderLayout());
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
							if(placing) {
								if(connectedTile.getCoords()[0] == 0 || connectedTile.getCoords()[1] == 0 || connectedTile.getCoords()[0] == 7 || connectedTile.getCoords()[1] == 9) {
									showPopUpMenuPlace(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
								}
							}else {
								if(playing) {
									check = currentBoard.getFireFighterList().get(currentBoard.getActiveFireFighterIndex()).getCurrentPosition().getCoords();
									if(connectedTile.getCoords()[0] == check[0] && connectedTile.getCoords()[1] == check[1]) {
										showPopUpMenuCurrent(e.getComponent(), e.getX(), e.getY(), currentBoard);
									} else {
										showPopUpMenuOther(e.getComponent(), e.getX(), e.getY(), currentBoard);
									}
								} 
								else {
									showPopUpMenuWaiting(e.getComponent(), e.getX(), e.getY(), currentBoard);
								}
							}
						} else if(SwingUtilities.isLeftMouseButton(e)) {
							showPopUpMenuInfo(e.getComponent(), e.getX(), e.getY(), currentBoard, coords);
						}
						
					}

					@Override
					public void mousePressed(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseEntered(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				validate();
			}
			
			public void drawTile(GameState currentBoard) {
				// TODO Auto-generated method stub
				
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
					Firefighter currentFF = clientManager.getUsersGameState().getFireFighterList().get(myIndex);
					Tile currentPos = currentFF.getCurrentPosition();
					Firefighter check;
					if(this.connectedTile == currentPos) {
						check = currentFF;
					} else {
						check = this.connectedTile.getFirefighterList().get(0);
					}
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
		        
			    for(actions.Action a: currentActions) {
			    	ActionList actionTitle = a.getTitle();
			    	String builder = "";
			    	JMenuItem newAction;
			    	int APCost = a.getCost();
			    	if(actionTitle == ActionList.Chop) {
			    		if(APCost == 2) {
			    			if(a.getDirection() == 0) {
				    			builder = "Left, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
										}
				    				}
				    			});
				    	        twiceMenu.add(newAction);
				    	        twoChopCheck = true;
				    			
				    		} 
			    		}
			    		
			    	} else if(actionTitle == ActionList.Extinguish) {
			    		if(APCost == 1) {
			    			if(a.getDirection() == 0) {
			    				builder = "Left, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//				    					a.perform(currentBoard);
//				    					gameTest.repainter();
				    					if(sendActionRequest(a)) {
											System.out.println("this is the print that board is refreshing");
//											clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
											refresh(clientManager.getUsersGameState());
											launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
										}
				    				}
				    			});
				    	        completelyMenu.add(newAction);
				    	        completeCheck = true;
				    		}
			    		}
			    	} else if(actionTitle == ActionList.Drive) {
			    		System.out.println(a.getClass().toString());
			    	} else if(actionTitle == ActionList.Finish) {
			    		builder = "End Turn";
		    			newAction = new JMenuItem(builder);
		    	        newAction.addActionListener(new ActionListener() {
		    				@Override
		    				public void actionPerformed(ActionEvent e) {
		    					if(sendEndTurnRequest()){
		    						System.out.println("this is the print that board is refreshing");
		    						launcher.showAdvanceFireString(clientManager.getUsersGameState().getAdvFireString());
		    						if(clientManager.getUsersGameState().isGameTerminated()) {
		    							launcher.showGameTermination();
		    							//refresh(clientManager.getUsersGameState());
		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						} else if(clientManager.getUsersGameState().isGameWon()) {
		    							launcher.showGameTermination();
		    							//refresh(clientManager.getUsersGameState());
		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						} else {
		    							refresh(clientManager.getUsersGameState());
		    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
		    						}
		    					}
		    				}
		    			});
		    	        finishMenu.add(newAction);
			    	} else if(actionTitle == ActionList.FireGun) {
			    		System.out.println(a.getClass().toString());
			    	} else if(actionTitle == ActionList.Handle) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
//										clientThread t1 = new clientThread(launcher);
//										t1.start();
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
										clientThread t1 = new clientThread(launcher);
										t1.begin();
//										t1.run();
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
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
//			    					a.perform(currentBoard);
//			    					gameTest.repainter();
			    					if(sendActionRequest(a)) {
										System.out.println("this is the print that board is refreshing");
//										clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
										refresh(clientManager.getUsersGameState());
										launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
									}
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    	        moveWVCheck = true;
			    			
			    		} 
			    	}
					
				}
		         
		        JMenuItem exitMenu = new JMenuItem("exit");
		        exitMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
//						System.exit(0);
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
//						gameTest.placeFF(currentBoard.returnTile(coords[0],coords[1]));
						if(sendPlaceFFRequest(coords)) {
							System.out.println("this is the print that board is refreshing");
//							clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
							System.out.println("helo!      " + clientManager.getUsersGameState().returnTile(0, 0).getFirefighterList().size());
							refresh(clientManager.getUsersGameState());
							launcher.repaint(false, myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
							System.out.println("finished repainting");
						}
//						clientManager.getUsersGameState().placeFireFighter(clientManager.getUsersGameState().getPlayingFirefighter(), clientManager.getUsersGameState().returnTile(3,0));
//						refresh(clientManager.getUsersGameState());
//						launcher.repaint();
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
    						launcher.showAdvanceFireString(clientManager.getUsersGameState().getAdvFireString());
    						if(clientManager.getUsersGameState().isGameTerminated()) {
    							launcher.showGameTermination();
    							refresh(clientManager.getUsersGameState());
    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
    						} else if(clientManager.getUsersGameState().isGameWon()) {
    							launcher.showGameTermination();
    							refresh(clientManager.getUsersGameState());
    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
    						} else {
    							refresh(clientManager.getUsersGameState());
    							launcher.repaint(false,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
    						}
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
			public void showPopUpMenuWaiting(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard) {
				JPopupMenu popupMenu = new JPopupMenu();
		        
		        JMenu placeAmbulance = new JMenu("Place Ambulance");
		        JMenu placeEngine = new JMenu("Place Engine");
		        
		        JMenuItem ambulanceLeft = new JMenuItem("Left");
		        ambulanceLeft.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(0, Vehicle.Ambulance)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem ambulanceTop = new JMenuItem("Top");
		        ambulanceTop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(1, Vehicle.Ambulance)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem ambulanceRight = new JMenuItem("Right");
		        ambulanceRight.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(2, Vehicle.Ambulance)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem ambulanceBottom = new JMenuItem("Bottom");
		        ambulanceBottom.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(3, Vehicle.Ambulance)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineLeft = new JMenuItem("Left");
		        engineLeft.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(0, Vehicle.Engine)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineTop = new JMenuItem("Top");
		        engineTop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(1, Vehicle.Engine)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        JMenuItem engineRight = new JMenuItem("Right");
		        engineRight.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(2, Vehicle.Engine)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
						}
					}
				});
		        
		        JMenuItem engineBottom = new JMenuItem("Bottom");
		        engineBottom.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(sendPlaceVehicleRequest(3, Vehicle.Engine)) {
							refresh(clientManager.getUsersGameState());
							launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
						
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
			
			//generates the popUp menu for tiles that don't contain current FF
				public void showPopUpMenuInfo(/*GameState currentBoard,*/ Component component, int x, int y, GameState currentBoard, int[] coords) {
					JPopupMenu popupMenu = new JPopupMenu();
					JMenu poiMenu = new JMenu("POIs");
					JMenu firefighterMenu = new JMenu("Firefighters");
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
						builder = f.getOwner().getUserName() + ": " + f.getColour().toString() + " fireman.";
						info = new JMenuItem(builder);
						firefighterMenu.add(info);
					}
					
					

					
			        popupMenu.add(poiMenu);
			        popupMenu.addSeparator();
			        popupMenu.add(firefighterMenu);
			        if(myIndex != clientManager.getUsersGameState().getActiveFireFighterIndex()) {
			        	JMenuItem waiting = new JMenuItem("waiting");
				        waiting.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								
								if(listening() == 1) {
									refresh(clientManager.getUsersGameState());
									launcher.repaint(clientManager.getUsersGameState().getFireFighterList().get(myIndex).getCurrentPosition() == null,myIndex == clientManager.getUsersGameState().getActiveFireFighterIndex());
								}
								
								
							}
						});
				        popupMenu.addSeparator();
				        popupMenu.add(waiting);
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
			
			if(clientManager.getUsersGameState().isGameTerminated()) {
				if(clientManager.getUsersGameState().getDamageCounter() >= 24) {
					JLabel popupMsg = new JLabel("Game over.\n The house has collapsed.");
					gameTPanel.setPreferredSize(new Dimension(300,300));
					gameTPanel.setBackground(tileColorRed);
					Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
					gameTPanel.setBorder(blackline);
					gameTPanel.add(popupMsg);
					gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
					
				} else if(clientManager.getUsersGameState().getLostVictimsList().size() >= 4) {
					JLabel popupMsg = new JLabel("Game over.\n 4 victims were lost.");
					gameTPanel.setPreferredSize(new Dimension(300,300));
					gameTPanel.setBackground(tileColorRed);
					Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
					gameTPanel.setBorder(blackline);
					gameTPanel.add(popupMsg);
					gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
				}
			} else if(clientManager.getUsersGameState().isGameWon()) {
				JLabel popupMsg = new JLabel("Game Won.\n 7 victims were saved in time.");
				gameTPanel.setPreferredSize(new Dimension(300,300));
				gameTPanel.setBackground(tileColorGreen);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,15);
				gameTPanel.setBorder(blackline);
				gameTPanel.add(popupMsg);
				gameTermination = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
			}
			
			gameTermination.show();
		}
		
		public void showAdvanceFireString(String message) {
			advFire = null;
			PopupFactory gameT = new PopupFactory();
			JPanel gameTPanel = new JPanel(new BorderLayout());
			JTextArea text = new JTextArea();
			text.append(message);
			text.setLineWrap(true);
			
			JButton okButton = new JButton("ok");
			okButton.setPreferredSize(new Dimension(20,20));
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					advFire.hide();
					advFire = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
				}
			});
			gameTPanel.setPreferredSize(new Dimension(300,400));
			gameTPanel.setBackground(tileColorWhite);
			Border blackline = BorderFactory.createLineBorder(tileColorBlack,10);
			gameTPanel.setBorder(blackline);
			gameTPanel.add(text, BorderLayout.NORTH);
			gameTPanel.add(okButton, BorderLayout.SOUTH);
			advFire = gameT.getPopup(rightPanel, gameTPanel, 1140, 50);
			
			advFire.show();
		}
		
		public void hideAdvPanel() {
			if(advFire != null) {
				advFire.hide();
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
}
