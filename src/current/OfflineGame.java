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
	private Dice dice;

	public OfflineGame() {
		players = new ArrayList<>();
		dice = new Dice();
		in = new Scanner(System.in);
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
		Player p;
		int deceitN = 0;
		String ready = "\n\tIs that you, ",
		beliefMsg = ", Do you believe ";
		boolean newRound = true, believe;
		clearScreen();

		while (players.size() > 1) {
			pIter = players.listIterator();
			while (pIter.hasNext()) {
				p = pIter.next();
				
				if (players.size() == 1) break;
				clearScreen();

				if (newRound) {
					dice.clearHistory();
					dice.shake();
					printStats(p, dice);
					dice.printDrawing();
				} else {
					believe = getYesOrNo("\n\t" + p.name + beliefMsg + deceitN + "? > ", in);
					
					if (believe && dice.get() != deceitN) 
						dice.setPrev(deceitN);
					
					if (!believe && deceitN != dice.get()) {

						if (players.size() == 2 && pIter.hasNext())
							p = pIter.next();
						else {
							pIter.previous();
							p = pIter.previous();
						}
							
						p.lives -= 1;
						userPrompt("\n\t" + p.name + " lost a life", WHITE);

						if (p.lives == 0) {
							userPrompt(" ... and he DIED\n");
							pIter.remove();
							newRound = true;
							p = pIter.next(); // BUG HERE fix nosuchelement ex
							continue;
						} 
					}   

					if (!believe && dice.get() == deceitN) {
						p.lives -= 1;
						userPrompt("\n\tYou lost a life");

						if (p.lives == 0) {
							userPrompt(" ... and you DIED\n");
							pIter.remove();
							newRound = true;
							continue;
						}

						clearScreen();
						printStats(p, dice);
						dice.printDrawing();
						newRound = true;
					}
					
					dice.shake();
					printStats(p, dice);
					dice.printDrawing();
				}
				
				deceitN = getInt("\tWhat will you say?: ", in);
				
				if (!Dice.isValid(deceitN)) { // here add if made up value is worse than prev, also remove live
					userPrompt("\n\tThat number does not exist! 🤣🤣 ... ");
					userPrompt("\n\t... -1 life\n", 300);
					p.lives -= 1;
					newRound = true;
				} else newRound = false;
				
				if (p.lives == 0) {
					userPrompt("\tYOU DIED\n");
					pIter.remove();
					newRound = true;
				} else newRound = false;

				userPrompt("\n\t⏩⏩ 🤜PASS🤜 THE 🎲DIE🎲 ⏩⏩", WHITE, 1000);
				
			}
		} out.println("\n\n\t" + players.get(players.size()-1).name + " 🗿 (chad) iS THE WINNER! 🎉🎉");
		
	}

	
	public static void main(String[] args) {
		OfflineGame game = new OfflineGame();
		//System.out.println("😂");
	}
	
}
