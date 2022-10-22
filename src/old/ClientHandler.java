package old;

import static java.lang.System.err;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Helper class for the {@code Client} class, manages the lower level 
 * functionalities of the client.
 * @author sench
 */
public class ClientHandler {
	
	private Socket s;
	private DataInputStream r;
	private DataOutputStream w;
	
	/**
	 * Takes a hostname and port number and attempts to instantiate the 
	 * {@code Socket}, {@code DataOutputStream} and {@code DataInputStream} 
	 * class attributes.
	 * @param host - server hostname
	 * @param port - server port number
	 */
	public ClientHandler(String host, int port) {
		try {
			s = new Socket(host, port);
			w = new DataOutputStream(s.getOutputStream());
			r = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			closeBridges();
			err.println("Connection to server failed ...");
		}
	}

	/**
	 * get {@code Socket} instance connection status 
	 * @return {@code boolean} connection status
	 */
	public boolean isConnected() {
		if (s.isConnected()) return true;
		else return false;
	}
	
	/**
	 * Attempt to read a utf string from the {@code DataInputStream} instance
	 * @return line - the UTF String from the {@code DataInputStream}.
	 */
	public String read() {
		String line = null;
			try {
				line =  r.readUTF();
			} catch (IOException e) {
				read();
			}
		return line;
	}

	/**
	 * Attempt to read an {@code int} from the {@code DataInputStream}
	 * @return n - the value read from the {@code DataInputStream}.
	 */
	public int readInt() {
		int n = 0;
		try {
			n = r.readInt();
		} catch (IOException e) {}
		return n;
	}
	
	
	/**
	 * Attempt to send a message to the server via the {@code DataOutputStream} 
	 * instance.
	 * @param msg - message to send
	 */
	public void send(String msg) {
		try {
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {
			send(msg);
		}
	}


	/**
	 * Attempts to close the {@code Socket}, 
	 * {@code DataInputStream} and {@code DataOutputStream} instances
	 */
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
