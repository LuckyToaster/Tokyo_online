package refactor4;

import static java.lang.System.out;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import refactor4.Client;

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
			closeBridges(s, r, w);
		}
		
		getUsername();
		talkToServer();
		closeBridges(s, r, w);
		in.close();
		
	}
	
	
	private void talkToServer() {
		while (s.isConnected()) {
			switch (readInt()) {
			case 1:
				out.println(readUTF());
				out.print("What'd you get? > ");
				send(in.next());
				break;
			case 2:
				out.println(readUTF());

				String msg = null;
				out.print("Yay or Nay? (y/n) > ");
				do msg = in.next().trim().toLowerCase();
				while (!msg.equals("y") && !msg.equals("n"));

				send(in.next());

				out.println(readUTF());
				out.print("What'd you get? > ");
				send(in.next());
				break;
			case 3:
				out.println(readUTF());
				break;
			default:
				break;
			}
		} out.println("YOU ARE DEAD, here have an 'L'!");
	}
	
	
	private String readUTF() {
		String line = null;
			try {
				line =  r.readUTF();
			} catch (IOException e) {}
		return line;
	}

	
	private int readInt() {
		int n = 0;
		try {
			n = r.read();
		} catch (IOException e) {}
		return n;
	}


	private void send(String msg) {
		try {
			w.writeUTF(msg);
			w.flush();
		} catch (IOException e) {
			closeBridges(s, r, w);
		}
	}

	
	private void closeBridges (Socket s, DataInputStream r, DataOutputStream w) {
		try {
			if (r != null) r.close();
			if (w != null) w.close();
			if (s != null) s.close();
		} catch (IOException e) {
			closeBridges(s, r, w);
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
