package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import actions.ActionList;
import game.GameState;
import tile.Tile;
import token.Colour;
import token.Firefighter;
import token.POI;
import token.Vehicle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class LocalizedTable {

		private final JFrame gameFrame;
		private final int NUM_TILES = 80;		// 8 x 10 (rows x columns)
		private BoardPanel boardPanel;
		private RightPanel rightPanel;
		private LeftPanel leftPanel;
		private final GameState currentBoard;
		private final Tile[][] gameTiles;
		
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
		private static boolean placing = true;
		
		public LocalizedTable(GameState inputBoard) {
			this.currentBoard = inputBoard;
			this.gameTiles = inputBoard.getMatTiles();
			this.gameFrame = new JFrame("FlashPoint");
			this.gameFrame.setLayout(new BorderLayout());
			final JMenuBar tableMenuBar = new JMenuBar();
			populateMenuBar(tableMenuBar);
			//this.gameFrame.setJMenuBar(tableMenuBar);
			this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
			this.boardPanel = new BoardPanel();
			this.rightPanel = new RightPanel(this.currentBoard);
			this.leftPanel = new LeftPanel(this.currentBoard);
			this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
			this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
			this.gameFrame.add(this.leftPanel,BorderLayout.WEST);
			this.gameFrame.setVisible(true);
		}
		
		public void refresh() {
			this.boardPanel = new BoardPanel();
			this.rightPanel = new RightPanel(this.currentBoard);
			this.leftPanel = new LeftPanel(this.currentBoard);
			this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
			this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
			this.gameFrame.add(this.leftPanel,BorderLayout.WEST);
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
					
					System.exit(0);
				}
			});
			
			fileMenu.add(exitMenuItem);
			
			return fileMenu;
		}
		
		private class RightPanel extends JPanel {
			GameState currentBoard;
			InformationPanel infoPanel;
			RightPanel(GameState updatedBoard){
				super(new GridLayout(2,1));
				setPreferredSize(RIGHT_PANEL_DIMENSION);
				currentBoard = updatedBoard;
				JTextArea chatArea = new JTextArea();
				chatArea.setLineWrap(true);
				JTextField chatField = new JTextField();
				
				infoPanel = new InformationPanel(currentBoard);
				add(infoPanel);
				
				add(chatArea);
				
				
				validate();
			}
		}
		
		private class InformationPanel extends JPanel{
			GameState currentBoard;
			InformationPanel(GameState updatedBoard){
				super(new GridLayout());
				setLayout(new GridLayout(6,1));
				this.currentBoard = updatedBoard;
				for(int i = 0; i<this.currentBoard.getFireFighterList().size(); i++) {
					Firefighter currentFF = this.currentBoard.getFireFighterList().get(i);
					String playerInfo = (currentFF.getOwner().getUserName() + "  AP: " + currentFF.getAP() /*+ "  Saved Ap: " + currentFF.getSavedAP()*/);
					String inputString;
					//needs fixing
					String ffColour = currentFF.getColour().toString(currentFF.getColour());
					if(this.currentBoard.getActiveFireFighterIndex() == i) {
						inputString = "<html> <font size=\"5\", color='"+ffColour+"'>" + playerInfo + "</font></html>";
					} 
//					else if(this.currentBoard.getActiveFireFighterIndex()+1 == i) {
//						inputString = "<html> <font color='"+ ffColour + "'>" + playerInfo + "</font></html>";
//					}
					else {
						inputString = "<html> <font color='"+ ffColour + "'>" + playerInfo + "</font></html>";
					}
					
					add(new JLabel(inputString));
					
//					JTextArea nextPlayer = new JTextArea(playerInfo);
//					nextPlayer.setEditable(false);
//					//can be used to show which player blongs to this client
//					if(this.currentBoard.getActiveFireFighterIndex() == i) {
//						nextPlayer.setFont(new Font("Serif", Font.ITALIC, 16));	
//					} 
//					if(this.currentBoard.getActiveFireFighterIndex()+1 == i) {
//						
//					}
//					
//					add(nextPlayer);
					
				}
				String inputString = "<html> <font size=\"5\"> Current Wall Damage: " + currentBoard.getDamageCounter() + "</font></html>";
				add(new JLabel(inputString));
			}
		}
		
		private class LeftPanel extends JPanel {
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
		}
		
		private class LostPoiPanel extends JPanel{
			GameState currentBoard;
			LostPoiPanel(GameState updatedBoard){
				super(new BorderLayout());
				setLayout(new GridLayout(2,2));
				this.currentBoard = updatedBoard;
				setBackground(tileColorRed);
				setPreferredSize(NORTH_LEFT_PANEL_DIMENSION);
				Border blackline = BorderFactory.createLineBorder(tileColorBlack,5);
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
				setBackground(tileColorGreen);
				setPreferredSize(SOUTH_LEFT_PANEL_DIMENSION);
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
				setBackground(tileColorGrey);
				currentBoard = updatedBoard;
				setPreferredSize(CENTER_LEFT_PANEL_DIMENSION);
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
		
		private class BoardPanel extends JPanel {
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
		}
		
		private class SuperTilePanel extends JPanel {
			private final int tileId;
			private final Tile connectedTile;
			private final int[] coords;
			SuperTilePanel(final BoardPanel boardPanel, final int tileId){
				super (new BorderLayout());
				setPreferredSize(SUPERTILE_PANEL_DIMENSION);
				coords = calculateTileCoords(tileId);
				connectedTile = gameTiles[coords[0]][coords[1]];
				final TilePanel tilePanel = new TilePanel(boardPanel, tileId);
				add(tilePanel, BorderLayout.CENTER);
				this.tileId = tileId;
				assignTileWall();
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
								check = currentBoard.getFireFighterList().get(currentBoard.getActiveFireFighterIndex()).getCurrentPosition().getCoords();
								if(connectedTile.getCoords()[0] == check[0] && connectedTile.getCoords()[1] == check[1]) {
									showPopUpMenuCurrent(e.getComponent(), e.getX(), e.getY(), currentBoard);
								} else {
									showPopUpMenuOther(e.getComponent(), e.getX(), e.getY(), currentBoard);
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
					Firefighter check = this.connectedTile.getFirefighterList().get(0);
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
				
				JMenu moveMenu = new JMenu("Move");
			    JMenu extinguishMenu = new JMenu("Extinguish"); 
			    JMenu toSmokeMenu = new JMenu("To Smoke/Smoke");
			    JMenu completelyMenu = new JMenu("Completely");
			    JMenu chopMenu = new JMenu("Chop");
			    JMenu handleMenu = new JMenu("Toggle Door");
			    JMenu carryMenu = new JMenu("Carry");
			    JMenu finishMenu = new JMenu("finish");
			    JMenu moveWithVictimMenu = new JMenu("Move With Victim");
		        
			    for(actions.Action a: currentActions) {
			    	ActionList actionTitle = a.getTitle();
			    	String builder = "";
			    	JMenuItem newAction;
			    	int APCost = a.getCost();
			    	if(actionTitle == ActionList.Chop) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
//			    					refresh();
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        chopMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
//			    					refresh();
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        chopMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
//			    					refresh();
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        chopMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
//			    					refresh();
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        chopMenu.add(newAction);
			    			
			    		} 
			    	} else if(actionTitle == ActionList.Extinguish) {
			    		if(APCost == 1) {
			    			if(a.getDirection() == 0) {
			    				builder = "Left, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        toSmokeMenu.add(newAction);
				    		} else if(a.getDirection() == 1) {
				    			builder = "Up, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        toSmokeMenu.add(newAction);
				    			
				    		} else if(a.getDirection() == 2) {
				    			builder = "Right, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        toSmokeMenu.add(newAction);
				    		} else if(a.getDirection() == 3) {
				    			builder = "Down, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        toSmokeMenu.add(newAction);
				    		} else if(a.getDirection() == -1) {
				    			builder = "Current Location, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        toSmokeMenu.add(newAction);
				    		}
			    		} else if(APCost == 2) {
			    			if(a.getDirection() == 0) {
			    				builder = "Left, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        completelyMenu.add(newAction);
				    		} else if(a.getDirection() == 1) {
				    			builder = "Up, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        completelyMenu.add(newAction);
				    			
				    		} else if(a.getDirection() == 2) {
				    			builder = "Right, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        completelyMenu.add(newAction);
				    		} else if(a.getDirection() == 3) {
				    			builder = "Down, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        completelyMenu.add(newAction);
				    		} else if(a.getDirection() == -1) {
				    			builder = "Current Location, APC: " + APCost;
				    			newAction = new JMenuItem(builder);
				    	        newAction.addActionListener(new ActionListener() {
				    				@Override
				    				public void actionPerformed(ActionEvent e) {
				    					a.perform(currentBoard);
				    					gameTest.repainter();
				    					System.out.println(a.getClass());
				    				}
				    			});
				    	        completelyMenu.add(newAction);
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
//		    					a.perform(currentBoard);
		    					gameTest.nextTurn();
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
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        handleMenu.add(newAction);
			    		} 
			    	} else if(actionTitle == ActionList.Move) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveMenu.add(newAction);
			    			
			    		} 
			    	} else if(actionTitle == ActionList.MoveWithVictim) {
			    		if(a.getDirection() == 0) {
			    			builder = "Left, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 1) {
			    			builder = "Up, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 2) {
			    			builder = "Right, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    			
			    		} else if(a.getDirection() == 3) {
			    			builder = "Down, APC: " + APCost;
			    			newAction = new JMenuItem(builder);
			    	        newAction.addActionListener(new ActionListener() {
			    				@Override
			    				public void actionPerformed(ActionEvent e) {
			    					a.perform(currentBoard);
			    					gameTest.repainter();
			    					System.out.println(a.getClass());
			    				}
			    			});
			    	        moveWithVictimMenu.add(newAction);
			    			
			    		} 
			    	}
					
				}
		         
		        JMenuItem exitMenu = new JMenuItem("exit");
		        exitMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						System.exit(0);
					}
				});
		        
		        
		        extinguishMenu.add(toSmokeMenu);
		        extinguishMenu.addSeparator();
		        extinguishMenu.add(completelyMenu);
		        
		        popupMenu.add(handleMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(moveMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(moveWithVictimMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(carryMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(chopMenu);
		        popupMenu.addSeparator();
		        popupMenu.add(extinguishMenu);
		        popupMenu.addSeparator();
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
						gameTest.placeFF(currentBoard.returnTile(coords[0],coords[1]));
					}
				});
		         
		        JMenuItem fileMenu = new JMenuItem("exit");
		        fileMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						System.exit(0);
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
						
						gameTest.nextTurn();
					}
				});
		         
		        JMenuItem fileMenu = new JMenuItem("exit");
		        fileMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						System.exit(0);
					}
				});
		        popupMenu.add(endTurn);
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
					
			        JMenuItem fileMenu = new JMenuItem("exit");
			        fileMenu.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							
							System.exit(0);
						}
					});
			        popupMenu.add(poiMenu);
			        popupMenu.addSeparator();
			        popupMenu.add(firefighterMenu);
			        
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
		
		
		
}
