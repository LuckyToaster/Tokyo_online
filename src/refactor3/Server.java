package refactor3;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Server {

	private ServerSocket ss;
	private Dice dice;
	private ListIterator<Socket> socketsIter;
	private ListIterator<Player> playersIter;
	private List<Socket> sockets;
	private List<Player> players;
	private Socket s;
	private Player p;
	private DataInputStream r;
	private DataOutputStream w;
	private String deceitMsg, answer2Deceit;
	private boolean newRound;


	public Server(int port, int connections, int lives) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}

		newRound = true;
		sockets = new ArrayList<>();
		players = new ArrayList<>();
		dice = new Dice();

		awaitUserConnections(connections, lives);
		awaitKeyPress();
		gameLoop();
	}

	private void gameLoop() {
		while (sockets.size() > 1) {
			socketsIter = sockets.listIterator();
			playersIter = players.listIterator();

			while (socketsIter.hasNext()) {
				s = (Socket) socketsIter.next();
				p = (Player) playersIter.next();

				if (handleWinner(s, p)) break;

				if (newRound) {
					send(s, 1);
					throwDiceSendStatsGetResponse(s, p);
					newRound = false;
				} else {
					send(s, 2);
					sendDeceitMsgGetAnswer(s);
					handlePrevSincereCurrentSkeptical(p);
					throwDiceSendStatsGetResponse(s, p);
					handleIfDead(p, s, playersIter, socketsIter);
				}
			}
		}
	}


	private void awaitUserConnections(int connections, int lives) {
		try {
			for (int i = 0; i < connections; i++) {
				sockets.add(ss.accept());
				s = sockets.get(sockets.size() - 1);
				r = new DataInputStream(s.getInputStream());
				players.add(new Player(r.readUTF(), lives));
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
	

	// how about broadcast to all except for one 
	private void broadcast(String msg) {
		for (Socket s : sockets) {
			try {
				w = new DataOutputStream(s.getOutputStream());
				w.writeUTF(msg);
				w.flush();
			} catch (IOException e) {}
		}
	}


	private String readLine(Socket s) {
		String msg = null;
		try {
			r = new DataInputStream(s.getInputStream());
			//while ((msg = r.readLine()) != null)
			msg = r.readUTF();
		} catch (IOException e) {}
		return msg;
	}


	private String printStats(Player player) {
		return "\t🐵 ".concat(player.name)
				.concat("   ⏪ ")
				.concat(dice.getPrev() + "   ")
				.concat("😂 ")
				.concat((dice.get() == 21 ? "TOKYO" : dice.get()) + "   ")
				.concat(player.lives + " ❤️");
	}


	private void awaitKeyPress() {
		Scanner in = new Scanner(System.in);
		out.print("Press Enter to start");
		in.nextLine();
		in.close();
	}
	
	
	private void throwDiceSendStatsGetResponse(Socket s, Player p) {
		dice.shake();
		send(s, printStats(p) + dice.getDrawing());
		deceitMsg = readLine(s);
	}
	
	
	private boolean handleWinner(Socket s, Player p) {
		if (sockets.size() == 1) {
			send(s, "YOU WIN, CONGRATS!");
			broadcast(p.name.concat(" WINS !!"));
			return true;
		} else return false;
	}
	
	
	private void handleIfDead(Player p, Socket s, ListIterator<Player> players, ListIterator<Socket> sockets) {
		if (p.lives == 0) {
			broadcast("Player " + p.name + " dies!");
			send(s, "YOU DIED LOL! Here, have an 'L'");
			try {
				s.close();
			} catch (IOException e) {}
			players.remove();
			sockets.remove();
		}
	}
	
	
	private void handlePrevSincereCurrentSkeptical(Player p) {
		if (parseInt(deceitMsg) == dice.get() && answer2Deceit.equals("n")) {
			broadcast(p.name + " " + p.lives + " remaining");
			p.lives -= 1;
			newRound = true;
		}
	}

	
	private void sendDeceitMsgGetAnswer(Socket s) {
		send(s, deceitMsg);
		answer2Deceit = readLine(s);
	}


	public static void main(String[] args) {
		new Server(5500, 3, 3);
	}

}
