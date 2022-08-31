package refactor2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;

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
		listen4Msg();
	}


	public void listen4Msg() {
		while (s.isConnected()) {
			try {
				switch (r.read()) {
				case (1): 
					readFromServerAndSendResponse();
					break;
				case (2): 
					out.println(readLine());
					askUserValidateAnswer();
					readFromServerAndSendResponse();
					break;
				default:
					break;
				}
			} catch (IOException e) {
				closeEverything(s, r, w);
			}
		}
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
		} catch (IOException e) {}
	}


	private void readFromServerAndSendResponse() throws IOException {
		out.println(readLine());
		out.print("What will you say? > ");
		send(in.next());
	}


	private void askUserValidateAnswer() {
		out.print("Do you trust him? (y/n) > ");
		String answer;
		do {
			answer = in.next().trim().toLowerCase(); 
			if (answer.equals("y")) send("y");
			else if (answer.equals("n")) send("n");
		} while (!answer.equals("y") || !answer.equals("n"));
	}


	private void getUsername() {
		out.print("Enter a username: ");
		send(in.next().trim());
	}


	public static void main(String[] args) {
		new Client("localhost", 5500);
	}

}
