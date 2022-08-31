package refactor3;

import static java.lang.System.out;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket s;
	private BufferedWriter w;
	private BufferedReader r;
	private Scanner in;
	
	public Client(String host, int port) {
		in = new Scanner(System.in);
		try {
			s = new Socket(host, port);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
		
		getUsername();
		beServersBitch();
		in.close();
	}
	
	
	private void beServersBitch() {
		while (s.isConnected())
			out.println(readLine());
		out.println("YOU DIED LOL!");
	}

	
	private void send(String msg) {
		try {
			w.write(msg);
			w.newLine();
			w.flush();
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
	}

	
	private String readLine() {
		String line = null;
			try {
				while ((line = r.readLine()) != null);
					return line;
			} catch (IOException e) {}
		return line;
	}

		
	private void closeEverything(Socket s, BufferedReader r, BufferedWriter w) {
		try {
			if (r != null) r.close();
			if (w != null) w.close();
			if (s != null) s.close();
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
	}
	
	
	private void getUsername() {
		out.print("Enter a username: ");
		send(in.next().trim());
	}
	

	public static void main(String[] args) {
		new Client("localhost", 5500);
	}


}
