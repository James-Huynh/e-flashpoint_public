package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Class representing the whole page of the main menu encapsulated inside a panel
 * @author James
 *
 */
public class MainMenuPanel extends JPanel {
	JButton createBtn;
	JButton findBtn;
	JButton rulesBtn;

	/**
	 * Create the panel.
	 */
	public MainMenuPanel() {
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);

		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(200, 200, 540, 297);
		menuPanel.setLayout(null);

		createButtons();

		menuPanel.add(createBtn);
		menuPanel.add(findBtn);
		menuPanel.add(rulesBtn);

		this.add(menuPanel);
	}

	private void createButtons() {
		createBtn = new JButton("Create Lobby");
		createBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		createBtn.setBounds(67, 35, 423, 49);
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		findBtn = new JButton("Find Lobby");
		findBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		findBtn.setBounds(67, 120, 423, 49);
		findBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		rulesBtn = new JButton("Rules");
		rulesBtn.setBounds(67, 206, 423, 49);
		rulesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		rulesBtn.setEnabled(false);

	}
}