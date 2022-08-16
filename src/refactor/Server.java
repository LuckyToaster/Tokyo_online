package refactor;
import static java.lang.System.out;
import static java.lang.Integer.parseInt;

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

	private Dice dice;
	private ServerSocket ss;
    private List<Socket> sockets;
	private List<Player> players;
	private Scanner in;
	private int lives, playerN;
	private boolean freshStart, finished;

	public Server(int port, int lives, int playerN) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}

		this.lives = lives;
		this.playerN = playerN;

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
		BufferedReader r; Socket s;
		try {
			for (int i = 0; i < playerN; i++) {
				sockets.add(ss.accept());
				s = sockets.get(sockets.size() -1);
				r = new BufferedReader(new InputStreamReader(s.getInputStream()));
				players.add(new Player(r.readLine(), lives));
			}
		} catch (IOException e) {
			closeServerSocket();
		}
	}


	private void startGame() {
		boolean prevGTNow, prev21NowNot21, freshStart = false;
		String response, clientMsg = null;
		Iterator<Player> playersIter;
		Iterator<Socket> socketsIter;
		Player player; 
		Socket socket;

		out.println("Game started");

		while (!finished) {
			playersIter = players.listIterator();	
			socketsIter = sockets.listIterator();

			while (playersIter.hasNext()) {
				player = playersIter.next();
				socket = socketsIter.next();
			
				if (freshStart) {
					send(socket, 1); // tell client this is case 1 in switch statement
					dice.shake();
					send(socket, printStats(player).concat(dice.getDrawing()) );
					clientMsg = readLine(socket);
				}

				else clientMsg = readLine(socket);
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
		String clientMsg = null;
		BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			clientMsg = r.readLine();
		} catch (IOException e) {}
		return clientMsg;
	}


	private void awaitKeyPress() {
		out.print("Press Enter to start");
		in.nextLine();
	}


	public static void main(String[] args) {
		new Server(5500, 3, 3);
	}
	
	/*
	 * while (!finished) {
			playersIter = players.listIterator();	
			socketsIter = sockets.listIterator();
			
			while (playersIter.hasNext()) {
				player = playersIter.next();
				socket = socketsIter.next();
			
				// check if there is a winner and end the game 
				if (players.size() == 1) {
					broadcast("Player " + player.getName() + " wins!");
					finished = true;
					break;
				}

				dice.shake();

				if (dice.getHistorySize() != 1) {
					prevGTNow = dice.get() < dice.getPrev();
					prev21NowNot21 = dice.get() != 21 && dice.getPrev() == 21;

					if (!freshStart && (prevGTNow || prev21NowNot21)) {
						broadcast(player.getName().concat(" has lost a life!"));
						player.loseLife();
						freshStart = true;
					}
				}

				// check if player died
				if (player.getLives() == 0) {
					broadcast("Player " + player.getName() + " dies!");
					send(socket, "YOU DIED LOL! Here, have an 'L'");
					playersIter.remove();
					socketsIter.remove();
					continue;
				}

				// print stats and dice throw 
				send(socket, printStats(player).concat(dice.getDrawing()) );

				// get msg from client and send it back to client
				clientMsg = readLine(socket);
				send(socket, "🤡 ".concat(clientMsg));
					
				if (parseInt(clientMsg) == dice.get() && readLine(socket).equals("n")) {
					broadcast(player.getName().concat(" got tricked + lost a life! ... LOL"));
					player.loseLife();
					freshStart = true;
				}

				// check if player died again
				if (player.getLives() == 0) {
					broadcast("Player " + player.getName() + " dies!");
					send(socket, "YOU DIED LOL! Here, have an 'L'");
					playersIter.remove();
					socketsIter.remove();
					continue;
				}
			}
		}
	 */

}
