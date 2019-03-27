package custom_panels;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoadGamePanel extends JPanel {
	private JPanel pnl_loadGame;
	private JLabel lbl_loadGame;
	private JPanel pnl_main;
	private JLabel lblNewLabel;
	private double lblX, lblY = 10, lblW = 248, lblH = 60;
	private int textAreaX = 10, textAreaW, textAreaH = 36;
	private int textAreaY = 100;
	private JLabel entryTest, entry_1, entry_2, entry_3, entry_4, entry_5;
	private JLabel[] entries = {entryTest,  entry_1, entry_2, entry_3, entry_4, entry_5};
	
	
	public LoadGamePanel() {
		setLayout(null);
		
		initializePanels();
		intializeEntries();
	}

	private void initializePanels() {
		pnl_main = new JPanel();
		pnl_main.setBackground(Color.LIGHT_GRAY);
		pnl_main.setBounds(308, 10, 455, 450);
		this.add(pnl_main);
		pnl_main.setLayout(null);
		
		lblX = pnl_main.getWidth()/2  - lblW/2;
		lblNewLabel = new JLabel("Load Game");
		lblNewLabel.setFont(new Font("Open Sans Semibold", Font.PLAIN, 23));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds((int) lblX, (int) lblY, (int) lblW, (int) lblH);
		pnl_main.add(lblNewLabel);
		
	}
	

	private void intializeEntries() {
		textAreaW = pnl_main.getWidth() - 2*textAreaX;
		
		for (int i = 0; i < entries.length; i++) {
			JLabel currEntry = entries[i];
			
			currEntry = new JLabel();
			currEntry.setBounds(textAreaX, textAreaY + i * (textAreaH + 10), textAreaW, textAreaH);
			currEntry.setFont(new Font("Open Sans", Font.PLAIN, 14));
			currEntry.setBackground(Color.white);
			currEntry.setOpaque(true);
			currEntry.setText("GameSave: " + i);
			pnl_main.add(currEntry);
			
			currEntry.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					// do something
				}
			});
			
		}
		
		
		
		// Testing
		entryTest = new JLabel();
		entryTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
		});
		entryTest.setBackground(Color.PINK);
		entryTest.setText("TEST");
		entryTest.setBackground(Color.white);
		entryTest.setOpaque(true);
		entryTest.setBounds(textAreaX, 94, textAreaW, textAreaH);
		pnl_main.add(entryTest);
		// Testing
		
	}

	public JPanel getPnl_main() {
		return pnl_main;
	}

	public void setPnl_main(JPanel pnl_main) {
		this.pnl_main = pnl_main;
	}

}
