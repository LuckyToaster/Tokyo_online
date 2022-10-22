package current;

import static java.lang.System.err;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for the {@code Server} class, manages some lower level 
 * funtionalities of the server.
 * @author sench
 */
public class ServerHandler {
	
	private ServerSocket ss;
	private DataInputStream r;
	private DataOutputStream w;
	
	/**
	 * Takes a port number and attempts to instantiate the {@code ServerSocket}
	 * class attribute.
	 * @param port - the port for clients to connect to 
	 */
	public ServerHandler(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}
	}
	

	/**
	 * Instantiates players according to the arguments specified and 
	 * assigns a socket (connection) to each player.
	 * 
	 * @param connections - number of connections to make
	 * @param lives - number of lives to assign each player
	 * @return List<Player> 
	 */
	public ArrayList<Player> awaitConnections(int connections, int lives) {
		ArrayList<Player> players = new ArrayList<>();
		Socket s;
		try {
			for (int i = 0; i < connections; i++) {
				s = ss.accept();
				r = new DataInputStream(s.getInputStream());
				players.add(new Player(s, r.readUTF(), lives));
			}
		} catch (IOException e)  {
			closeServerSocket();
		}
		return players;
	}
	
	/**
	 * Send a message to all the players through their respective sockets.
	 * 
	 * @param players - list of players to broadcast to
	 * @param msg - message to broadcast
	 */
	public void broadcast(List<Player> players, String msg) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeUTF(msg);
				w.flush();
			} catch (IOException e) {
				closeServerSocket();
			}
		}
	}
	
	/**
	 * Send an integer value to all the players through their respective sockets
	 * 
	 * @param players - list of players to broadcast to
	 * @param n - value to broadcast
	 */
	public void broadcast(List<Player> players, int n) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeInt(n);
				w.flush();
			} catch (IOException e) {
				closeServerSocket();
			}
		}
	}
	
	/**
	 * Send a message to a socket
	 */
	public void send(Socket s, String msg) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {
			closeServerSocket();
		}
	}
	
	/**
	 * Send an integer value to a socket
	 */
	public void send(Socket s, int n) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.write(n);
			w.flush();
		} catch (IOException e) {
			closeServerSocket();
		}
	}
	
	public String read(Socket s) {
		String msg = null;
		try {
			r = new DataInputStream(s.getInputStream());
			msg = r.readUTF();
		} catch (IOException e) { }
		return msg;
	}
	
	public void closeServerSocket() {
		if (ss != null) 
			try {
				ss.close();
			} catch (IOException e) {
				err.println("The server can't even kill itself");
			}
	}

}
