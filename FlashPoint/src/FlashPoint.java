import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
//import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//zz

public class FlashPoint {

	private JFrame frame;
	private JTextField textField;
	private JButton button4;
	private JLabel button4_1;
	private JLabel button5;
	private JLabel button6;
	private JLabel button7;
	private JLabel button8;
	private JLabel button9;
	private JLabel button10;
	private JLabel button11;
	private JLabel button12;
	private JLabel button13;
	private JLabel button14;
	private JLabel button15;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
////					FlashPoint window = new FlashPoint();
////					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

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
		
		
		JPanel chatBox = new JPanel();
		chatBox.setVisible(false);
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
		
		Font font = new Font("Verdana", Font.BOLD, 11);
		textArea.setFont(font);
		textArea.setForeground(new Color(254, 254, 255));
		textArea.setBackground(new Color(52, 59, 67));
		textArea.append("[Flash Point] Welcome to FlashPoint.\nPlease start your chat here.\n");
		textArea.setEditable(false);
		
		textField.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
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
		statsBox.setBounds(1007, 366, 273, 367);
		Image img12 = new ImageIcon(this.getClass().getResource("/statsBoxUpdated.jpg")).getImage();
		statsBox.setIcon(new ImageIcon(img12));
		frame.getContentPane().add(statsBox);
		statsBox.setVisible(false);
		
		gameLogBox = new JLabel("");
		gameLogBox.setBounds(1007, 366, 273, 367);
		Image img11 = new ImageIcon(this.getClass().getResource("/chatBoxUpdated.jpg")).getImage();
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
		frame.getContentPane().add(lblNewLabel);
		
		//gameboard cells
				JLabel board = new JLabel();
				
				
				board.setBorder(null);
				board.setBounds(145, 25, 892, 713);
				frame.getContentPane().add(board);
				board.setLayout(new GridLayout(8,10));
				button4_1=new JLabel("11");	
				button5=new JLabel("12");
				button6=new JLabel("13");
				button7=new JLabel("14");
				button8=new JLabel("15");
				button9=new JLabel("16");
				button10=new JLabel("17");
				button11=new JLabel("18");
				button12=new JLabel("19");
				button13=new JLabel("10");
				button14=new JLabel("21");
				button15=new JLabel("22");
				JLabel button16 = new JLabel("23");
				JLabel button17=new JLabel("24");
				JLabel button18=new JLabel("25");
				JLabel button19=new JLabel("26");
				JLabel button20=new JLabel("27");
				JLabel button21=new JLabel("28");
				JLabel button22=new JLabel("29");
				JLabel button23=new JLabel("20");
				JLabel button24=new JLabel("31");
				JLabel button25=new JLabel("32");
				JLabel button26=new JLabel("33");
				JLabel button27=new JLabel("34");
				JLabel button28=new JLabel("35");
				JLabel button29=new JLabel("36");
				JLabel button30=new JLabel("37");
				JLabel button31=new JLabel("38");
				JLabel button32=new JLabel("39");
				JLabel button33=new JLabel("30");
				JLabel button34=new JLabel("41");
				JLabel button35=new JLabel("42");
				JLabel button36=new JLabel("43");
				JLabel button37=new JLabel("44");
				JLabel button38=new JLabel("45");
				
					JLabel button39=new JLabel("46");
					JLabel button40=new JLabel("47");
					JLabel button41=new JLabel("48");
					JLabel button42=new JLabel("49");
					JLabel button43=new JLabel("40");
					JLabel button44=new JLabel("51");
					JLabel button45=new JLabel("52");
					JLabel button46=new JLabel("53");
					JLabel button47=new JLabel("54");
					JLabel button48=new JLabel("55");
					JLabel button49=new JLabel("56");
					JLabel button50=new JLabel("47");
					JLabel button51=new JLabel("58");
					JLabel button52=new JLabel("59");
					JLabel button53=new JLabel("50");
					JLabel button54=new JLabel("61");
					JLabel button55=new JLabel("62");
					JLabel button56=new JLabel("63");
					JLabel button57=new JLabel("64");
					JLabel button58=new JLabel("65");
					JLabel button59=new JLabel("66");
					JLabel button60=new JLabel("67");
					JLabel button61=new JLabel("68");
					JLabel button62=new JLabel("69");
					JLabel button63=new JLabel("60");
					JLabel button64=new JLabel("71");
					JLabel button65=new JLabel("72");
					JLabel button66=new JLabel("73");
					JLabel button67=new JLabel("74");
					JLabel button68=new JLabel("75");
					JLabel button69=new JLabel("76");
					JLabel button70=new JLabel("77");
					JLabel button71=new JLabel("78");
					JLabel button72=new JLabel("79");
					JLabel button73=new JLabel("70");
					JLabel button74=new JLabel("81");
					
					JLabel button75=new JLabel("82");
					JLabel button76=new JLabel("83");
					JLabel button77=new JLabel("84");
					JLabel button78=new JLabel("85");
					JLabel button79=new JLabel("86");
					JLabel button80=new JLabel("87");
					JLabel button81=new JLabel("88");
					JLabel button82=new JLabel("89");
					JLabel button83=new JLabel("80");
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
					
					button5.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button6.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button7.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button15.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button16.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button17.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button25.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button27.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
					button26.addMouseListener(new MouseListener() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // é¼ æ ‡ç‚¹å‡»ï¼ˆæŒ‰ä¸‹å¹¶æŠ¬èµ·ï¼‰
		            }
		            @Override
		            public void mousePressed(MouseEvent e) {
		                // é¼ æ ‡æŒ‰ä¸‹
		            }
		            @Override
		            public void mouseReleased(MouseEvent e) {
		                // é¼ æ ‡é‡Šæ”¾

		                // å¦‚æžœæ˜¯é¼ æ ‡å�³é”®ï¼Œåˆ™æ˜¾ç¤ºå¼¹å‡ºè�œå�•
		                if (e.isMetaDown()) {
		                    showPopupMenu(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                // é¼ æ ‡è¿›å…¥ç»„ä»¶åŒºåŸŸ
		            }
		            @Override
		            public void mouseExited(MouseEvent e) {
		                // é¼ æ ‡ç¦»å¼€ç»„ä»¶åŒºåŸŸ
		            }
		        });
		
		
		
		
	}
	public static void showPopupMenu (Component invoker, int x, int y ) {
        
        JPopupMenu popupMenu = new JPopupMenu(); 
        JMenuItem copyMenuItem = new JMenuItem("move");
        JMenuItem pasteMenuItem = new JMenuItem("chop walls");
        JMenu editMenu = new JMenu("extinguish");   
        JMenuItem fileMenu = new JMenuItem("exit");

        
        JMenuItem findMenuItem = new JMenuItem("put out smoke");
        JMenuItem replaceMenuItem = new JMenuItem("out out fire");
        
        editMenu.add(findMenuItem);
        editMenu.add(replaceMenuItem);

        
        popupMenu.add(copyMenuItem);
        popupMenu.add(pasteMenuItem);
        popupMenu.addSeparator();       // æ·»åŠ ä¸€æ�¡åˆ†éš”ç¬¦
        popupMenu.add(editMenu);
        popupMenu.add(fileMenu);

        
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("å¤�åˆ¶ è¢«ç‚¹å‡»");
            }
        });
        findMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("æŸ¥æ‰¾ è¢«ç‚¹å‡»");
            }
        });
        // ......

        
        popupMenu.show(invoker, x, y);
    }
}

