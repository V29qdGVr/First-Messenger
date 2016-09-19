package net.kazior.messenger.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import net.kazior.messenger.view.View;

public class Controller {

	private TcpClient tcpClient;
	private View view;

	public Controller() {

		view = new View();
		view.askForUsername();
		view.addSendButtonListener(new SendButtonListener());
		view.addWindowListener(new WindowListenerForClosing());

		tcpClient = new TcpClient(this);
		tcpClient.start(); // it could be run() function, because GUI has other threads, but constructor would not end
	}

	private class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea messageArea = view.getMessageArea();
			String message = view.getUsername() + ": " + messageArea.getText();
			tcpClient.sendMessageToServer(message);
			messageArea.setText("");
		}
	}

	private class WindowListenerForClosing extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent we) {

			tcpClient.setRunning(false);
			tcpClient.closeConnections();
			System.exit(0);

		}
	}

	public void receiveMessage(String message) {
		JTextArea chatArea = view.getChatArea();

		if (chatArea.getText().length()==0)
			chatArea.append(message);
		else
			chatArea.append(System.lineSeparator() + message);
	}

	public boolean askForReconnection() {
		return view.askYesNoQuestion("Brak po³¹czenia z serwerem", "Próbowaæ ponownie siê po³¹czyæ?");
	}

	public void tellConnectionIsReady() {
		JTextArea chatArea = view.getChatArea();
		JButton sendButton = view.getSendButon();

		if (chatArea.getText().length()==0)
			chatArea.append("Po³¹czono!");
		else
			chatArea.append(System.lineSeparator() + "Po³¹czono!");

		sendButton.setEnabled(true);
	}

	public void tellConnectionIsNotReady() {
		JTextArea chatArea = view.getChatArea();
		JButton sendButton = view.getSendButon();

		if (!chatArea.getText().endsWith("Oczekiwanie na serwer...")) {
			if (chatArea.getText().length()==0)
				chatArea.append("Oczekiwanie na serwer...");
			else
				chatArea.append(System.lineSeparator() + "Oczekiwanie na serwer...");
		}
		sendButton.setEnabled(false);
	}
}
