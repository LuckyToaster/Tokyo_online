package refactor4;

import java.net.Socket;

public class Player {

	public String name;
	public int lives;
	public Socket s;

	public Player(Socket s, String name, int lives) {
		this.s = s;
		this.name = name;
		this.lives = lives;
	}

}
