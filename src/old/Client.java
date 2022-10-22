package old;

import static java.lang.System.out;
import java.util.Scanner;


public class Client {
	
	private ClientHandler ch;
	private Scanner in;
	
	public Client(String host, int port, String username) {
		in = new Scanner(System.in);
		ch = new ClientHandler(host, port);

		ch.send(username);

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

				break;
			case 3:
				out.println(ch.read());
				break;
			default:
				break;
			}
		} out.println("YOU ARE DEAD, here have an 'L'!");
	}
	
	
	public boolean isConnected() {
		return ch.isConnected();
	}
	
	
	public static void main(String[] args) {

	}

}
