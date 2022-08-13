import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static java.lang.System.out;

public class Player {
	
	private String name, lastThrow;
	private int lives;
	private boolean alive;
	private Registry registry;
	private Tokyable server;


	Player() {
		alive = true;
	}

	public void init() {
	    // TODO	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getLives() {
		return lives;
	}


	public void setLives(int lives) {
		this.lives = lives;
	}


	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	public Registry getRegistry() {
		return registry;
	}


	public void setRegistry(Registry registry) {
		this.registry = registry;
	}


	public Tokyable getServer() {
		return server;
	}


	public void setServer(Tokyable server) {
		this.server = server;
	}
	
	public static void main(String[] args) {
		//new Player("Senen", "tokyo", 5500, "tokyo" );
		try {

			Registry registry = LocateRegistry.getRegistry("localhost", 5500);
			Tokyable server = (Tokyable) registry.lookup("tokyo");
			
			out.println(server.throwDice());

		} catch (RemoteException | NotBoundException e) {}
	}

}
