package refactor5;

import static java.lang.System.out;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

	Server server;
	Client client;

	public Game() {
		askUserConnectOrHostLoop();
	}

	private void askUserConnectOrHostLoop() {
		Scanner in = new Scanner(System.in);
		clearScreen();
		out.print("\t1] Connect to game\n\t2] Host Game\n> ");
		try {
			switch (in.nextInt()) {
			case 1:
				out.println("Joining game...");
				break;
			case 2:
				out.println("creating game ...");
				break;
			default:
				askUserConnectOrHostLoop();
			}
		} catch (InputMismatchException e) {
			askUserConnectOrHostLoop();
		}
		in.close();
	}
	
	public static void clearScreen() {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows")) Runtime.getRuntime().exec("cls");
			else Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Game();
	}

}
