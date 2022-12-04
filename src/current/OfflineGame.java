package current;

import static java.lang.System.out;

import java.util.ArrayList;
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
	private boolean newRound, believe;
	private Dice dice;
	private Player p;
	private int deceitN;

	public OfflineGame() {
		in = new Scanner(System.in);
		players = new ArrayList<>();
		dice = new Dice();
		newRound = true;
		deceitN = 0;

		configure();
		gameLoop();
	}
	

	private void gameLoop() {
		String beliefMsg = ", Do you believe ";
		clearScreen();

		while (players.size() > 1) {
			pIter = players.listIterator();
			while (pIter.hasNext()) {
				p = pIter.next();
				clearScreen();

				if (newRound) {
					dice.clearHistory();
					dice.shake();
					printStats(p, dice);
					dice.draw();
					newRound = false;
				} else { 
					believe = getYesOrNo("\n\t" + p.name + beliefMsg + deceitN + "? > ", in);

					handleTricked();
					handleWrong();
					handleOutwitted();

					if (!newRound) {
						dice.shake();
						printStats(p, dice);
						dice.draw();
					}
				}

				handleDeath("\t" + p.name + " DIED");
				if (handleWinner()) break;

				if (!newRound) {
					deceitN = getInt("\tWhat will you say?: ", in);
					handleInvalidDeceitNumber(deceitN);
					userPrompt("\n\t⏩⏩ 🤜PASS🤜 THE 🎲DIE🎲 ⏩⏩", WHITE, 2000);
				} else userPrompt("\n\t 🎉🎉 🥳 NEW ROUND 💯💯", WHITE, 2000);
			}
		}
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
		clearScreen("\n");

		for (int i = 0; i < playerN; i++)
			players.add(new Player(getStr(msg + (i+1) + ": ", in), lives));
			
		if (hasDuplicates(players)) {
			userPrompt(duplicateNamesMsg);
			players.clear();
			instantiatePlayers(playerN, lives);
		} 
	}

	
	/**
	 * When the player believes a lie, he has to go along with the lie
	 */
	private void handleTricked() {
		if (believe && dice.get() != deceitN) {
			userPrompt("\tYou were tricked, it was actually " + dice.get());
			dice.set(deceitN);
		}
	}
	
	/**
	 * When the player does not believe the previous player's sincere statement
	 */
	private void handleWrong() {
		if (!believe && dice.get() == deceitN) {
			looseLife("\tYou lost a life");
			newRound = true;
		}
	}
	
	/**
	 * When the player outwits his opponent because he doesnt believe the previous
	 * player's lie
	 */
	private void handleOutwitted() {
		if (!believe && deceitN != dice.get()) { 
			if (pIter.previousIndex() >= 1) { 
				pIter.previous();
				p = pIter.previous();
				looseLife("\t" + p.name + " lost a life\n", RED);
				p = pIter.next();
			} else {
				for (int i = 0; i < players.size() - 2; i++) 
					pIter.next(); 
				p = pIter.next(); 

				looseLife("\t" + p.name + " lost a life\n", RED);

				for (int i = players.size() - 1; i > 0; i--) 
					pIter.previous(); 
				p = pIter.previous(); 
			}
			newRound = true;
		}
	}
	
	private void handleInvalidDeceitNumber (int deceitN) {
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

	private void handleDeath (String msg) {
		if (p.lives == 0) {
			userPrompt(msg);
			pIter.remove();
			newRound = true;
		}
	}

	private void looseLife(String msg) {
		p.lives -= 1;
		userPrompt(msg);
	}
	
	private void looseLife(String msg, String color) {
		p.lives -= 1;
		userPrompt(msg, color);
	}

	public static void main(String[] args) {
		OfflineGame game = new OfflineGame();
	}
	
}
