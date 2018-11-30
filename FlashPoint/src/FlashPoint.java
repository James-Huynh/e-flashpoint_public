import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Component;
import java.util.Timer;
import java.util.TimerTask;

public class FlashPoint {

	private JFrame frame;
	private JTextField textField;
	private JButton button4;
	private JLabel button4_1;
	private JLabel button5;
	private JLabel button6;
	private static JLabel button7;
	private JLabel button8;
	private JLabel button9;
	private JLabel button10;
	private JLabel button11;
	private JLabel button12;
	private JLabel button13;
	private JLabel button14;
	private static JLabel button15;
	private static JLabel button17;
	private static JLabel button16;
	private static JLabel button25;
	private static JLabel button27;
	private static JLabel button26;
	private static int num=0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					FlashPoint window = new FlashPoint();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the application.
	 */
	public FlashPoint() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(150, 150, 1298, 812);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//MAKING FRAME OF GAME BOARD!!!!!!!!!
		
		JLabel statsBox, gameLogBox;
		
		
		//VARIALBES FOR MAKING CHAT STSTEM!!!!!!!!!!!!!!!!1
		
		JPanel placeFireFighter = new JPanel();
		placeFireFighter.setVisible(true);
		placeFireFighter.setBounds(400, 240, 380, 256);
		frame.getContentPane().add(placeFireFighter);
		
		JLabel label = new JLabel("");
		Image img22 = new ImageIcon(this.getClass().getResource("/placeFireFighterPopup.jpg")).getImage();
		label.setIcon(new ImageIcon(img22));
		
		
		GroupLayout gl_placeFireFighter = new GroupLayout(placeFireFighter);
		gl_placeFireFighter.setHorizontalGroup(
			gl_placeFireFighter.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_placeFireFighter.createSequentialGroup()
					.addComponent(label, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
));					

		gl_placeFireFighter.setVerticalGroup(
			gl_placeFireFighter.createParallelGroup(Alignment.LEADING)
				.addComponent(label, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_placeFireFighter.createSequentialGroup()
					.addGap(218)
					
					.addContainerGap())
		);
		placeFireFighter.setLayout(gl_placeFireFighter);
		
		placeFireFighter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
               placeFireFighter.setVisible(false);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                // 鼠标按下
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                // 鼠标释放

