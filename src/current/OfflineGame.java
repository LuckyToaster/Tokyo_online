package current;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import static current.Util.clearScreen;
import static current.Util.userPrompt;
import static current.Util.getStr;
import static current.Util.hasDuplicates;
import static current.Util.*;

public class OfflineGame {
	
	public static Scanner in;
	private List<Player> players;
	private ListIterator<Player> pIter;
	private Dice dice;
	private boolean newRound, believe;
	private Player p;
	private int deceitN;

	public OfflineGame() {
		players = new ArrayList<>();
		dice = new Dice();
		in = new Scanner(System.in);
		newRound = true;
		deceitN = 0;

		configure();
		gameLoop();
	}

	
	/**
	 * Game configuration screen
	 */
	private void configure() {
		int playerN = 0, lives = 0;
		clearScreen();

		try {
			out.print("\n\n\tEnter number of players: ");
			playerN = Integer.parseInt(in.next());
			out.print("\tEnter number of lives: ");
			lives = Integer.parseInt(in.next());
		} catch (NumberFormatException e) {
			userPrompt(INVALID_CONFIG_MSG);
			configure();
		}

		if (playerN >= 0 && lives >= 0) 
			instantiatePlayers(playerN, lives);
		else {
			userPrompt(INVALID_CONFIG_MSG);
			configure();
		}
	}

	/**
	 * Player instantiation screen 
	 * @param playerN
	 * @param lives
	 */
	private void instantiatePlayers(int playerN, int lives) {
		String msg = "\tEnter a name for player ",
		duplicateNamesMsg = "\n\tPlease enter a different name for each player";
		clearScreen("\n\n");

		for (int i = 0; i < playerN; i++)
			players.add(new Player(getStr(msg + (i+1) + ": ", in), lives));
			
		if (hasDuplicates(players)) {
			userPrompt(duplicateNamesMsg);
			players.clear();
			instantiatePlayers(playerN, lives);
		} 
	}
	
	
	private void gameLoop() {
		String beliefMsg = ", Do you believe ";
		clearScreen();

		while (!handleWinner()) {
			pIter = players.listIterator();
			while (pIter.hasNext()) {
				p = pIter.next();
				clearScreen();

				if (newRound) newRound();
				else believe = getYesOrNo("\n\t" + p.name + beliefMsg + deceitN + "? > ", in);

				handleTricked();
				handleWrong();
				//handleOutwitted();

				if (!newRound) normalRound();

				deceitN = getInt("\tWhat will you say?: ", in);
				handleInvalidDeceitNumber(p, deceitN);
				handleDeath(p, "\t YOU DIED");
				userPrompt("\n\t⏩⏩ 🤜PASS🤜 THE 🎲DIE🎲 ⏩⏩", WHITE, 1000);
			}
		}
	}

	
	private void looseLife(Player p, String msg) {
		p.lives -= 1;
		userPrompt(msg);
	}
	
	
	private void looseLife(Player p, String msg, String color) {
		p.lives -= 1;
		userPrompt(msg, color);
	}

	
	private void handleDeath (Player p, String msg) {
		if (p.lives == 0) {
			userPrompt(msg);
			pIter.remove();
			newRound = true;
		}
	}

	
	private void handleInvalidDeceitNumber (Player p, int deceitN) {
		if (!Dice.isValid(deceitN) || Dice.calcVal(deceitN) < dice.getVal() ) { 
			userPrompt("\n\tThat number does not exist! 🤣🤣 ... ");
			userPrompt("\n\t... -1 life\n", 300);
			p.lives -= 1;
			newRound = true;
		} 
	}
	

	private boolean handleWinner() {
		if (players.size() == 1) {
			out.println("\n\n\t" + players.get(0).name + " 🗿 (chad) iS THE WINNER! 🎉🎉");
			return true;
		} else return false;
	}
	
	
	// OK
	private void newRound() {
		dice.clearHistory();
		dice.shake();
		printStats(p, dice);
		dice.draw();
		newRound = false;
	}
	
	
	private void normalRound() {
		dice.shake();
		printStats(p, dice);
		dice.draw();
	}

	
	// OK
	private void handleTricked() {
		if (believe && dice.get() != deceitN) {
			userPrompt("\n\tYou were tricked, it was actually " + dice.get());
			dice.set(deceitN);
		}
	}
	
	// OK
	private void handleWrong() {
		if (!believe && dice.get() == deceitN) { 
			looseLife(p, "\n\tYou lost a life");
			handleDeath(p, " ... and you DIED\n");
		}
	}
	
	
	private void handleOutwitted() {
		if (!believe && deceitN != dice.get()) { // OUTWITTED
			handleDeath(p, "... and DIED\n");
			if (pIter.previousIndex() >= 1) { // try 2 or whatever tf
				pIter.previous();
				p = pIter.previous();
				looseLife(p, "\n\t" + p.name + " lost a life", WHITE);
				handleDeath(p, "... and DIED\n");
				handleWinner();
				pIter.next();
				p = pIter.next();
			} else {
				for (int i = 0; i < players.size() - 1; i++)
					pIter.next();
				p = pIter.next();
				looseLife(p, "\n\t" + p.name + " lost a life", WHITE);
				handleDeath(p, "... and DIED\n");
				for (int i = players.size() - 1; i > 0; i--) // >= ??
					pIter.previous();
				p = pIter.previous();
			}
			newRound = true;
		}
	}
	

	public static void main(String[] args) {
		OfflineGame game = new OfflineGame();

		//System.out.println("😂");
		List<Integer> l = Arrays.asList(0,1,2,3,4);
		ListIterator<Integer> li;
		int c = 0;

		/*
		//while (c < 3) {
			li = l.listIterator();
			for (int i = 0; i < l.size(); i++)
				System.out.println(li.next());
			//c++;
		//}
		
		li = l.listIterator();
		System.out.println(li.next());
		System.out.println(li.next());
		System.out.println(li.previous());
		System.out.println(li.previous());
		*/
	}
	
}
