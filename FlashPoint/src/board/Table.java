package board;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

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

public class Table {

		private final JFrame gameFrame;
		private final int NUM_TILES = 80;		// 8 x 10 (rows x columns)
		private final BoardPanel boardPanel;
		private final RightPanel rightPanel;
		private final LeftPanel leftPanel;
		private final int[] fireLayout = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,2,1,0,0,0,0,1,1,0,1,0,1,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		private final int[] pieceLayout = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,0,0,0,2,0,1,0,0,2,0,0,2,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		
		
		
		private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(2200,1200);
		private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(1500,1200);
		private final static Dimension RIGHT_PANEL_DIMENSION = new Dimension(500,1200);
		private final static Dimension LEFT_PANEL_DIMENSION = new Dimension(200,1200);
		private final static Dimension SUPERTILE_PANEL_DIMENSION = new Dimension(40,40);
		private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
		private static String defaultImagesPath = "img/";
		
		private Color tileColorGrey = Color.decode("#B0B0B0");
		private Color tileColorRed = Color.decode("#FF0000");
		private static Color tileColorWhite = Color.decode("#FFFFFF");
		private Color tileColorBlack = Color.decode("#000000");
		private Color tileColorGreen = Color.decode("#00900B");
		
		public Table() {
			this.gameFrame = new JFrame("FlashPoint");
			this.gameFrame.setLayout(new BorderLayout());
			final JMenuBar tableMenuBar = new JMenuBar();
			populateMenuBar(tableMenuBar);
			//this.gameFrame.setJMenuBar(tableMenuBar);
			this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
			this.boardPanel = new BoardPanel();
			this.rightPanel = new RightPanel();
			this.leftPanel = new LeftPanel();
			this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
			this.gameFrame.add(this.rightPanel, BorderLayout.EAST);
			this.gameFrame.add(this.leftPanel,BorderLayout.WEST);
			this.gameFrame.setVisible(true);
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
			RightPanel(){
				super(new BorderLayout());
				setPreferredSize(RIGHT_PANEL_DIMENSION);
				JTextArea textArea = new JTextArea();
				textArea.setLineWrap(true);
				JTextField textField = new JTextField();
				
				try {
					final BufferedImage leftImage = ImageIO.read(new File(defaultImagesPath + "RIGHT_TOP.gif"));
					add(new JLabel(new ImageIcon(leftImage)), BorderLayout.NORTH);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				add(textArea, BorderLayout.SOUTH);
				
				
				validate();
			}
		}
		
		private class LeftPanel extends JPanel {
			LeftPanel(){
				setPreferredSize(LEFT_PANEL_DIMENSION);
				
				try {
					final BufferedImage leftImage = ImageIO.read(new File(defaultImagesPath + "LEFT_PANEL.gif"));
					add(new JLabel(new ImageIcon(leftImage)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				validate();
			}
		}
		
		private class BoardPanel extends JPanel {
			final List<SuperTilePanel> boardTiles;
			
			BoardPanel(){
				super(new GridLayout(8,10));
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
			SuperTilePanel(final BoardPanel boardPanel, final int tileId){
				super (new BorderLayout());
				setPreferredSize(SUPERTILE_PANEL_DIMENSION);
				final TilePanel tilePanel = new TilePanel(boardPanel, tileId);
				add(tilePanel, BorderLayout.CENTER);
				this.tileId = tileId;
				assingTileWall();
			}
			private void assingTileWall() {
				Border blackline = BorderFactory.createLineBorder(tileColorBlack);
				
				final JPanel wall = new JPanel();
				wall.setBackground(tileColorGrey);
//				wall.setBorder(blackline);
				
				final JPanel wall2 = new JPanel();
				wall2.setBackground(tileColorGrey);
//				wall2.setBorder(blackline);
//				
//				final JPanel wall3 = new JPanel();
//				wall3.setBackground(tileColorGrey);
////				wall3.setBorder(blackline);
//				
//				final JPanel wall4 = new JPanel();
//				wall4.setBackground(tileColorGrey);
////				wall4.setBorder(blackline);
				
				if(this.tileId/10 == 0 || this.tileId/10 == 7 || this.tileId%10 == 9 || this.tileId%10 == 0) {
					if(this.tileId/10 == 0 && (this.tileId%10 != 9 && this.tileId%10 != 0) ) {
						add(wall, BorderLayout.SOUTH);
					} else if(this.tileId/10 == 7 && (this.tileId%10 != 9 && this.tileId%10 != 0)) {
						add(wall, BorderLayout.NORTH);
					}
					if(this.tileId/10 != 0 && this.tileId/10 != 7 && (this.tileId%10 == 9 && this.tileId%10 != 7) ) {
						add(wall, BorderLayout.WEST);
					} else if(this.tileId/10 != 0 && this.tileId/10 != 7 && this.tileId%10 == 0 && this.tileId%10 != 7) {
						add(wall, BorderLayout.EAST);
					}
				} else if(this.tileId/10 == 1 || this.tileId/10 == 6 || this.tileId%10 == 8 || this.tileId%10 == 1){
					if(this.tileId/10 == 1) {
						add(wall, BorderLayout.NORTH);
					} else if(this.tileId/10 == 6) {
						add(wall, BorderLayout.SOUTH);
					}
					if(this.tileId%10 == 1) {
						add(wall2, BorderLayout.WEST);
					} else if(this.tileId%10 == 8) {
						add(wall2, BorderLayout.EAST);
					}
				}else if(this.tileId == 45) {
					try {
						final BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORBOT.gif"));
						final BufferedImage imageWallDAM = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DAMAGED.gif"));
						final BufferedImage imageWallDES = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DESTROYED.gif"));
						add(new JLabel(new ImageIcon(imageWall)), BorderLayout.NORTH);
						add(new JLabel(new ImageIcon(imageWallDES)), BorderLayout.SOUTH);
						add(new JLabel(new ImageIcon(imageWallDAM)), BorderLayout.EAST);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(this.tileId == 46) {
					try {
						BufferedImage imageWallDAM = ImageIO.read(new File(defaultImagesPath + "VERT_WALL_DAMAGED.gif"));
						add(new JLabel(new ImageIcon(imageWallDAM)), BorderLayout.WEST);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(this.tileId == 55) {
					try {
						BufferedImage imageWallDES = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DESTROYED.gif"));
						add(new JLabel(new ImageIcon(imageWallDES)), BorderLayout.NORTH);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(this.tileId == 35) {
					try {
						BufferedImage imageWall = ImageIO.read(new File(defaultImagesPath + "HORT_WALL_DOORTOP.gif"));
						add(new JLabel(new ImageIcon(imageWall)), BorderLayout.SOUTH);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else {
//					add(wall, BorderLayout.NORTH);
//					add(wall2, BorderLayout.EAST);
//					add(wall3, BorderLayout.SOUTH);
//					add(wall4,BorderLayout.WEST);
				}
				
			}
		}
		
		private class TilePanel extends JPanel {
			
			private final int tileId;
			//final List<TokenPanel> tokenTiles;
			
			TilePanel(final BoardPanel boardPanel, final int tileId) {
				super (new GridLayout(2,2));
				this.tileId = tileId;
				setPreferredSize(TILE_PANEL_DIMENSION);
				assignTileColour();
				Border blackline = BorderFactory.createLineBorder(tileColorBlack);
				setBorder(blackline);
				assignTilePieceIcon();
				
				addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(final MouseEvent e) {
						if(SwingUtilities.isRightMouseButton(e)) {
							//brings up menu
							showPopUpMenu(e.getComponent(), e.getX(), e.getY());
						} else if(SwingUtilities.isLeftMouseButton(e)) {
							System.out.println(tileId);
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
			
			private void assignTilePieceIcon() {
				this.removeAll();
				if(this.tileId/10 == 0 || this.tileId/10 == 7 || this.tileId%10 == 9 || this.tileId%10 == 0) {

				} else {
					if(pieceLayout[this.tileId] == 1) {
						try {
							final BufferedImage image = ImageIO.read(new File(defaultImagesPath + "GREEN_FIREMAN.gif"));
							add(new JLabel());
							add(new JLabel(new ImageIcon(image)));
							add(new JLabel());
							add(new JLabel());	
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (pieceLayout[this.tileId] == 2) {
						try {
							final BufferedImage image2 = ImageIO.read(new File(defaultImagesPath + "VICTIM.gif"));
							add(new JLabel(new ImageIcon(image2)));	
							add(new JLabel());
							add(new JLabel());	
							add(new JLabel());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (pieceLayout[this.tileId] == 3) {
						try {
							final BufferedImage image = ImageIO.read(new File(defaultImagesPath + "BLACK_FIREMAN.gif"));
							final BufferedImage image2 = ImageIO.read(new File(defaultImagesPath + "POI.gif"));
							add(new JLabel(new ImageIcon(image2)));	
							add(new JLabel(new ImageIcon(image)));
							add(new JLabel());
							add(new JLabel());	
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						
					}
				}
				
//				if(board.getTile(this.tileId).isTileOccupied()) {
//					String pieceIconPath = "";
//					try {
//						final BufferedImage image = ImageIO.read(new File());
//						add(new JLabel(new ImageIcon(image)));	
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			
			}

			private void assignTileColour() {
				if(fireLayout[this.tileId] == 1) {
					setBackground(tileColorRed);
				}else if(fireLayout[this.tileId] == 2) {
					setBackground(tileColorGrey);
				}else {
					if(this.tileId/10 == 0 || this.tileId/10 == 7 || this.tileId%10 == 9 || this.tileId%10 == 0) {
						setBackground(tileColorGreen);
					} else {
						setBackground(tileColorWhite);
					}
				}				
				
			}
		}
		
		public static void showPopUpMenu(Component component, int x, int y) {
			JPopupMenu popupMenu = new JPopupMenu();
			 JMenu moveMenu = new JMenu("move");
		     JMenu editMenu = new JMenu("extinguish"); 
			
			JMenuItem moveleft = new JMenuItem("move left");
			moveleft.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.out.println("move left");
				}
			});
			
	        JMenuItem movedown = new JMenuItem("move down");
	        movedown.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.out.println("move down");
				}
			});
	        
	        JMenuItem moveright = new JMenuItem("move right");
	        moveright.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.out.println("move right");
				}
			});
	        
	        JMenuItem pasteMenuItem = new JMenuItem("chop walls");
	        pasteMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.out.println("chop walls");
				}
			});
	         
	        JMenuItem fileMenu = new JMenuItem("exit");
	        fileMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.exit(0);
				}
			});
	        
	        JMenuItem findMenuItem = new JMenuItem("put out fire");
	        findMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					component.setBackground(tileColorWhite);
					System.out.println("put out fire");
				}
			});
	        
	        editMenu.add(findMenuItem);
	        moveMenu.add(moveright);
	        moveMenu.add(movedown);
	        moveMenu.add(moveleft);
	        popupMenu.add(pasteMenuItem);
	        popupMenu.addSeparator();
	        popupMenu.add(moveMenu);
	        popupMenu.addSeparator();
	        popupMenu.add(editMenu);
	        popupMenu.addSeparator();
	        popupMenu.add(fileMenu);
	        
	        popupMenu.show(component, x, y);		// very important
		}
		
}
