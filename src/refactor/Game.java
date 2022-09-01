package refactor;

import java.util.Scanner;
import static java.lang.System.out;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class Game {
	
	
	
	public void askUserConnectOrHostLoop() {
		Scanner in = new Scanner(System.in);
		int answer;
		do {
			out.print("[1] Connect to Game, [2] Host Game\n> ");
			answer = in.nextInt();
			if (answer == 1 || answer == 2) continue;
			else out.println("Pleaser Enter a valid answer");
		} while (answer != 1 || answer != 2);
		in.close();
	}
	
	public static void clear() {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows"))
				new ProcessBuilder("cmd", "cls").inheritIO().start().waitFor();
				//Runtime.getRuntime().exec("cls");
			else Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			System.out.println(i);
			clear();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}

}
