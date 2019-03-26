package custom_panels;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadGamePanel extends JPanel {
	private JPanel pnl_loadGame;
	private JLabel lbl_loadGame;
	private JPanel panel;
	
	
	public LoadGamePanel() {
		setLayout(null);
		
		initializePanels();
	}


	private void initializePanels() {
		panel = new JPanel();
		panel.setBackground(Color.red);
		panel.setBounds(93, 29, 210, 201);
		add(panel);
	}
	
}
