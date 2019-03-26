package test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;

import custom_panels.ChatBox;
import custom_panels.LoadGamePanel;

public class JamesTest {
	private JFrame mainFrame;
	private Container contentPane;
	private ChatBox testChat;
	private JPanel chatBoxPanel;
	private int popMenuX = 500;
	private int popMenuY = 200;
	private int panelX = 500;
	private int panelY = 200;



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JamesTest test = new JamesTest();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public JamesTest() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(0, 0, 800, 746);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);
		contentPane = mainFrame.getContentPane();
		
		/////////////
		LoadGamePanel loadPanel = new LoadGamePanel();
		mainFrame.getContentPane().add(loadPanel);
		loadPanel.setVisible(true);
		loadPanel.setLayout(null);
		//////////////
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.YELLOW);
		panel_1.setBounds(233, 222, 270, 331);
		mainFrame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JTextPane txtpnAlpha = new JTextPane();
		txtpnAlpha.setText("Alpha");
		txtpnAlpha.setBounds(0, 159, 270, 26);
		panel_1.add(txtpnAlpha);
		
		//testChat = new ChatBox(0, 0, null);
		//chatBoxPanel = testChat.getPanel_main();
		//chatBoxPanel.setLocation(100, 100);
		//contentPane.add(chatBoxPanel);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
