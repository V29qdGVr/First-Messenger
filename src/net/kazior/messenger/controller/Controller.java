package net.kazior.messenger.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import net.kazior.messenger.view.View;

public class Controller {

	public static final String LOG_IN_COMMAND = "[**SIGN_IN_COMMAND**]";
	public static final String SIGN_UP_COMMAND = "[**SIGN_UP_COMMAND**]";
	public static final String MESSAGE_COMMAND = "[**MESSAGE_COMMAND**]";
	public static final String SPLITTER = "[**SPLITTER**]";

	public static final String LOG_IN_BACK_COMMAND_TRUE = "[**SIGN_IN_BACK_COMMAND_TRUE**]";
	public static final String LOG_IN_BACK_COMMAND_FALSE = "[**SIGN_IN_BACK_COMMAND_FALSE**]";
	public static final String SIGN_UP_BACK_COMMAND_TRUE = "[**SIGN_UP_BACK_COMMAND_TRUE**]";
	public static final String SIGN_UP_BACK_COMMAND_FALSE = "[**SIGN_UP_BACK_COMMAND_FALSE**]";
	public static final String MESSAGE_BACK_COMMAND_TRUE = "[**MESSAGE_BACK_COMMAND_TRUE**]";
	public static final String MESSAGE_BACK_COMMAND_FALSE = "[**MESSAGE_BACK_COMMAND_FALSE**]";


	private View view;
	private InputValidator inputValidator;
	private TcpClient tcpClient;

	private String logInRequest;
	private String signUpRequest;
	private String messageRequest;

	public Controller() {

		view = new View();
		view.addButtonsListener(new ButtonsListener());
		view.bindEnterActionToMessageArea(new EnterAction());
		view.addWindowListener(new WindowListenerForClosing());

		inputValidator = new InputValidator();

		tcpClient = new TcpClient(this);
		tcpClient.start(); // it could be run() function, because GUI has other
		// threads, but constructor would not end
	}

