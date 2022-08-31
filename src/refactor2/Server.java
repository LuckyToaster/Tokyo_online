package refactor2;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	private Dice dice;
	private ServerSocket ss;
	private List<Socket> sockets;
	private List<Player> players;
	private ListIterator<Player> playersIter;
	private ListIterator<Socket> socketsIter;
	private Scanner in;
	private int lives, playerN;
	private String deceitMsg, answer2Deceit;
	private boolean newRound, finished;
	private Player p;
	private Socket s; 

	public Server(int port, int lives, int playerN) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}

		this.lives = lives;
		this.playerN = playerN;

		deceitMsg = null; 
		answer2Deceit = null;
		newRound = true;
		finished = false;
		dice = new Dice();
		in = new Scanner(System.in);
		sockets = new ArrayList<>();
		players = new ArrayList<>();

		awaitUserConnections();
		awaitKeyPress();
		startGame();
	}


	private void awaitUserConnections() {
		BufferedReader r;
		Socket s;
		try {
			for (int i = 0; i < playerN; i++) {
				sockets.add(ss.accept());
				s = sockets.get(sockets.size() - 1);
				r = new BufferedReader(new InputStreamReader(s.getInputStream()));
				players.add(new Player(r.readLine(), lives));
			}
		} catch (IOException e) {
			closeServerSocket();
		}
	}


	private void startGame() {
		out.println("Game started");

		while (!finished) {
			playersIter = players.listIterator();
			socketsIter = sockets.listIterator();

			while (playersIter.hasNext()) {
				p = playersIter.next();
				s = socketsIter.next();

				if (finished = checkIfWinner(p)) break;

				if (newRound) {
					send(s, 1); // tell client this is case 1 in switch statement
					doTheUsual(s, p);
					newRound = false;
				} else {
					send(s, 2);
					send(s, deceitMsg);
					answer2Deceit = readLine(s);
					newRound = prevSincereCurrentSkeptical(p);
					doTheUsual(s, p);
					newRound = prevLiedCurrentSkeptical(p, playersIter, socketsIter);
				} handleIfDead(p, playersIter, socketsIter);
			}
		}
	}


	private void broadcast(String msg) {
		BufferedWriter w;
		for (Socket s : sockets) {
			try {
				w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				w.write(msg);
				w.newLine();
				w.flush();
			} catch (IOException e) {}
		}
	}


	private void send(Socket s, String msg) {
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write(msg);
			w.newLine();
			w.flush();
		} catch (IOException e) {}
	}


	private void send(Socket s, int n) {
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write(n);
			w.newLine();
			w.flush();
		} catch (IOException e) {}
	}


	private String readLine(Socket s) {
		String msg = null;
		BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			msg = r.readLine();
		} catch (IOException e) {}
		return msg;
	}
	
	
	private void closeServerSocket() {
		if (ss != null)
			try {
				ss.close();
			} catch (IOException e) {}
	}


	private String printStats(Player player) {
		return "\t🐵 ".concat(player.getName())
				.concat("   ⏪ ")
				.concat(dice.getPrev() + "   ")
				.concat("😂 ")
				.concat((dice.get() == 21 ? "TOKYO" : dice.get()) + "   ")
				.concat(player.getLives() + " ❤️");
	}


	private void doTheUsual(Socket s, Player p) {
		dice.shake();
		send(s, printStats(p).concat(dice.getDrawing()));
		deceitMsg = readLine(s);
	}
	
	
	private boolean prevSincereCurrentSkeptical(Player p) {
		if (parseInt(deceitMsg) == dice.get() && answer2Deceit.equals("n")) {
			p.loseLife();
			return true;
		} else return false;
	}
	
	
	// this also checks and handles if the player has died 
	private boolean prevLiedCurrentSkeptical(Player p, ListIterator<Player> players, ListIterator<Socket> sockets) {
		if (parseInt(deceitMsg) != dice.getPrev() && answer2Deceit.equals("n")) {
			if (playersIter.hasPrevious() && socketsIter.hasPrevious()) {
				s = socketsIter.previous();
				p = playersIter.previous();

				p.loseLife();
				broadcast(p.getName().concat(" has lost a life!"));

				if (p.getLives() == 0) {
					broadcast("Player " + p.getName() + " dies!");
					send(s, "YOU DIED LOL! Here, have an 'L'");
					playersIter.remove();
					socketsIter.remove();
				}

				s = socketsIter.next();
				p = playersIter.next();
			} return true;
		} else return false;
	}
	
	
	private boolean checkIfWinner(Player p) {
		if (players.size() == 1) {
			broadcast("Player " + p.getName() + " wins!");
			return true;
		} else return false; 	
	}

	
	private void handleIfDead(Player p, ListIterator<Player> players, ListIterator<Socket> sockets) {
		if (p.getLives() == 0) {
			broadcast("Player " + p.getName() + " dies!");
			send(s, "YOU DIED LOL! Here, have an 'L'");
			players.remove();
			sockets.remove();
		}
	}


	private void awaitKeyPress() {
		out.print("Press Enter to start");
		in.nextLine();
	}


	public static void main(String[] args) {
		new Server(5500, 3, 2);
	}


}

/*
 * while (!finished) { playersIter = players.listIterator(); socketsIter =
 * sockets.listIterator();
 * 
 * while (playersIter.hasNext()) { player = playersIter.next(); socket =
 * socketsIter.next();
 * 
 * // check if there is a winner and end the game if (players.size() == 1) {
 * broadcast("Player " + player.getName() + " wins!"); finished = true; break; }
 * 
 * dice.shake();
 * 
 * if (dice.getHistorySize() != 1) { prevGTNow = dice.get() < dice.getPrev();
 * prev21NowNot21 = dice.get() != 21 && dice.getPrev() == 21;
 * 
 * if (!freshStart && (prevGTNow || prev21NowNot21)) {
 * broadcast(player.getName().concat(" has lost a life!")); player.loseLife();
 * freshStart = true; } }
 * 
 * // check if player died if (player.getLives() == 0) { broadcast("Player " +
 * player.getName() + " dies!"); send(socket,
 * "YOU DIED LOL! Here, have an 'L'"); playersIter.remove();
 * socketsIter.remove(); continue; }
 * 
 * // print stats and dice throw send(socket,
 * printStats(player).concat(dice.getDrawing()) );
 * 
 * // get msg from client and send it back to client clientMsg =
 * readLine(socket); send(socket, "🤡 ".concat(clientMsg));
 * 
 * if (parseInt(clientMsg) == dice.get() && readLine(socket).equals("n")) {
 * broadcast(player.getName().concat(" got tricked + lost a life! ... LOL"));
 * player.loseLife(); freshStart = true; }
 * 
 * // check if player died again if (player.getLives() == 0) {
 * broadcast("Player " + player.getName() + " dies!"); send(socket,
 * "YOU DIED LOL! Here, have an 'L'"); playersIter.remove();
 * socketsIter.remove(); continue; } } }
 */