                // 如果是鼠标右键，则显示弹出菜单
                
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入组件区域
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标离开组件区域
            }
        });
		
		
		
		JPanel chatBox = new JPanel();
		chatBox.setVisible(false);
		
		//gameboard cells
				JLabel board = new JLabel();
				
				
				board.setBorder(null);
				board.setBounds(105, 24, 892, 713);
				frame.getContentPane().add(board);
				board.setLayout(new GridLayout(8,10));
				button4_1=new JLabel("");	
				button5=new JLabel("");
				button6=new JLabel("");
				button7=new JLabel("");
				Image img20 = new ImageIcon(this.getClass().getResource("/JUNHA_TOKEN.jpg")).getImage();
				button8=new JLabel("");
				button9=new JLabel("");
				button10=new JLabel("");
				button11=new JLabel("");
				button12=new JLabel("");
				button13=new JLabel("");
				button14=new JLabel("");
				button15=new JLabel("");
				button15.setIcon(new ImageIcon(img20));
				button17=new JLabel("");
				button17.setIcon(new ImageIcon(img20));
				button16=new JLabel("");
				button16.setIcon(new ImageIcon(img20));
				button27=new JLabel("");
				Image img21 = new ImageIcon(this.getClass().getResource("/deleteFireMarker.jpg")).getImage();
				button27.setIcon(new ImageIcon(img21));
				button26=new JLabel("");
				button26.setIcon(new ImageIcon(img20));
				button25=new JLabel("");
				button25.setIcon(new ImageIcon(img20));
			
				
				
				JLabel button18=new JLabel("");
				JLabel button19=new JLabel("");
				JLabel button20=new JLabel("");
				JLabel button21=new JLabel("");
				JLabel button22=new JLabel("");
				JLabel button23=new JLabel("");
				JLabel button24=new JLabel("");
				JLabel button28=new JLabel("");
				JLabel button29=new JLabel("");
				JLabel button30=new JLabel("");
				JLabel button31=new JLabel("");
				JLabel button32=new JLabel("");
				JLabel button33=new JLabel("");
				JLabel button34=new JLabel("");
				JLabel button35=new JLabel("");
				JLabel button36=new JLabel("");
				JLabel button37=new JLabel("");
				JLabel button38=new JLabel("");
				
					JLabel button39=new JLabel("");
					JLabel button40=new JLabel("");
					JLabel button41=new JLabel("");
					JLabel button42=new JLabel("");
					JLabel button43=new JLabel("");
					JLabel button44=new JLabel("");
					JLabel button45=new JLabel("");
					JLabel button46=new JLabel("");
					JLabel button47=new JLabel("");
					JLabel button48=new JLabel("");
					JLabel button49=new JLabel("");
					JLabel button50=new JLabel("");
					JLabel button51=new JLabel("");
					JLabel button52=new JLabel("");
					JLabel button53=new JLabel("");
					JLabel button54=new JLabel("");
					JLabel button55=new JLabel("");
					JLabel button56=new JLabel("");
					JLabel button57=new JLabel("");
					JLabel button58=new JLabel("");
					JLabel button59=new JLabel("");
					JLabel button60=new JLabel("");
					JLabel button61=new JLabel("");
					JLabel button62=new JLabel("");
					JLabel button63=new JLabel("");
					JLabel button64=new JLabel("");
					JLabel button65=new JLabel("");
					JLabel button66=new JLabel("");
					JLabel button67=new JLabel("");
					JLabel button68=new JLabel("");
					JLabel button69=new JLabel("");
					JLabel button70=new JLabel("");
					JLabel button71=new JLabel("");
					JLabel button72=new JLabel("");
					JLabel button73=new JLabel("");
					JLabel button74=new JLabel("");
					
					JLabel button75=new JLabel("");
					JLabel button76=new JLabel("");
					JLabel button77=new JLabel("");
					JLabel button78=new JLabel("");
					JLabel button79=new JLabel("");
					JLabel button80=new JLabel("");
					JLabel button81=new JLabel("");
					JLabel button82=new JLabel("");
					JLabel button83=new JLabel("");
					board.add(button4_1);
					board.add(button5);
					board.add(button6);
					board.add(button7);
					board.add(button8);
					board.add(button9);
					board.add(button10);
					board.add(button11);
					board.add(button12);
					board.add(button13);
					board.add(button14);
					board.add(button15);
					board.add(button16);
					board.add(button17);
					board.add(button18);
					board.add(button19);
					board.add(button20);
					board.add(button21);
					
					board.add(button22);
					board.add(button23);
					board.add(button24);
					board.add(button25);
					board.add(button26);
					board.add(button27);
					board.add(button28);
					board.add(button29);
					board.add(button30);
					board.add(button31);
					board.add(button32);
					board.add(button33);
					board.add(button34);
					board.add(button35);
					board.add(button36);
					board.add(button37);
					board.add(button38);
					
					board.add(button39);
					board.add(button40);
					board.add(button41);
					board.add(button42);
					board.add(button43);
					board.add(button44);
					board.add(button45);
					board.add(button46);
					board.add(button47);
					board.add(button48);
					board.add(button49);
					board.add(button50);
					board.add(button51);
					board.add(button52);
					board.add(button53);
					board.add(button54);
					board.add(button55);
					board.add(button56);
					board.add(button57);
					board.add(button58);
					board.add(button59);
					board.add(button60);
					board.add(button61);
					board.add(button62);
					board.add(button63);
					board.add(button64);
					board.add(button65);
					board.add(button66);
					board.add(button67);
					board.add(button68);
					board.add(button69);
					board.add(button70);
					board.add(button71);
					board.add(button72);
					board.add(button73);
					board.add(button74);
					board.add(button75);
					board.add(button76);
					board.add(button77);
					board.add(button78);
					board.add(button79);
					board.add(button80);
					board.add(button81);
					board.add(button82);
					board.add(button83);
					
				
					button16.setVisible(false);
					button15.setVisible(false);
					button17.setVisible(false);
					button25.setVisible(false);
					button26.setVisible(false);
					button27.setVisible(false);
					
					button5.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			            	
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			            	
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
			                
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button6.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					
					button7.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			            	button7.setIcon(new ImageIcon(img20));
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button8.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			            	
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			            
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button15.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button17.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button16.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					
					button25.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button27.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					button26.addMouseListener(new MouseListener() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                // 鼠标点击（按下并抬起）
			            }
			            @Override
			            public void mousePressed(MouseEvent e) {
			                // 鼠标按下
			            }
			            @Override
			            public void mouseReleased(MouseEvent e) {
			                // 鼠标释放

			                // 如果是鼠标右键，则显示弹出菜单
			                if (e.isMetaDown()) {
			                    showPopupMenuB(e.getComponent(), e.getX(), e.getY());
			                }
			            }
			            @Override
			            public void mouseEntered(MouseEvent e) {
			                // 鼠标进入组件区域
			            }
			            @Override
			            public void mouseExited(MouseEvent e) {
			                // 鼠标离开组件区域
			            }
			        });
					
		chatBox.setBounds(1008, 366, 273, 367);
		frame.getContentPane().add(chatBox);
		
		button4 =new JButton("11");
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		
		textField = new JTextField();
		textField.setBackground(new Color(52, 59, 67));
		textField.setForeground(new Color(254, 254, 255));
		textField.setText("Type here");
		
		GroupLayout gl_chatBox = new GroupLayout(chatBox);
		gl_chatBox.setHorizontalGroup(
			gl_chatBox.createParallelGroup(Alignment.TRAILING)
				.addComponent(textField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
				.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
		);
		gl_chatBox.setVerticalGroup(
			gl_chatBox.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatBox.createSequentialGroup()
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 338, GroupLayout.PREFERRED_SIZE)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
		);
		chatBox.setLayout(gl_chatBox);
		
		Timer timer = new Timer();
		
		Font font = new Font("Verdana", Font.BOLD, 11);
		textArea.setFont(font);
		textArea.setForeground(new Color(254, 254, 255));
		textArea.setBackground(new Color(52, 59, 67));
		textArea.append("[Flash Point] Welcome to FlashPoint.\nPlease start your chat here.\n");
		textArea.append("[Ben Ruddock] Lets go guys!\n");
