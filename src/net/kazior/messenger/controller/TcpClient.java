package net.kazior.messenger.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClient extends Thread {

	private Controller controller;

	private Socket clientSocket;
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;

	private volatile boolean running = true;

	public TcpClient(Controller controller) {
		this.controller = controller;
		connectToServer();
	}

	private void connectToServer() {
		try {
			controller.tellConnectionIsNotReady();
			clientSocket = new Socket("localhost", 8086);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			controller.tellConnectionIsReady();
		} catch (IOException e) {
			System.out.println("Problem while connecting to SERVER.");
			askAndReconnectToServer();
		}
	}

	private void askAndReconnectToServer() {
		if (controller.askForReconnection() == true) {
			closeConnections();
			connectToServer();
		} else {
			System.exit(0);
		}
	}

	public void sendMessageToServer(String message) {
		try {
			outToServer.writeBytes(message + '\n'); // NullPointerException could happen, but we disable send button when the connection is not set
		} catch (IOException e) {	// very unlikely, we do not have to do anything with it, because our listening loop will do
			System.out.println("Problem while sending a message.");
		}
	}

	private void receiveMessageFromServer() {
		try {
			String receivedMessage = inFromServer.readLine(); // returns null if the other end was not closed properly
			if (receivedMessage != null) {
				System.out.println("Received from SERVER: \"" + receivedMessage+"\".");
				controller.receiveMessage(receivedMessage);
			} else {
				System.out.println("SERVER has closed.");
				askAndReconnectToServer();
			}
		} catch (IOException e) { // happens when the other end was not closed properly or the connection was lost or we closed our connections
			if (running) { // this is when we did not close connections by closing view
				System.out.println("SERVER has disconnected in wrong way.");
				askAndReconnectToServer();
			} else { // this is when we closed connections by closing View
				System.out.println("CLIENT has closed connection - could not listen to SERVER.");
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			receiveMessageFromServer();
		}
	}

	public void closeConnections() { // if there are any connections and they are opened, close them in opposite order to opening

		if (outToServer != null)
			try {
				outToServer.close(); //nothing happens if we close it more than once
			} catch (IOException e) {
				System.out.println("Problem while closing the socket."); // very unlikely
			}

		if (inFromServer != null)
			try {
				inFromServer.close(); // nothing happens if we close it more than once
			} catch (IOException e) {
				System.out.println("Problem while closing input reader."); // very unlikely
			}

		if (clientSocket != null && !clientSocket.isClosed())
			try {
				clientSocket.close(); // not sure if we can close it more than once, but we can check if it is closed
			} catch (IOException e) {
				System.out.println("Problem while closing output stream."); // very unlikely	
			}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
