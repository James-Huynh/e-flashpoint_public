package custom_panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ChatBox extends JPanel{
	private JTextField textField;
	private JScrollPane scrollPane;
	private JPanel panel_main;
	private JButton btn_sendMess;

	private final Rectangle rect_main = new Rectangle(0, 0, 300, 500);	// arbitrary
	private final Rectangle rect_textArea = new Rectangle(0, 0, (int) rect_main.getWidth(), (int) rect_main.getHeight() - 30);
	private final Rectangle rect_chat = new Rectangle(0, (int) rect_textArea.getHeight() , (int) rect_main.getWidth() - 50, (int) (rect_main.getHeight() - rect_textArea.getHeight()));
	private final Rectangle rect_button = new Rectangle((int) rect_chat.getWidth(), (int) rect_textArea.getHeight(), (int) (rect_main.getWidth() -  rect_chat.getWidth()), (int)  (rect_main.getHeight() - rect_textArea.getHeight()));
	private JTextPane textPane;

	private DefaultStyledDocument document;
	private StyleContext context = new StyleContext();
	private Style style = context.addStyle("test", null);

	public ChatBox() {
		setLayout(null);

		panel_main = new JPanel();
		panel_main.setBounds(rect_main);
		panel_main.setLayout(null);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (isEnterKey(e) == true) {
					System.out.println("Enter key true");
					displayMessage();
				}
			}
		});



		document = new DefaultStyledDocument();
		textPane = new JTextPane(document);
		
		textPane.setFont(new Font("Open Sans", Font.BOLD, 18));
		textPane.setText("lololol");
		textPane.setEnabled(false);
		textPane.setBounds(rect_textArea);
		


		scrollPane = new JScrollPane(textPane);

		panel_main.add(scrollPane);
		scrollPane.setBounds(rect_textArea);

		textField.setBounds(rect_chat);
		panel_main.add(textField);
		textField.setColumns(10);

		btn_sendMess = new JButton("New button");
		btn_sendMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMessage();
			}
		});
		btn_sendMess.setBounds(rect_button);
		panel_main.add(btn_sendMess);
	}


	private boolean isEnterKey(KeyEvent e) {
		return (e.getKeyCode() == KeyEvent.VK_ENTER);
	}


	private void displayMessage() {
		String currText = textPane.getText();
		String newText = "\n" + textField.getText();

		StyleConstants.setForeground(style, Color.red);
		try {
			document.insertString(document.getLength(), newText, style);
		} catch(BadLocationException exc) {
			exc.printStackTrace();
		}


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
