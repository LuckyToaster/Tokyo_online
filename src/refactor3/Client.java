package refactor3;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket s;
	private DataOutputStream w;
	private DataInputStream r;
	private Scanner in;
	
	public Client(String host, int port) {
		in = new Scanner(System.in);
		try {
			s = new Socket(host, port);
			w = new DataOutputStream(s.getOutputStream());
			r = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
		
		getUsername();
		listen2Server();
		in.close();
	}
	
	
	private void listen2Server() {
		while (s.isConnected()) {
			switch (readInt()) {
			case 1:
				readFromServerAndSendResponse();
				break;
			case 2:
				out.println(readLine());
				send(getValidatedAnswer());
				readFromServerAndSendResponse();
				break;
			default:
				break;
			}
		}
		out.println("YOU ARE DEAD, here have an 'L'!");
	}

	
	private void send(String msg) {
		try {
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
	}

	
	private String readLine() {
		String line = null;
			try {
				//while ((line = r.readLine()) != null);
				line =  r.readUTF();
			} catch (IOException e) {}
		return line;
	}

	
	private int readInt() {
		int n = 0;
		try {
			//while ((n = r.read()) != 0)
			n = r.read();
		} catch (IOException e) {}
		return n;
	}

		
	private void closeEverything(Socket s, DataInputStream r, DataOutputStream w) {
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
	
	
	private void readFromServerAndSendResponse() {
		out.println(readLine());
		out.print("What'd you get? > ");
		send(in.next());
	}

	private String getValidatedAnswer() {
		String msg = null;
		out.print("Yay or Nay? (y/n) > ");
		do msg = in.next().trim().toLowerCase();
		while (!msg.equals("y") || !msg.equals("n"));
		return msg;
	}


	public static void main(String[] args) {
		new Client("localhost", 5500);
	}


}
