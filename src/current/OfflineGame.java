package current;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


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
		Util.clearScreen();

		try {
			out.print("\n\n\tEnter number of players: ");
			playerN = Integer.parseInt(in.next());
			out.print("\tEnter number of lives: ");
			lives = Integer.parseInt(in.next());
		} catch (NumberFormatException e) {
			Util.userPrompt(Util.INVALID_CONFIG_MSG);
			configure();
		}

		if (playerN >= 0 && lives >= 0) 
			instantiatePlayers(playerN, lives);
		else {
			Util.userPrompt(Util.INVALID_CONFIG_MSG);
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

		Util.clearScreen("\n\n");
		for (int i = 0; i < playerN; i++)
			players.add(new Player(Util.getStr(msg + (i+1) + ": ", in), lives));
			
		if (Util.hasDuplicates(players)) {
			Util.userPrompt(duplicateNamesMsg);
			players.clear();
			instantiatePlayers(playerN, lives);
		} 
	}
	
	
	private void gameLoop() {
		Player p;
		boolean finished = false, newRound = true;

		
		while (players.size() > 1 && !finished) {
			pIter = players.listIterator();
			
			while (pIter.hasNext() && !finished) {
				p = pIter.next();
				
				if (newRound) {
					dice.shake();
					printStats(p);
					out.println(dice.getDrawing());
				}
				
				newRound = false;
				finished = true;
			}
		}
	}
	
	
	private void printStats(Player player) {
		out.println( "🐵 ".concat(player.name)
				.concat("⏪ ")
				.concat(dice.getPrev() + "   ")
				.concat("")
				.concat((dice.get() == 21 ? "東京 TOKYO 東京" : dice.get()) + "   ")
				.concat(player.lives + "❤️"));
	}

	
	private void test() {
		for (Player p : players)
			out.println("name: " + p.name + ", lives: " + p.lives);
	}

	
	public static void main(String[] args) {
		OfflineGame game = new OfflineGame();
	}
}
