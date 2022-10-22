package refactor6;

import static java.lang.System.out;
import static java.lang.System.err;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

	public Server server;
	public static Client client;
	public static Thread t;
	public static Scanner in;

	public Game() {
		in = new Scanner(System.in);
		askUserConnectOrHostLoop();
		in.close();
	}

	
	private void askUserConnectOrHostLoop() {
		int choice = 0;
		clearScreen();
		out.print("\n\t1] Connect to game\n\t2] Host Game\n\t3] Host and Connect\n\n\t> ");

		try {
			choice = Integer.valueOf(in.next()).intValue();
		} catch (NumberFormatException e) {
			userPrompt("\n\tTry entering a digit", 500);
			askUserConnectOrHostLoop();
		}

		if (choice == 1) connectToServer();
		else if (choice == 2) initializeServer();
		else if (choice == 3) {
			initializeServer();
			connectToServer();
		} else {
			userPrompt("\n\tPlease select a valid option", 1000);
			askUserConnectOrHostLoop();
		}
	}


	private static void initializeServer() {
		int port = 0, players = 0, lives = 0;
		clearScreen();

		try {
			out.print("\n\tEnter a port number: ");
			port = Integer.valueOf(in.next());
			out.print("\n\tEnter number of players: ");
			players = Integer.valueOf(in.next());
			out.print("\n\tEnter number of lives: ");
			lives = Integer.valueOf(in.next());
		} catch (NumberFormatException e) {
			userPrompt("\n\tPlease enter a valid config");
			initializeServer();
		}
		
		if (port <= 0||players <= 0||lives <= 0||port > 6000||players > 10) {
			userPrompt("Please enter a valid config", 1000);
			initializeServer();
		}

		userPrompt("\n\tBEEP-BEEP, BOP-BOP  ...");
		userPrompt("\tInitializing server ...");
		clearScreen();
		out.println("\n\tYour host-name (IP): " + getIP());
		out.println("\n\tAwaiting connections ...");
		
		t = new Thread(new Server(port, players, lives));
		t.start();
	}


	private static void connectToServer() {
		String host, username; int port; 
		clearScreen();
		try {
			out.print("\n\tEnter a host name: ");
			host = getUserStr();
			out.print("\n\tEnter a port number: ");
			port = in.nextInt();
			out.print("\n\tEnter a username: ");
			username = getUserStr();

			client = new Client(host, port, username);
		} catch (InputMismatchException e) {
			userPrompt("\n\n\tInput mismatch motherf****r");
			connectToServer();
		}
			
		if (!client.isConnected()) {
			userPrompt("\n\tInvalid host or port");
			connectToServer();
		}
	}

	
	private static String getIP() {
		String IP = null;
		try {
			IP = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {} 
		return IP;
	}


	private static void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
	}

	
	private static String getUserStr() {
		String input = "";
		do {
			input = in.next().trim();
		} while (input.isBlank() || input.isEmpty());
		return input;
	}

	
	private static void userPrompt(String msg, int time) {
		err.print(msg);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {}
	}
	

	private static void userPrompt(String msg) {
		err.print(msg);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {}
	}

	
	public static void main(String[] args) {
		new Game();
	}

}
