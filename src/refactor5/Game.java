package refactor5;

import static java.lang.System.out;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

	public Server server;
	public static Client client;
	public static Thread t;
	public static Scanner in;
	String os;

	public Game() {
		os = System.getProperty("os.name");
		in = new Scanner(System.in);

		askUserConnectOrHostLoop2();

		in.close();
	}


	private void askUserConnectOrHostLoop() {
		clearScreen();
		out.print("\t1] Connect to game\n\t2] Host Game\n> ");
		try {
			switch (in.nextInt()) {
			case 1:
				connectToServer();
				out.println("Joining game...");
				break;
			case 2:
				initializeServer();
				out.println("Initializing game server ...");
				out.println("Awaiting user connections ...");
				break;
			default:
				askUserConnectOrHostLoop();
			}
		} catch (InputMismatchException e) {
			askUserConnectOrHostLoop();
		}
	}


	private void askUserConnectOrHostLoop2() {
		int choice = 0;
		try {
			do {
				clearScreen();

				out.print("1] Connect to game\n2] Host Game\n> ");
				choice = Integer.valueOf(in.next()).intValue();

				if (choice == 1) {
					connectToServer();
					out.println("Joining game...");
				} else if (choice == 2) {
					initializeServer();
					out.println("Initializing game server ...");
					out.println("Awaiting user connections ...");
				}
			} while (choice != 1 || choice != 2);
		} catch (NumberFormatException e) {
			askUserConnectOrHostLoop2();
		}
	}


	// ensure no breakage and no infinite loops
	private static void initializeServer() {
		int port, players, lives;
		try {
			clearScreen();

			out.print("Enter a port number: ");
			port = in.nextInt();
			out.print("Enter number of players: ");
			players = in.nextInt();
			out.print("Enter number of lives: ");
			lives = in.nextInt();

			t = new Thread(new Server(port, players, lives));
			t.start();
		} catch (InputMismatchException e) {
			in.reset(); // hmm might remove
			initializeServer();
		}
	}


	private static void connectToServer() {
		String host;
		int port;
		try {
			clearScreen();

			out.print("Enter a host name: ");
			host = in.next();
			out.print("Enter a port number: ");
			port = in.nextInt();

			client = new Client(host, port);
			
			if (!client.isConnected()) 
				connectToServer();
		} catch (InputMismatchException e) {
			connectToServer();
		}
	}

	@Deprecated
	public static void clear() {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows"))
				new ProcessBuilder("cls.exe").inheritIO().start().waitFor();
			else
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			// Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void main(String[] args) {
		new Game();
	}

}
