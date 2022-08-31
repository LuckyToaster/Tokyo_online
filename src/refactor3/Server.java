package refactor3;

import static java.lang.System.out;
import static java.lang.Integer.toString;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Server {

	private ServerSocket ss;
	private List<Socket> sockets;
	private List<Player> players;
	private Dice dice;
	private int lives;
	private Socket s;
	private Player p;
	private BufferedReader r;

	public Server(int port, int connections, int lives) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
		}

		sockets = new ArrayList<>();
		players = new ArrayList<>();
		dice = new Dice();

		awaitUserConnections(connections, lives);
		awaitKeyPress();
		doShitInALoop();

	}

	private void doShitInALoop() {
		Iterator<Socket> socketsIter;
		Iterator<Player> playersIter;
		int count = 1;
		boolean newRound = true;

		while (sockets.size() > 1) {
			socketsIter = sockets.listIterator();
			playersIter = players.listIterator();

			while (socketsIter.hasNext()) {
				s = (Socket) socketsIter.next();
				p = (Player) playersIter.next();

				if (sockets.size() == 1) {
					send(s, "YOU WIN, CONGRATS!");
					broadcast(p.getName().concat(" WINS !!"));
					break;
				} else {
					dice.shake();

					if (dice.get() < dice.getPrev() && dice.historySize() > 1)
						p.loseLife();

					if (p.getLives() == 0) {
						send(s, "😂 YOU DIED 😂, here have an L");
						broadcast(p.getName().concat(" died LOL"));
						socketsIter.remove();
						playersIter.remove();
					} else
						send(s, count + ") " + printStats(p));
					count++;
				}
			}
		}
	}

	private void awaitUserConnections(int connections, int lives) {
		try {
			for (int i = 0; i < connections; i++) {
				sockets.add(ss.accept());
				s = sockets.get(sockets.size() - 1);
				r = new BufferedReader(new InputStreamReader(s.getInputStream()));
				players.add(new Player(r.readLine(), lives));
			}
		} catch (IOException e) {
			closeServerSocket();
		}
	}

	private void closeServerSocket() {
		if (ss != null)
			try {
				ss.close();
			} catch (IOException e) {
			}
	}

	private void send(Socket s, String msg) {
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write(msg);
			w.newLine();
			w.flush();
		} catch (IOException e) {
		}
	}

	// this method doesn't fucking work, gotta make the communication asynchronous
	private void broadcast(String msg) {
		BufferedWriter w;
		for (Socket s : sockets) {
			try {
				w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				w.write(msg);
				w.newLine();
				w.flush();
			} catch (IOException e) {
			}
		}
	}

	private String readLine(Socket s) {
		String msg = null;
		BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			msg = r.readLine();
			r.close();
		} catch (IOException e) {
		}
		return msg;
	}

	private String printStats(Player player) {
		return "\t🐵 ".concat(player.getName())
				.concat("   ⏪ ")
				.concat(dice.getPrev() + "   ")
				.concat("😂 ")
				.concat((dice.get() == 21 ? "TOKYO" : dice.get()) + "   ")
				.concat(player.getLives() + " ❤️");
	}

	private void awaitKeyPress() {
		Scanner in = new Scanner(System.in);
		out.print("Press Enter to start");
		in.nextLine();
		in.close();
	}

	public static void main(String[] args) {
		new Server(5500, 3, 3);
	}

}
