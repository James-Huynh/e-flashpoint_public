package custom_panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import chat.ChatMsgEntity;
import client.ClientManager;
import commons.bean.TextMessage;

/**
 * 
 * @author James
 *
 */
public class ChatBox extends JPanel{
	private JTextField textField;
	private JScrollPane scrollPane;
	private JPanel panel_main;
	private JButton btn_sendMess;

	//	private final EventListenerList REGISTERED_OBJECTS;

	private Rectangle rect_main;
	private Rectangle rect_textArea;
	private Rectangle rect_chat;
	private Rectangle rect_button;
	private JTextPane textPane;

	private DefaultStyledDocument document = new DefaultStyledDocument();
	private StyleContext context = new StyleContext();
	private Style style = context.addStyle("James", null);

	private ClientManager clientManager;
	private List<ChatMsgEntity> mDataArrays;

	public ChatBox(int x, int y, ClientManager myClientManager) {
		setLayout(null);

		this.clientManager = myClientManager;

		rect_main = new Rectangle(0, 0, x, y);
		createRectangles();

		panel_main = new JPanel();
		panel_main.setBounds(rect_main);
		panel_main.setLayout(null);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (isEnterKey(e) == true) {
					sendGUIMessageRequest();
				}
			}
		});



		document = new DefaultStyledDocument();
		StyleConstants.setForeground(style, Color.red);
		document.addStyle("hello", style);
		textPane = new JTextPane(document);

		textPane.setFont(new Font("Open Sans", Font.BOLD, 18));
		textPane.setEnabled(false);
		textPane.setBounds(rect_textArea);



		scrollPane = new JScrollPane(textPane);

		panel_main.add(scrollPane);
		scrollPane.setBounds(rect_textArea);

		textField.setBounds(rect_chat);
		panel_main.add(textField);
		textField.setColumns(10);

		btn_sendMess = new JButton(">");
		btn_sendMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendGUIMessageRequest();
			}
		});
		btn_sendMess.setBounds(rect_button);
		panel_main.add(btn_sendMess);
	}


	private void createRectangles() {
		rect_textArea = new Rectangle(0, 0, (int) rect_main.getWidth(), (int) rect_main.getHeight() - 30);
		rect_chat = new Rectangle(0, (int) rect_textArea.getHeight() , (int) rect_main.getWidth() - 50, (int) (rect_main.getHeight() - rect_textArea.getHeight()));
		rect_button = new Rectangle((int) rect_chat.getWidth(), (int) rect_textArea.getHeight(), (int) (rect_main.getWidth() -  rect_chat.getWidth()), (int)  (rect_main.getHeight() - rect_textArea.getHeight()));

	}


	private boolean isEnterKey(KeyEvent e) {
		return (e.getKeyCode() == KeyEvent.VK_ENTER);
	}


	public void refreshChatBox(boolean b) {
		final StringBuilder finalText = new StringBuilder();
		//document = new DefaultStyledDocument();
		
		mDataArrays = clientManager.getChatArray();
		
		if(b) {
//			try {
//				document.remove(0, document.getLength());
//			} catch (BadLocationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			for (ChatMsgEntity entity : mDataArrays) {
				finalText.delete(0, finalText.length());
				finalText.append("[" + entity.getDate() + "] ");
				finalText.append(entity.getName() + ": ");
				finalText.append(entity.getMessage() + "\n");
				
				updateChatGUI(finalText.toString());
			}
		} else {
			if(!mDataArrays.isEmpty()) {
				ChatMsgEntity entity  = mDataArrays.get(0);
				finalText.append("[" + entity.getDate() + "] ");
				finalText.append(entity.getName() + ": ");
				finalText.append(entity.getMessage() + "\n");
				
				
				updateChatGUI(finalText.toString());
			}
		}
		
		
		
	}

	private void updateChatGUI(String text) {
		try {
			System.out.println("this is in UCG" + text);
			document.insertString(document.getLength(), text, style);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}

		textField.setText("");
	}

	/*
	public void addSelectionPiecesListenerListener(SearchEntryListener obj) {
		REGISTERED_OBJECTS.add(SearchEntryListener.class, obj);
	}

	private void raiseEvenStringTyped() {
		for (ChatLobbyListener listener: REGISTERED_OBJECTS.getListeners(ChatLobbyListener.class)) {
			listener.guiMessageTyped();
		}
	}
	 */

	private void sendGUIMessageRequest() {
		TextMessage message = new TextMessage(textField.getText());


		clientManager.sendMsgRequest(message);

		//	this.updateChatGUI(textField.getText() + "\n");				// will be moved to client
		textField.setText("");
	}


	public JTextField getTextField() {
		return textField;
	}
	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public JPanel getPanel_main() {
		return panel_main;
	}
	public void setPanel_main(JPanel panel_main) {
		this.panel_main = panel_main;
	}
}
