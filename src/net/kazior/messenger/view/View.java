package net.kazior.messenger.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int width = 320;
	private final int height = 640;

	private String username;

	private JPanel panel;
	private JTextArea chatArea;
	private JScrollPane scrollChatPane;
	private JTextArea messageArea;
	private JScrollPane scrollMessagePane;
	private JButton sendButton;

	public View() {

		setTitle("Messenger");
		initAndAddComponents();

		setResizable(false);
		pack();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initAndAddComponents() {

		panel = new JPanel();

		chatArea = new JTextArea();
		chatArea.setFont(new Font("Courier New", Font.BOLD, 18));
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		scrollChatPane = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		messageArea = new JTextArea();
		messageArea.setFont(new Font("Courier New", Font.BOLD, 16));
		messageArea.setLineWrap(true);
		scrollMessagePane = new JScrollPane(messageArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		sendButton = new JButton("Send");

		panel.setPreferredSize(new Dimension(width, height));
		scrollChatPane.setPreferredSize(new Dimension(width, height / 4 * 3));
		scrollMessagePane.setPreferredSize(new Dimension(width, height / 8));
		sendButton.setPreferredSize(new Dimension(width, height / 8));

		panel.setLayout(new BorderLayout());
		panel.add(scrollChatPane, BorderLayout.NORTH);
		panel.add(scrollMessagePane, BorderLayout.CENTER);
		panel.add(sendButton, BorderLayout.SOUTH);

		add(panel);
	}

	public void addSendButtonListener(ActionListener listener) {
		sendButton.addActionListener(listener);
	}

	public JTextArea getChatArea() {
		return chatArea;
	}
	
	public JTextArea getMessageArea() {
		return messageArea;
	}
	
	public JButton getSendButon() {
		return sendButton;
	}

	public String getUsername() {
		return username;
	}

	public void askForUsername() {
		username = JOptionPane.showInputDialog("Podaj swoje imiê");
	}

	public boolean askYesNoQuestion(String title, String question) {
		int choice = JOptionPane.showOptionDialog(null, question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
}