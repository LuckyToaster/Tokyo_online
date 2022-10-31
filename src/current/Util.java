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
	
	
	/**
	 * Get a string from standard input.
	 * @param msg - the msg to prompt the user
	 * @param in - scanner to get the input String
	 * @return - String from standard input
	 */
	public static String getStr(String msg, Scanner in) {
		String input = "";
		do {
			out.print(msg);
			input = in.next().trim();
		} while (input.isBlank() || input.isEmpty());
		return input;
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
	
	
	public static boolean hasDuplicates(List<Player> list) {
		final Set<String> set = new HashSet<>();
		for (Player p : list)
			if (!set.add(p.name.toLowerCase())) 
				return true;
		return false;
	}
}
