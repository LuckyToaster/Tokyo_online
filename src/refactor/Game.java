package refactor;

import java.util.Scanner;
import static java.lang.System.out;
import static java.lang.Integer.parseInt;

public class Game {
	
	
	
	public void askUserConnectOrHostLoop() {
		Scanner in = new Scanner(System.in);
		int answer;
		do {
			out.print("[1] Connect to Game, [2] Host Game\n> ");
			answer = in.nextInt();
			if (answer == 1) continue;
			else if (answer == 2) continue;
			else out.println("Pleaser Enter a valid answer");
		} while (answer != 1 || answer != 2);
		in.close();
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			System.out.print("\033[H\033[2J" + i); // this ANSI code will clear the screen
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}

}
