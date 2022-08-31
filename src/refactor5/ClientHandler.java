package refactor5;

import static java.lang.System.err;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler {
	
	private Socket s;
	private DataInputStream r;
	private DataOutputStream w;
	
	public ClientHandler(String host, int port) {
		try {
			s = new Socket(host, port);
			w = new DataOutputStream(s.getOutputStream());
			r = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			closeBridges();
			err.println("Connection to server failed");
		}
	}

	
	public boolean isConnected() {
		if (s.isConnected()) return true;
		else return false;
	}
	
	
	public String read() {
		String line = null;
			try {
				line =  r.readUTF();
			} catch (IOException e) {}
		return line;
	}

	
	public int readInt() {
		int n = 0;
		try {
			n = r.read();
		} catch (IOException e) {}
		return n;
	}
	
	
	public void send(String msg) {
		try {
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {}
	}


	public void closeBridges () {
		try {
			if (r != null) r.close();
			if (w != null) w.close();
			if (s != null) s.close();
		} catch (IOException e) {
			System.err.println("Failed to close bridges");
		}
	}

}
