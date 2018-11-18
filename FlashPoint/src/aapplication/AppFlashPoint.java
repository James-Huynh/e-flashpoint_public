package aapplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import window.GameLobby;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AppFlashPoint extends JFrame {

	private JPanel contentPane;
	private GameLobby gameLobby;
	private static AppFlashPoint frame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AppFlashPoint();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppFlashPoint() {
		setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				proceedNextWindow();
			}
		});
		btnStart.setBounds(80, 57, 287, 149);
		contentPane.add(btnStart);

	}
	
	private void proceedNextWindow() {
		frame.setVisible(false);
		gameLobby = new GameLobby(this);
		gameLobby.setVisible(true);
		gameLobby.setLocationRelativeTo(null);
	}

}
