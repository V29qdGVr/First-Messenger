package net.kazior.messenger.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class MainPanel extends JPanel {

	private JTextArea chatArea;
	private JScrollPane scrollChatPane;
	private JTextArea messageArea;
	private JScrollPane scrollMessagePane;
	private JButton sendButton;
	
	public MainPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());
		
		chatArea = new JTextArea();
		chatArea.setFont(new Font("Courier New", Font.BOLD, 18));
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		((DefaultCaret)chatArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // makes scrollbar always scrolled down
		scrollChatPane = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		messageArea = new JTextArea();
		messageArea.setFont(new Font("Courier New", Font.BOLD, 16));
		messageArea.setLineWrap(true);
		((DefaultCaret)messageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // makes scrollbar always scrolled down
		scrollMessagePane = new JScrollPane(messageArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		sendButton = new JButton("Send");
		sendButton.putClientProperty("Parent", "MainPanel");

		scrollChatPane.setPreferredSize(new Dimension(width, height / 4 * 3));
		scrollMessagePane.setPreferredSize(new Dimension(width, height / 8));
		sendButton.setPreferredSize(new Dimension(width, height / 8));


		add(scrollChatPane, BorderLayout.NORTH);
		add(scrollMessagePane, BorderLayout.CENTER);
		add(sendButton, BorderLayout.SOUTH);	
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public JButton getSendButton() {
		return sendButton;
	}
}