//		textArea.setEditable(false);
		
		textField.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
			    textArea.append("[Eric Cao] Hope for the best!!\n");
			    textArea.setEditable(false);
			  }
			}, 3*1000);
			textField.setText("");
		}	
		});
		
		textField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	            	
	            	String input = "[Junha Park] " + textField.getText();
	            	textArea.append(input + "\n");
            		textField.setText("");
	            	
	            	
	            	/*if(input.length() < 29) {
	            		textArea.append(" " + input + "\n");
	            		textField.setText("");
	            	}
	            	 else { 
	            		int unPrintedInput = input.length(), startIndex = 0, endIndex = 29;
	            		
	            		while(unPrintedInput > 29) {
	            			
	            			textArea.append(" " + input.substring(startIndex, endIndex) + "\n");
	            			
	            		
	            			startIndex = startIndex + 29;
	            			endIndex = startIndex + 29;
	            			unPrintedInput = unPrintedInput - 29;
	            		}
	            		textArea.append(" " + input.substring(startIndex, input.length()) + "\n");
	            		textField.setText("");
	            		
	            	}	*/
	            	
	            }
	        }
	    });
		
		
		
		
		/*JLabel lblNewLabel_1 = new JLabel("");
        
		//adding chatbox image to the chatbox panel
		Image img10 = new ImageIcon(this.getClass().getResource("/chatBoxUpdated.jpg")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img10));
		chatBox.add(lblNewLabel_1);
		
		GroupLayout gl_chatBox = new GroupLayout(chatBox);
		gl_chatBox.setHorizontalGroup(
			gl_chatBox.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel_1)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
		);
		gl_chatBox.setVerticalGroup(
			gl_chatBox.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatBox.createSequentialGroup()
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		chatBox.setLayout(gl_chatBox);
		
		*/
		
		
		statsBox = new JLabel("");
		statsBox.setBounds(1007, 366, 272, 366);
		Image img12 = new ImageIcon(this.getClass().getResource("/UpdatedStatsBox.jpg")).getImage();
		statsBox.setIcon(new ImageIcon(img12));
		frame.getContentPane().add(statsBox);
		statsBox.setVisible(false);
		
		gameLogBox = new JLabel("");
		gameLogBox.setBounds(1007, 366, 272, 366);
		Image img11 = new ImageIcon(this.getClass().getResource("/gamelogbox.jpg")).getImage();
		gameLogBox.setIcon(new ImageIcon(img11));
		frame.getContentPane().add(gameLogBox);
		gameLogBox.setVisible(false);
		
		
		JButton btnNewButton = new JButton("");
		Image img1 = new ImageIcon(this.getClass().getResource("/ChatButton.jpg")).getImage();
		btnNewButton.setIcon(new ImageIcon(img1));
		
		btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	chatBox.setVisible(!chatBox.isVisible());
            }
        });
		
		btnNewButton.setBounds(1011, 733, 38, 32);
		frame.getContentPane().add(btnNewButton);
		
		
		
		JButton btnNewButton_1 = new JButton("");
		Image img2 = new ImageIcon(this.getClass().getResource("/redDiceRoll.jpg")).getImage();
		btnNewButton_1.setIcon(new ImageIcon(img2));
		btnNewButton_1.setBounds(1008, 320, 91, 41);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		Image img3 = new ImageIcon(this.getClass().getResource("/blackDiceRoll.jpg")).getImage();
		btnNewButton_2.setIcon(new ImageIcon(img3));
		btnNewButton_2.setBounds(1096, 320, 91, 41);
		frame.getContentPane().add(btnNewButton_2);
	

		
		JButton btnNewButton_3 = new JButton("");
		Image img4 = new ImageIcon(this.getClass().getResource("/endTurnButton.jpg")).getImage();
		btnNewButton_3.setIcon(new ImageIcon(img4));
		btnNewButton_3.setBounds(1185, 320, 91, 41);
		frame.getContentPane().add(btnNewButton_3);
		
		
		
		
		JButton btnNewButton_4 = new JButton("");
		Image img5 = new ImageIcon(this.getClass().getResource("/statisticsButton.jpg")).getImage();
		btnNewButton_4.setIcon(new ImageIcon(img5));
		
		btnNewButton_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	statsBox.setVisible(!statsBox.isVisible());
            }
        });
		btnNewButton_4.setBounds(1093, 733, 38, 32);
		frame.getContentPane().add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("");
		Image img6 = new ImageIcon(this.getClass().getResource("/GamelogButton.jpg")).getImage();
		btnNewButton_5.setIcon(new ImageIcon(img6));
		
		btnNewButton_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	gameLogBox.setVisible(!gameLogBox.isVisible());
            }
        });
		btnNewButton_5.setBounds(1052, 733, 38, 32);
		frame.getContentPane().add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("");
		Image img7 = new ImageIcon(this.getClass().getResource("/bugreport button.jpg")).getImage();
		btnNewButton_6.setIcon(new ImageIcon(img7));
		btnNewButton_6.setBounds(1250, 735, 28, 28);
		frame.getContentPane().add(btnNewButton_6);
		
		JLabel lblNewLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/customGameBoard.jpg")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(0, 0, 1280, 765);
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		
		
		
	}
	
	public static void showPopupMenu(Component invoker, int x, int y) {
        // 创建 弹出菜单 对象
        JPopupMenu popupMenu = new JPopupMenu();

        // 创建 一级菜单
        JMenuItem movedown = new JMenuItem("move down");
        JMenuItem pasteMenuItem = new JMenuItem("chop walls");
        JMenuItem moveright = new JMenuItem("move right");
        JMenuItem moveleft= new JMenuItem("move left");
        JMenuItem fileMenu = new JMenuItem("exit");

        // 创建 二级菜单
       
        // 添加 二级菜单 到 "编辑"一级菜单
     

        // 添加 一级菜单 到 弹出菜单
        popupMenu.add(movedown);
        popupMenu.add(pasteMenuItem);
        popupMenu.add(moveright);
        popupMenu.add(moveleft);
        popupMenu.addSeparator();       // 添加一条分隔符
      
        popupMenu.add(fileMenu);

        // 添加菜单项的点击监听器
        movedown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 
            	button7.setVisible(false);
            if(1==1) {
            	   button17.setVisible(true);
            }
            }
        });
        
        // ......

        // 在指定位置显示弹出菜单
        popupMenu.show(invoker, x, y);
    }
    public static void showPopupMenuB (Component invoker, int x, int y ) {
        
        JPopupMenu popupMenu = new JPopupMenu(); 
        JMenuItem moveleft = new JMenuItem("move left");
        JMenuItem movedown = new JMenuItem("move down");
  
        JMenuItem moveright = new JMenuItem("move right");
       
        JMenuItem pasteMenuItem = new JMenuItem("chop walls");
        JMenuItem moveUp = new JMenuItem("move up");
        
        JMenu editMenu = new JMenu("extinguish");   
        JMenuItem fileMenu = new JMenuItem("exit");

        
        JMenuItem findMenuItem = new JMenuItem("put out smoke");
        JMenuItem replaceMenuItem = new JMenuItem("put out fire");
        
        editMenu.add(findMenuItem);
        editMenu.add(replaceMenuItem);
        popupMenu.add(moveright);
        popupMenu.add(movedown);
        popupMenu.add(moveleft);
        popupMenu.add(pasteMenuItem);
        popupMenu.addSeparator();       // Ã¦Â·Â»Ã¥Å Â Ã¤Â¸â‚¬Ã¦ï¿½Â¡Ã¥Ë†â€ Ã©Å¡â€�Ã§Â¬Â¦
        popupMenu.add(editMenu);
        popupMenu.add(fileMenu);
       
      
        movedown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button15.setVisible(false);
                button25.setVisible(true);
            }
        });
        moveright.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	button25.setVisible(false);
            	button26.setVisible(true);
            }
        });
      
        
        replaceMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button27.setVisible(true);
            }
        });
        moveleft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(num==0) { 
            	button17.setVisible(false);
            	 button16.setVisible(true);
            	}else {
            		button16.setVisible(false);
	            	 button15.setVisible(true);
            	}
            num=1;
            }
        });
        // ......

        
        popupMenu.show(invoker, x, y);
    }
}
