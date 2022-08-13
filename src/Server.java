import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import static java.lang.Math.random;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class Server implements Tokyable {
	
	private int lives, die1, die2, playerN;
    private Registry registry;
	private ArrayList<Integer> history;
	private String[] dice = {	
		"""
		-----
		|   |
		| * |
		|   |
		-----
		""",

		"""
		-----
		|*  |
		|   |
		|  *|
		-----
		""",

		"""
		-----
		|*  |
		| * |
		|  *|
		-----
		""",

		"""
		-----
		|* *|
		|   |
		|* *|
		-----
		""",

		"""
		-----
		|* *|
		| * |
		|* *|
		-----
		""",

		"""
		-----
		|* *|
		|* *|
		|* *|
		-----
		"""
	};

	
	Server(int playerN, int lives) {
		this.playerN = playerN;
		this.lives = lives;
	}


	public int getLives() throws RemoteException {
		return this.lives;
	}


	public int throwDie() {
		return (int) (1 + (random() * 5));
	}

	
	/**
	 * <ul>
	 * 	<li>Throws both dice, assigning them new values</li>
	 * 	<li>Interprets the value that the dice would represent in a game of Tokyo</li>
	 * 	<li>Adds the interpreted value to the {@code history ArrayList}
	 * 	<li>Returns the interpreted value</li>
	 * </ul>
	 * @return the number that the dice represent in the Tokyo dice game
	 */
	public int throwDice() throws RemoteException {
		int dice;

		this.die1 = throwDie();
		this.die2 = throwDie(); 

		if (die1 < die2) 
			dice = parseInt(valueOf(die2) + valueOf(die1));
		else dice = parseInt(valueOf(die1) + valueOf(die2));
		
		history.add(dice);
		return dice;
	}

	
	/**
	 * @return Ascii Art representation of both dice as a {@code String}
	 */
	public String getAsciiArt() {
		return dice[this.die1] + "\n" + dice[this.die2];
	}

	
	public int getPrevious() throws RemoteException {
		return history.get(history.size() -2);
	}
	
	
	public static void main(String[] args) {
		Registry registry = null;
		try {

			registry = LocateRegistry.createRegistry(5500);
			Server server = new Server(3,1);
			registry.rebind("tokyo", (Tokyable) UnicastRemoteObject.exportObject(server, 0));

		} catch (RemoteException e) {}
	}

}