	private class EnterAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.getMainPanel().getSendButton().doClick();
		}
	}

	private class ButtonsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			String parent = (String) ((JButton) e.getSource()).getClientProperty("Parent");
			String actionCommand = e.getActionCommand();

			switch (parent) {
//----------------------------------------------------------------//			
			case "LogInPanel":
				if (actionCommand.equals("LogIn")) {
					String email = view.getLogInPanel().getEmailField().getText();
					String password = view.getLogInPanel().getPasswordField().getText();

					view.showLoadingPanel();

					logInRequest = LOG_IN_COMMAND + SPLITTER + email + SPLITTER + password;
					tcpClient.sendMessageToServer(logInRequest);
				} else if (actionCommand.equals("SignUp")) {
					view.showSignUpPanel1();
				}
				break;
//----------------------------------------------------------------//			
			case "SignUpPanel1":
				if (actionCommand.equals("Continue")) {

					boolean isSomethingWrong = false;
					
					String email = view.getSignUpPanel1().getEmailField().getText();
					if (!inputValidator.isEmailValid(email)) {
						view.getSignUpPanel1().getEmailField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel1().getEmailField().setForeground(Color.white);

					String birthday = view.getSignUpPanel1().getBirthdayField().getText();
					if (!inputValidator.isDateValid(birthday, "dd-MM-yyyy")) {
						view.getSignUpPanel1().getBirthdayField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel1().getBirthdayField().setForeground(Color.white);

					String password = view.getSignUpPanel1().getPasswordField().getText();
					String repeatPassword = view.getSignUpPanel1().getRepeatPasswordField().getText();
					if (!inputValidator.isPasswordValid(password) || !password.equals(repeatPassword)) {
						view.getSignUpPanel1().getRepeatPasswordField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel1().getRepeatPasswordField().setForeground(Color.white);

					if (isSomethingWrong)
						return;
					
					signUpRequest = SIGN_UP_COMMAND + SPLITTER + email+ SPLITTER + birthday + SPLITTER + password + SPLITTER;
					view.showSignUpPanel2();
					
				} else if (actionCommand.equals("Back")) {
					view.showLogInPanel();
				}
				break;
//----------------------------------------------------------------//
			case "SignUpPanel2":
				if (actionCommand.equals("Submit")) {
					
					boolean isSomethingWrong = false;

					String firstName = view.getSignUpPanel2().getFirstNameField().getText();
					if (!inputValidator.isNameValid(firstName)) {
						view.getSignUpPanel2().getFirstNameField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel2().getFirstNameField().setForeground(Color.white);
					
					String lastName = view.getSignUpPanel2().getLastNameField().getText();
					if (!inputValidator.isNameValid(lastName)) {
						view.getSignUpPanel2().getLastNameField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel2().getLastNameField().setForeground(Color.white);
					
					String placeField = view.getSignUpPanel2().getPlaceField().getText();
					if (!inputValidator.isNameValid(placeField)) {
						view.getSignUpPanel2().getPlaceField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel2().getPlaceField().setForeground(Color.white);
					
					String stateField = view.getSignUpPanel2().getStateField().getText();
					if (!inputValidator.isNameValid(stateField)) {
						view.getSignUpPanel2().getStateField().setForeground(Color.ORANGE);
						isSomethingWrong = true;
					} else
						view.getSignUpPanel2().getStateField().setForeground(Color.white);
					
					if (isSomethingWrong)
						return;

					signUpRequest += firstName + SPLITTER + lastName + SPLITTER + placeField + SPLITTER + stateField;
					view.showLoadingPanel();

					tcpClient.sendMessageToServer(signUpRequest);
				} else if (actionCommand.equals("Back")) {
					view.showSignUpPanel1();
				}
				break;
//----------------------------------------------------------------//				
			case "MainPanel":
				if (actionCommand.equals("Send")) {
					JTextArea messageArea = view.getMainPanel().getMessageArea();
					String request = MESSAGE_COMMAND + SPLITTER + view.getNick() + ": " + messageArea.getText(); ///////
					tcpClient.sendMessageToServer(request);
					messageArea.setText("");
				}
				break;
//----------------------------------------------------------------//
			}
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

		if (message.equals(LOG_IN_BACK_COMMAND_FALSE)) {
			view.showLogInPanel();
		} else if (message.equals(LOG_IN_BACK_COMMAND_TRUE)) {
			view.showMainPanel();
			askForNick();
		} else if (message.equals(SIGN_UP_BACK_COMMAND_FALSE)) {
			view.showSignUpPanel1();
		} else if (message.equals(SIGN_UP_BACK_COMMAND_TRUE)) {
			view.showMainPanel();
		} else if (message.equals(MESSAGE_BACK_COMMAND_FALSE)) {
			
		} else if (message.equals(MESSAGE_BACK_COMMAND_TRUE)) {
			
		} else {
			JTextArea chatArea = view.getMainPanel().getChatArea();
			if (chatArea.getText().length() == 0)
				chatArea.append(message);
			else
				chatArea.append(System.lineSeparator() + message);
		}
	}

	public boolean askForReconnection() {
		return view.askYesNoQuestion("Brak połączenia z serwerem", "Próbować ponownie się połączyć?");
	}

	public void tellConnectionIsReady() {
		JTextArea chatArea = view.getMainPanel().getChatArea();
		JButton sendButton = view.getMainPanel().getSendButton();

		if (chatArea.getText().length() == 0)
			chatArea.append("Połączono!");
		else
			chatArea.append(System.lineSeparator() + "Połączono!");

		sendButton.setEnabled(true);
	}

	public void tellConnectionIsNotReady() {
		JTextArea chatArea = view.getMainPanel().getChatArea();
		JButton sendButton = view.getMainPanel().getSendButton();

		if (!chatArea.getText().endsWith("Oczekiwanie na serwer...")) {
			if (chatArea.getText().length() == 0)
				chatArea.append("Oczekiwanie na serwer...");
			else
				chatArea.append(System.lineSeparator() + "Oczekiwanie na serwer...");
		}
		sendButton.setEnabled(false);
	}
	
	public void askForNick() {
		view.setNick(JOptionPane.showInputDialog("Enter your nick"));
	}
}
