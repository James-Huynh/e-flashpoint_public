package custom_panels;


import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import chat.ChatMsgEntity;
import client.ClientManager;
import commons.bean.TextMessage;
import token.Colour;

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

	private StyledDocument documento;
	MutableAttributeSet attrs;
	//private Style style;

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


		textPane = new JTextPane();
		documento = (StyledDocument) textPane.getStyledDocument();
		
		//style = textPane.addStyle("StyleName", null);
		
		attrs = textPane.getInputAttributes();
		//style = textPane.addStyle("StyleName", null);
		//that's how u change color
		StyleConstants.setForeground(attrs, Color.black);
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

		
		mDataArrays = clientManager.getChatArray();
		
		if(b) {
			List<ChatMsgEntity> temp = reverseList(mDataArrays);
//			try {
//				document.remove(0, document.getLength());
//			} catch (BadLocationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			for (ChatMsgEntity entity : temp) {
				finalText.delete(0, finalText.length());
				finalText.append(entity.getDate());
				finalText.append(entity.getName() + ": ");
				finalText.append(entity.getMessage() + "\n");
				
				
				Colour playerColor = entity.getColour();
				System.out.println("im here" + playerColor);
				//change font color here
				if(playerColor == Colour.GREEN) StyleConstants.setForeground(attrs, Color.GREEN);
				if(playerColor == Colour.BLUE) StyleConstants.setForeground(attrs, Color.BLUE);
				if(playerColor == Colour.RED) StyleConstants.setForeground(attrs, Color.RED);
				if(playerColor == Colour.PURPLE) StyleConstants.setForeground(attrs, Color.MAGENTA);
				if(playerColor == Colour.BLACK) StyleConstants.setForeground(attrs, Color.BLACK);
				if(playerColor == Colour.WHITE) StyleConstants.setForeground(attrs, Color.WHITE);
				
				updateChatGUI(finalText.toString());
			}
		} else {
			if(!mDataArrays.isEmpty()) {
				ChatMsgEntity entity  = mDataArrays.get(0);
				finalText.append("[" + entity.getDate() + "] ");
				finalText.append(entity.getName() + ": ");
				finalText.append(entity.getMessage() + "\n");
				
				Colour playerColor = entity.getColour();
				System.out.println("im here2" + playerColor);
				//change font color here
				if(playerColor == Colour.GREEN) StyleConstants.setForeground(attrs, Color.GREEN);
				if(playerColor == Colour.BLUE) StyleConstants.setForeground(attrs, Color.BLUE);
				if(playerColor == Colour.RED) StyleConstants.setForeground(attrs, Color.RED);
				if(playerColor == Colour.PURPLE) StyleConstants.setForeground(attrs, Color.MAGENTA);
				if(playerColor == Colour.BLACK) StyleConstants.setForeground(attrs, Color.BLACK);
				if(playerColor == Colour.WHITE) StyleConstants.setForeground(attrs, Color.WHITE);
				
				
				updateChatGUI(finalText.toString());
			}
		}
		
		
		
	}

	private void updateChatGUI(String text) {
		try {
			System.out.println("this is in UCG" + text);
//			ChatMsgEntity entity  = mDataArrays.get(0);
//			Colour playerColor = entity.getColour();
//			//change font color here
//			if(playerColor == Colour.GREEN) StyleConstants.setForeground(style, Color.GREEN);
//			if(playerColor == Colour.BLUE) StyleConstants.setForeground(style, Color.BLUE);
//			if(playerColor == Colour.RED) StyleConstants.setForeground(style, Color.RED);
//			if(playerColor == Colour.PURPLE) StyleConstants.setForeground(style, Color.MAGENTA);
//			if(playerColor == Colour.BLACK) StyleConstants.setForeground(style, Color.BLACK);
//			if(playerColor == Colour.WHITE) StyleConstants.setForeground(style, Color.WHITE);
			
			documento.insertString(documento.getLength(), text, attrs);
			
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}

		textField.setText("");
		textField.requestFocus();
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
	public List<ChatMsgEntity> reverseList(List<ChatMsgEntity> someList){
		List<ChatMsgEntity> tempList = new ArrayList<ChatMsgEntity>(someList);
		Collections.reverse(tempList);
		return tempList;
	}
}
