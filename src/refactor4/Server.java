package refactor4;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Server {
	
	private Dice dice;
	private ServerSocket ss;
	private DataInputStream r;
	private DataOutputStream w;
	private List<Player> players;
	private ListIterator<Player> playersIter;
	private String deceitMsg, answer2Deceit;

	public Server(int port, int connections, int lives) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}
		
		players = new ArrayList<>();
		dice = new Dice();
		
		awaitUserConnections(connections, lives);
		awaitKeyPress();
		try {
			gameLoop();
		} catch (IOException e) {}
	}

	
	private void gameLoop() throws IOException {
		boolean newRound = true;
		Player p;

		while (players.size() > 1) {
			playersIter = players.listIterator();

			while (playersIter.hasNext()) {
				p = playersIter.next();
			
				// check for winner
				if (players.size() == 1) {
					send(p.s, 3);
					send(p.s, "YOU WIN, CONGRATS!");
					broadcast(p.name.concat(" WINS !!"));
					p.s.close();
				}
				
				if (newRound) {
					dice.shake();
					send(p.s, 1);
					send(p.s, printStats(p) + dice.getDrawing());
					deceitMsg = readUTF(p.s);
					newRound = false;
				} else {
					send(p.s, 2);
					send(p.s, deceitMsg);
					answer2Deceit = readUTF(p.s);
					
					// handle prev sincere current skeptical
					if (parseInt(deceitMsg) == dice.get() && answer2Deceit.equals("n")) {
						p.lives -= 1;
						newRound = true;
						dice.clearHistory();
					}
					
					dice.shake();
					send(p.s, printStats(p) + dice.getDrawing());
					deceitMsg = readUTF(p.s);

					/* after case 2, case 3 in case we wanna send extra info
					 * like someone lost a life or to broadcast a death
					 */
					broadcast(3);
					broadcast(p.name + " " + p.lives + " lives remaining");
					
					// handle if dead
					if (p.lives == 0) {
						broadcast(3);
						broadcast("Player " + p.name + " died");

						send(p.s, 3);
						send(p.s, "YOU DIED LOL! Here, have an 'L'");

						p.s.close();
						playersIter.remove();
					}
				}
			}
		}
	} 
	
	
	private void broadcast(String msg) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeUTF(msg);
				w.flush();
			} catch (IOException e) {}
		}
	}
	
	
	private void broadcast(int n) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeInt(n);
				w.flush();
			} catch (IOException e) {}
		}
	}
	
	
	private void send(Socket s, String msg) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {}
	}
	
	
	private void send(Socket s, int n) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.write(n);
			w.flush();
		} catch (IOException e) {}
	}
	
	
	private String readUTF(Socket s) {
		String msg = null;
		try {
			r = new DataInputStream(s.getInputStream());
			msg = r.readUTF();
		} catch (IOException e) {}
		return msg;
	}
	
	
	private void closeServerSocket() {
		if (ss != null) 
			try {
				ss.close();
			} catch (IOException e) { 
				closeServerSocket();
			}
	}

	
	private void awaitUserConnections(int connections, int lives) {
		Socket s;
		try {
			for (int i = 0; i < connections; i++) {
				s = ss.accept();
				r = new DataInputStream(s.getInputStream());
				players.add(new Player(s, r.readUTF(), lives));
			}
		} catch (IOException e) {
			closeServerSocket();
		}
	}

	
	private void awaitKeyPress() {
		Scanner in = new Scanner(System.in);
		out.println("Press enter to start");
		in.nextLine();
		in.close();
	}
	
	
	private String printStats(Player player) {
		return "\t🐵 ".concat(player.name)
				.concat("   ⏪ ")
				.concat(dice.getPrev() + "   ")
				.concat("😂 ")
				.concat((dice.get() == 21 ? "TOKYO" : dice.get()) + "   ")
				.concat(player.lives + " ❤️");
	}
	
	
	public static void main(String[] args) {
		Server server = new Server(5500, 3, 3);
	}

}
