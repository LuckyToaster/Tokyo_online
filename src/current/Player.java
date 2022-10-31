package current;

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
	
	public Player(String name, int lives) {
		this.name = name;
		this.lives = lives;
		this.s = null;
	}

}
