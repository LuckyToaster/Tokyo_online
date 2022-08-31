package refactor5;

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
	private ServerHandler handler;
	private List<Player> players;
	private ListIterator<Player> playersIter;
	private String deceitMsg, answer2Deceit;

	public Server(int port, int connections, int lives) {
		handler = new ServerHandler(port);
		players = handler.awaitConnections(connections, lives);
		dice = new Dice();

		awaitKeyPress();
		try {
			gameLoop();
		} catch (IOException e) {}
	}

	
	private void gameLoop() throws IOException {
		boolean newRound = true, firstOneToDie = true;
		Player p;

		while (players.size() > 1) {
			playersIter = players.listIterator();

			while (playersIter.hasNext()) {
				p = playersIter.next();
			
				// check for winner
				if (players.size() == 1) {
					handler.send(p.s, 3);
					handler.send(p.s, "YOU WON, CONGRATS! 🍀✨🎉🎉");
					handler.broadcast(players, 3);
					handler.broadcast(players, p.name + " WINS! 🗿 ");
					p.s.close();
					break;
				}
				
				if (newRound) {
					dice.shake();
					handler.send(p.s, 1);
					handler.send(p.s, printStats(p) + dice.getDrawing());
					deceitMsg = handler.read(p.s);
					newRound = false;
				} else {
					handler.send(p.s, 2);
					handler.send(p.s, "🤡 " + deceitMsg);
					answer2Deceit = handler.read(p.s);
					
					// handle prev sincere current skeptical
					if (parseInt(deceitMsg) == dice.get() && answer2Deceit.equals("n")) {
						p.lives -= 1;
						newRound = true;
						dice.clearHistory();
					}
					
					// if current got a bad number and accidentally told the truth
					if (parseInt(deceitMsg) == dice.get() && dice.get() < dice.getPrev()) {
						p.lives -= 1;
						newRound = true;
						dice.clearHistory();
					}
						
					
					dice.shake();
					handler.send(p.s, printStats(p) + dice.getDrawing());
					deceitMsg = handler.read(p.s);
					
					// prev lied current skeptical
					if (parseInt(deceitMsg) != dice.getPrev() && answer2Deceit.equals("n")) {
						if (playersIter.hasPrevious()) {
							p = playersIter.previous();
							p.lives -= 1;

							if (p.lives == 0) {
								if (firstOneToDie) {
									handler.broadcast(players, 3);
									handler.broadcast(players, "Player " + p.name + " died");
									handler.send(p.s, 3);
									handler.send(p.s, "FIRST ONE TO DIE,\n 🌈LGBT PRIDE 🏳️‍🌈🏳️‍🌈");
									firstOneToDie = false;
								} else {
									handler.broadcast(players, 3);
									handler.broadcast(players, "Player " + p.name + " died");
									handler.send(p.s, 3);
									handler.send(p.s, "YOU DIED LOL! Here, have an 'L'");
								}
								p.s.close();
								playersIter.remove();
							}
						}
					} else 
					

					if (p.lives == 0) { // handle if dead
						if (firstOneToDie) {
							handler.broadcast(players, 3);
							handler.broadcast(players, "Player " + p.name + " died");
							handler.send(p.s, 3);
							handler.send(p.s, "FIRST ONE TO DIE,\n 🌈LGBT PRIDE 🏳️‍🌈🏳️‍🌈");
							firstOneToDie = false;
						} else {
							handler.broadcast(players, 3);
							handler.broadcast(players, "Player " + p.name + " died");
							handler.send(p.s, 3);
							handler.send(p.s, "YOU DIED LOL! Here, have an 'L'");
						}

						p.s.close();
						playersIter.remove();
					}
				}
			}
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
		Server server = new Server(5500, 2, 2);
	}

}
