package refactor;

import java.util.Scanner;
import static java.lang.System.out;
import static java.lang.Integer.parseInt;

public class Game {
	
	private Server server;
	
	public Game() {
	}
	
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
	

}
