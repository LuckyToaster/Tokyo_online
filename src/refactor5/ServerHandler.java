package refactor5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
	
	private ServerSocket ss;
	private DataInputStream r;
	private DataOutputStream w;
	
	public ServerHandler(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {}
	}

	
	public Socket accept() {
		Socket s = null;
		try {
			s = ss.accept();
		} catch (IOException e) {}
		return s;
	}
	

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
	
	
	public void broadcast(List<Player> players, String msg) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeUTF(msg);
				w.flush();
			} catch (IOException e) {}
		}
	}
	
	
	public void broadcast(List<Player> players, int n) {
		for (Player p : players) {
			try {
				w = new DataOutputStream(p.s.getOutputStream());
				w.writeInt(n);
				w.flush();
			} catch (IOException e) {}
		}
	}
	
	
	public void send(Socket s, String msg) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {}
	}
	
	
	public void send(Socket s, int n) {
		try {
			w = new DataOutputStream(s.getOutputStream());
			w.write(n);
			w.flush();
		} catch (IOException e) {}
	}
	
	
	public String read(Socket s) {
		String msg = null;
		try {
			r = new DataInputStream(s.getInputStream());
			msg = r.readUTF();
		} catch (IOException e) {}
		return msg;
	}
	
	
	public void closeServerSocket() {
		if (ss != null) 
			try {
				ss.close();
			} catch (IOException e) { 
				closeServerSocket();
			}
	}

}
