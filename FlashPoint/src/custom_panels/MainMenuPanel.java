package custom_panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import personalizedlisteners.mainMenuListeners.MainMenuListener;

/**
 * Class representing the whole page of the main menu encapsulated inside a panel
 * @author James
 *
 */
public class MainMenuPanel extends JPanel {
	JButton createBtn; //@James - Make these private attributes? @Zaid
	JButton findBtn;
	JButton rulesBtn;
	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();

	/**
	 * Create the visible components
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
				raiseEventCreateBtn();
			}
		});

		findBtn = new JButton("Find Lobby");
		findBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		findBtn.setBounds(67, 120, 423, 49);
		findBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventFindBtn();
			}

		});

		rulesBtn = new JButton("Rules");
		rulesBtn.setBounds(67, 206, 423, 49);
		rulesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		rulesBtn.setEnabled(false);

	}
	
	/**
	 * Register an object to be a listener
	 * @param obj
	 */
	public void addSelectionPiecesListenerListener(MainMenuListener obj) {
		REGISTERED_OBJECTS.add(MainMenuListener.class, obj);
	}
	
	/**
	 * Raise an event: the create button has been clicked
	 */
	private void raiseEventCreateBtn() {
		for (MainMenuListener listener: REGISTERED_OBJECTS.getListeners(MainMenuListener.class)) {
			listener.clickCreate();
		}
	}
	

	private void raiseEventFindBtn() {
		for (MainMenuListener listener: REGISTERED_OBJECTS.getListeners(MainMenuListener.class)) {
			listener.clickFind();
		}
	}
	
	
}