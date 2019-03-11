package custom_panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import client.Client;
import client.ClientInputThread;
import client.ClientOutputThread;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import gui.Launcher;
import personalizedlisteners.loginListeners.LoginListener;

/**
 * Class representing the login page. 
 * Panel size corresponds to dimensions specified for 'Central' panel in Launcher.java
 * Two panels created i.e 'login' and 'header'. Motivation :- all relevant fields, buttons
 * 	 for login are inside that panel, so easy to move around if need to be changed
 * 		--All attributes global
 * 		--Sub method'ed as much as possible
 * @author zaidyahya & James
 */
public class LoginPanel extends JPanel {
	
	private static ClientManager clientManager;
	private JLabel pwdLabel;
	private JPasswordField password;
	private JTextField userNameField;
	private JLabel usrLabel;
	private JButton loginBtn;
	private JButton registerBtn;
	private JToggleButton tglBtn;
	private JPanel inputPanel;
	private JPanel headerPanel;
	private JLabel headerLabel;
	private PopupFactory popUpHolder;
	private Popup loginFailedPopUp;
	private JPanel popUpPanel;
	
	private final EventListenerList REGISTERED_OBJECTS = new EventListenerList();

	/**
	 * Create the panel.
	 */
	public LoginPanel(Dimension panelDimension, ClientManager clientManager) {
		//super(new BorderLayout());
		//setPreferredSize(panelDimension);  /* Not working */
		setPreferredSize(new Dimension(1000,800));
		setLayout(null);
		this.clientManager = clientManager;
		
		createHeaderPanel();
		createInputPanel();
		createPopUp();
//		serverRequest();
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setBounds(131,60, 653,126);
		createHeader();
		this.add(headerPanel);
	}

	private void createInputPanel() {
		inputPanel = new JPanel();
		inputPanel.setBounds(187, 229, 439, 286);
		inputPanel.setLayout(null);

		//this.add(inputPanel);
		createLoginFields();
		createLoginButton();
		createRegisterButton();
		createRememberMe();
		this.add(inputPanel);

	}

	private void createLoginFields() {
		usrLabel = new JLabel("Username");
		usrLabel.setFont(new Font("Copperplate", Font.PLAIN, 20));
		usrLabel.setBounds(52, 31, 103, 37);
		inputPanel.add(usrLabel);

		userNameField = new JTextField();
		userNameField.setFont(new Font("Avenir", Font.PLAIN, 13));
		userNameField.setBounds(230, 33, 183, 35);
		inputPanel.add(userNameField);
		userNameField.setColumns(10);

		password = new JPasswordField(10);
		password.setBounds(230, 80, 183, 35);
		inputPanel.add(password);

		pwdLabel = new JLabel("Password");
		pwdLabel.setFont(new Font("Copperplate", Font.PLAIN, 20));
		pwdLabel.setBounds(52, 80, 103, 37);
		inputPanel.add(pwdLabel);
	}

	private void createLoginButton() {
		loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Avenir", Font.PLAIN, 20));
		loginBtn.setBounds(89, 182, 117, 35);
		inputPanel.add(loginBtn);

		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Login Clicked");
				if(loginRequest(getUsername(), getPassword())) {
					System.out.println("Login succesful");
					raiseEventLoginBtn();
				}
				else {
					System.out.println("Login failed");
					showPopUp();
				}
				//raiseEventLoginBtn();
			}
		});
	}

	private void createRegisterButton() {
		registerBtn = new JButton("Register");
		registerBtn.setFont(new Font("Avenir", Font.PLAIN, 20));
		registerBtn.setBounds(245, 182, 117, 35);
		inputPanel.add(registerBtn);

		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Register Clicked");
				raiseEventRegisterBtn();
				//**Server request**
			}
		});
	}

	private void createRememberMe() {
		tglBtn = new JToggleButton("Remember Me");
		tglBtn.setSelected(true);
		tglBtn.setBounds(171, 141, 117, 29);
		inputPanel.add(tglBtn);

	}

	private void createHeader() {
		headerLabel = new JLabel("FLASHPOINT");
		headerLabel.setBounds(6, 20, 622, 84);
		headerLabel.setForeground(new Color(255, 0, 0));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Nanum Brush Script", Font.BOLD | Font.ITALIC, 90));
		headerPanel.add(headerLabel);
	}
	
	private void createPopUp() {
		popUpPanel = new JPanel(new BorderLayout());
		popUpHolder = new PopupFactory();
		
		JTextArea text = new JTextArea();
		text.append("Login failed bro");
		text.setLineWrap(true);
		
		JButton okButton = new JButton("ok");
		okButton.setPreferredSize(new Dimension(20,20));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginFailedPopUp.hide();
//				loginFailedPopUp = popUpHolder.getPopup(this, popUpPanel, 1140, 50);
			}
		});
		popUpPanel.setPreferredSize(new Dimension(300,400));
		popUpPanel.setBackground(Color.decode("#FFFFFF"));
		popUpPanel.add(text, BorderLayout.NORTH);
		popUpPanel.add(okButton, BorderLayout.SOUTH);
		loginFailedPopUp = popUpHolder.getPopup(this, popUpPanel, 500, 400);
	}
	
	private void showPopUp() {
		loginFailedPopUp.show();
	}
	
	private void hidePopUp() {
		loginFailedPopUp.hide();
	}

	// James
	/**
	 * Register an object to be a listener
	 * @param obj
	 */
	public void addSelectionPiecesListenerListener(LoginListener obj) {
		REGISTERED_OBJECTS.add(LoginListener.class, obj);
	}

	// James
	/**
	 * Raise an event: the login button has been clicked
	 */
	private void raiseEventLoginBtn() {
		for (LoginListener listener: REGISTERED_OBJECTS.getListeners(LoginListener.class)) {
			listener.clickLogin();
		}
	}
	
	private void raiseEventRegisterBtn() {
		for (LoginListener listener: REGISTERED_OBJECTS.getListeners(LoginListener.class)) {
			listener.clickRegister();
		}
	}

	public String getUsername() {
		return this.userNameField.getText();
	}

	public String getPassword() {
		return new String(this.password.getPassword());
	}
	
	//------------Server Requests------------//
	public boolean loginRequest(String name, String password) {
		return clientManager.loginRequest(name, password);
	}

}
