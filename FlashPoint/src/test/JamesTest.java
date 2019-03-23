package test;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import custom_panels.ChatBox;

public class JamesTest {
	private JFrame mainFrame;
	private Container contentPane;
	private ChatBox testChat;
	private JPanel chatBoxPanel;



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
		
		testChat = new ChatBox();
		chatBoxPanel = testChat.getPanel_main();
		chatBoxPanel.setLocation(100, 100);
		contentPane.add(chatBoxPanel);
	}
}
