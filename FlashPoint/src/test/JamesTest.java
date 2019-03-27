package test;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

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
	private LoadGamePanel loadGame;
	private JPanel loadPanel;

	// test


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
		loadGame = new LoadGamePanel(null);
		loadPanel = loadGame.getPnl_main();
		loadPanel.setLocation(new Point(300, 150));
		contentPane.add(loadPanel);


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
