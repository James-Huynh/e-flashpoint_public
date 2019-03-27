package custom_panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class LoadGamePanel extends JPanel {
	private JPanel pnl_loadGame;
	private JLabel lbl_loadGame;
	private JPanel pnl_main;
	private JLabel lblNewLabel;
	private double lblX, lblY = 10, lblW = 248, lblH = 60;
	private int textAreaX = 10, textAreaW, textAreaH = 36;
	private int textAreaY = 100;
	private JLabel entryTest, entry_1, entry_2, entry_3, entry_4, entry_5;
	private ArrayList<JLabel> listEntries;


	public LoadGamePanel(final File folder) {
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
		
		listEntries = new ArrayList<JLabel>();
		listEntries.add(entry_1);
		listEntries.add(entry_2);
		listEntries.add(entry_3);
		listEntries.add(entry_4);
		listEntries.add(entry_5);

		for (int i = 0; i < listEntries.size(); i++) {
			JLabel currEntry = listEntries.get(i);

			currEntry = new JLabel();
			currEntry.setBounds(textAreaX, textAreaY + i * (textAreaH + 10), textAreaW, textAreaH);
			currEntry.setFont(new Font("Open Sans", Font.PLAIN, 14));
			currEntry.setBackground(Color.white);
			currEntry.setOpaque(true);
			currEntry.setText("GameSave: " + i);
			pnl_main.add(currEntry);

			currEntry.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
					entryClicked((JLabel) e.getComponent());
					}
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


	private void entryClicked(JLabel entry) {
		System.out.println(entry.getText());

		// @Eric call loadMethod
	}

	public JPanel getPnl_main() {
		return pnl_main;
	}

	public void setPnl_main(JPanel pnl_main) {
		this.pnl_main = pnl_main;
	}

}