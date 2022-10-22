package refactor6;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Server implements Runnable {
	
	private Dice dice;
	private ServerHandler sh;
	private List<Player> players;
	private ListIterator<Player> playersIter;
	private String deceitMsg, answer;
	private boolean newRound, firstDeath, finished;


	public Server(int port, int connections, int lives) {
		sh = new ServerHandler(port);
		players = sh.awaitConnections(connections, lives);
		dice = new Dice();

		newRound = true;
		firstDeath = true;
		finished = false;

		//awaitKeyPress();
		try {
			gameLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void gameLoop() throws IOException {
		Player p;

		while (players.size() > 1 && !finished) {
			playersIter = players.listIterator();

			while (playersIter.hasNext() && !finished) {
				p = playersIter.next();
				
				if (newRound) {
					sh.send(p.s, 1);
					throwDiceSendStatsGetResponse(p);
					newRound = false;
				} else {
					sh.send(p.s, 2);
					sendDeceitMsgGetAnswer(p);

					if (prevSincereAndLuckyCurrentSus() || currentMessedUp()) {
						loseLife(p);
					} else if (prevLiedOrUnluckyCurrentSus()) {
						if (playersIter.hasPrevious()) {
							p = playersIter.previous();
							loseLife(p);
							if (p.lives == 0) handleDeath(p, players, playersIter);
							p = playersIter.next();
						}
					}
					
					/* check if current was deceived and set the result of dice
					*  to the deceitMsg 
					*/

					if (p.lives == 0) 
						handleDeath(p, players, playersIter);

					throwDiceSendStatsGetResponse(p);

					if (players.size() == 1) {
						handleWinner(p);
						break;
					}
				}
			}
		}
	} 
	
	
	private void throwDiceSendStatsGetResponse(Player p) {
		dice.shake();
		sh.send(p.s, printStats(p) + dice.getDrawing());
		this.deceitMsg = sh.read(p.s);
	}
	
	
	private void sendDeceitMsgGetAnswer(Player p) {
		sh.send(p.s, "🤡 " + deceitMsg);
		this.answer = sh.read(p.s);
	}
	

	private void loseLife(Player p) {
		p.lives -= 1;
		this.newRound = true;
		dice.clearHistory();
	}
	
	
	private boolean prevSincereAndLuckyCurrentSus() {
		boolean didNotLie = parseInt(deceitMsg) == dice.get(),
		lucky = dice.getVal() >= dice.getPrevVal(),
		currentSus = answer.equals("n");

		return didNotLie && lucky && currentSus;
	}
	
	
	private boolean prevLiedOrUnluckyCurrentSus() {
		boolean lied = parseInt(deceitMsg) != dice.get(),
		unlucky = dice.getVal() < dice.getPrevVal(),
		currentSus = answer.equals("n");

		return (lied || unlucky) && currentSus;
	}
	
	
	private boolean currentMessedUp() {
		// add out of range, not a number ... etc
		return parseInt(deceitMsg) < dice.getPrevVal();
	}
	
	
	private boolean currentDeceived() {
		if ((parseInt(deceitMsg) != dice.get()) && answer.equals("y")) 
			return true;
		else return false;
	}

	
	private void handleDeath(Player p, List<Player> players, ListIterator<Player> iter) {
		if (firstDeath) {
			sh.broadcast(players, 3);
			sh.broadcast(players, "Player " + p.name + " died");
			sh.send(p.s, 3);
			sh.send(p.s, "FIRST ONE TO DIE,\n 🌈LGBT PRIDE 🏳️‍🌈🏳️‍🌈"); // this should be on client side
			firstDeath = false;
		} else {
			sh.broadcast(players, 3);
			sh.broadcast(players, "Player " + p.name + " died");
			sh.send(p.s, 3);
			sh.send(p.s, "YOU DIED LOL! Here, have an 'L'");
		}

		try {
			p.s.close();
		} catch (IOException e) {}

		iter.remove(); 
	}
	

	private void handleWinner(Player p) {
		sh.send(p.s, 3);
		sh.send(p.s, "YOU WON, CONGRATS! 🍀✨🎉🎉");
		sh.broadcast(players, 3);
		sh.broadcast(players, p.name + " WINS! 🗿 ");
		try {
			p.s.close();
		} catch (IOException e) {}
		
		finished = true;
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
				.concat(dice.prev() + "   ")
				.concat("😂 ")
				.concat((dice.get() == 21 ? "TOKYO" : dice.get()) + "   ")
				.concat(player.lives + " ❤️");
	}
	
	
	public static void main(String[] args) {
		Server server = new Server(5500, 2, 2);
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
