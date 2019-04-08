package custom_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.ClientManager;
import personalizedlisteners.mainMenuListeners.MainMenuListener;

/**
 * Class representing the whole page of the main menu encapsulated inside a panel
 * @author James
 *
 */
public class MainMenuPanel extends JPanel {
	private static String defaultImagesPath = "img";
	private static String imageName = "/background-dark_firefighters.jpg";

	private	JButton createBtn;
	private	JButton findBtn;
	private	JButton rulesBtn;
	private JButton btn_LoadGame;
	private JLabel lbl_image;
	private ImageIcon imageIconBackground;
	
	private JPanel headerPanel;
	private JLabel headerLabel;

	private ClientManager clientManager;

	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();
	private JPanel panel;


	/**
	 * Create the visible components
	 */
	public MainMenuPanel(ClientManager myClientManager) {
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);

		this.clientManager = myClientManager;

		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(200, 129, 540, 473);
		menuPanel.setLayout(null);
		menuPanel.setBackground(new Color(0,0,0,0));

		createButtons();
		createHeaderPanel();

		menuPanel.add(createBtn);
		menuPanel.add(findBtn);
		menuPanel.add(rulesBtn);
		menuPanel.add(btn_LoadGame);

		this.add(menuPanel);
	
//		initializeImages();
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setBounds(34,22,449,107);
		headerPanel.setOpaque(false);
		this.add(headerPanel);
		createHeader();
	}
	
	private void createHeader() {
		headerLabel = new JLabel("FLASHPOINT");
		headerLabel.setBounds(6, 6, 533, 84);
		headerLabel.setForeground(new Color(255, 0, 0));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 58));
		headerPanel.add(headerLabel);	
	}

	private void createButtons() {
		btn_LoadGame = new JButton("Load Game");
		btn_LoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				raiseEventLoadBtn();
			}
		});
			btn_LoadGame.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
				panel.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				@Override
				public void mouseExited(MouseEvent e) {
				panel.setBorder(null);

			
		}});
			
			
		btn_LoadGame.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		btn_LoadGame.setBounds(67, 194, 423, 49);


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
				System.out.println("We clicked find lobby");
				if(findLobbyRequest() == true) {
					System.out.println("Lobbies were found");
					raiseEventFindBtn();
				}else {
					System.out.println("Lobbies were not found");
				}

			}


		});

		rulesBtn = new JButton("Rules");
		rulesBtn.setBounds(67, 274, 423, 49);
		rulesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 18));
		rulesBtn.setEnabled(false);

	}


	private void initializeImages() {
		BufferedImage imageBackground = null;

		try {
			imageBackground = ImageIO.read(new File(defaultImagesPath + imageName));
		} catch (IOException e) {
			System.out.println("Image cannot be loaded: " + imageName);
		}

		
		// TMP
		int w = imageBackground.getWidth();
		int h = imageBackground.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(1.0, 1.0);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(imageBackground, after);
		// TMP
		
		imageIconBackground = new ImageIcon(after);
	
		lbl_image = new JLabel(imageIconBackground);
//		lbl_image.setal
		lbl_image.setBounds(0, 0, 1000, 800);
		this.add(lbl_image);
		
		
//		btn_LoadGame.setIcon(imageIconBackground); // testing
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


	private void raiseEventLoadBtn() {
		for (MainMenuListener listener: REGISTERED_OBJECTS.getListeners(MainMenuListener.class)) {
			listener.clickLoad();
		}
	}

	private boolean findLobbyRequest() {
		return clientManager.lobbyListRequest();
	}
}