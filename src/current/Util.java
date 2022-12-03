package current;

import static java.lang.System.err;
import static java.lang.System.out;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Util {
	
	public static final String INPUT_MISMATCH_MSG = "\n\tInput mismatch motherf*****r";
	public static final String INVALID_CONFIG_MSG = "\n\tPleaser enter a valid configuration";
	public static final String NOT_A_NUM_MSG =  "\n\tPlease enter a number\n\n";

	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";
	public static final String RESET = "\u001B[0m";
	
	public static final String FUNNY = "😂";
	public static final String PREV = "";
	public static final String TOKYO = "";
	public static final String MONKEY = "";
	public static final String HEART = "";
	public static final String POOP = "";
	public static final String CLOWN = "";
	public static final String PARTY = "🎉";
	public static final String HUNDRED_PERCENT = "💯";
	public static final String DEVIL = "U+1F608";
	public static final String SKULL = "U+2620";
	
	public static void userPrompt(String msg, int time) {
		err.print(RED + msg + RESET);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {}
	}

	public static void userPrompt(String msg) {
		err.print(RED + msg + RESET);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {}
	}
	
	public static void userPrompt(String msg, String color) {
		out.print(color + msg + RESET);
		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {}
	}

	public static void userPrompt(String msg, String color, int time) {
		out.print(color + msg + RESET);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {}
	}
	
	public static String getStr(String msg, Scanner in) {
		out.print(msg);
		return in.next().trim();
	}
	
	public static int getInt(String msg, Scanner in) {
		int n = 0;
		try {
			out.print(msg);
			n = Integer.valueOf(in.next().trim());
		} catch (NumberFormatException e) {
			Util.userPrompt(Util.NOT_A_NUM_MSG);
			getInt(msg, in);
		}
		return n;
	}
	
	
	public static boolean getYesOrNo(String msg, Scanner in) {
		String input = "";
		do {
			clearScreen();
			out.print(msg);
			input =  in.next().trim().toLowerCase();
		} while (!input.equals("yes") && !input.equals("no") && !input.equals("y") && !input.equals("n"));

		if (input.equals("yes") || input.equals("y")) 
			return true;
		else return false;
	}


	public static void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
	}

	
	public static void clearScreen(String msg) {
		out.print("\033[H\033[2J");
		out.print(msg);
		out.flush();
	}
	
	
	public static void printStats(Player p, Dice d) {
		out.println( "\n\t🐵 ".concat(p.name)
				.concat("   ⏪ ")
				.concat(d.getPrev() + "   ")
				.concat("😂 ")
				.concat((d.get() == 21 ? " TOKYO " : d.get()) + "   ")
				.concat(p.lives + "❤️"));
	}
	
	
	public static boolean hasDuplicates(List<Player> list) {
		Set<String> set = new HashSet<>();
		for (Player p : list)
			if (!set.add(p.name.toLowerCase())) 
				return true;
		return false;
	}
	
}
