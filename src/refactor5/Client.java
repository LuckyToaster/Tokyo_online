package refactor5;

import static java.lang.System.out;
import java.util.Scanner;


public class Client {
	
	private ClientHandler ch;
	private Scanner in;
	
	public Client(String host, int port) {
		in = new Scanner(System.in);
		ch = new ClientHandler(host, port);

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
				out.print("What'd you get? > ");
				ch.send(in.next());
				break;
			case 2:
				out.println(ch.read());

				String msg = null;
				do {
					out.print("Yay or Nay? (y/n) > ");
					msg = in.next().trim().toLowerCase();
				} while (!msg.equals("y") && !msg.equals("n"));

				ch.send(msg);

				out.println(ch.read());
				out.print("What'd you get? > ");
				ch.send(in.next());
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
	
	
	public static void main(String[] args) {
		new Client("localhost", 5500);
	}

}
