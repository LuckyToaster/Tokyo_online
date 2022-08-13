package refactor;

import static java.lang.System.out;

public class Player {
	
	private String name;
	private int lives;

	public Player(String name, int lives) {
		this.name = name;
		this.lives = lives;
	}
	
	public void loseLife() {
		lives -= 1;
	}
	
	public int getLives() {
		return lives;
	}
	
	public String getName() {
		return name;
	}
	
}
