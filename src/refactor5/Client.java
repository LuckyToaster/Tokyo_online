package refactor5;

import static java.lang.System.out;

import java.io.IOException;
import java.util.Scanner;


public class Client {
	
	private ClientHandler ch;
	private Scanner in;
	private String os;
	
	public Client(String host, int port) {
		in = new Scanner(System.in);
		ch = new ClientHandler(host, port);
		os = System.getProperty("os.name");

		getUsername();
		talkToServer();
		ch.closeBridges();
		in.close();
	}
	
	
	private void talkToServer() {
		while (ch.isConnected()) {
			switch (ch.readInt()) {
			case 1:
				out.println(ch.read());
				out.print("\tWhat'd you get? > ");
				ch.send(in.next());
				clearScreen();
				break;
			case 2:
				out.println("\t".concat(ch.read()));

				String msg = null;
				do {
					out.print("\tYay or Nay? (y/n) > ");
					msg = in.next().trim().toLowerCase();
				} while (!msg.equals("y") && !msg.equals("n"));

				ch.send(msg);

				out.println(ch.read());
				out.print("\tWhat'd you get? > ");
				ch.send(in.next());

				clearScreen();
				break;
			case 3:
				out.println(ch.read());
				break;
			default:
				break;
			}
		} out.println("YOU ARE DEAD, here have an 'L'!");
	}
	
	
	private void getUsername() {
		out.print("Enter a username: ");
		ch.send(in.next().trim());
	}
	
	
	private void clearScreen() {
		try {
			if (os.contains("Windows")) Runtime.getRuntime().exec("cls");
			else Runtime.getRuntime().exec("clear");
		} catch (IOException e) {}
	}

	
	public static void main(String[] args) {
		new Client("localhost", 5500);
	}

}
