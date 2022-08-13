package refactor;

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

		out.print("Enter a username: ");
		sendMsg(in.next().trim());
		
		listenForMsg();
	}

	
	private void sendMsg(String msg) {
		try {
			w.write(msg);
			w.newLine();
			w.flush();
		} catch (IOException e) {
			closeEverything(s, r, w);
		}
	}
	

	public void listenForMsg() {
		String serverMsg;
		while (s.isConnected()) {
			try {

				// print dice drawing 
				serverMsg = r.readLine();
				out.println(serverMsg);
				
				if (serverMsg.length() <= 5) askUserValidateAnswer();

				out.print("What will you say? > ");
				sendMsg(Integer.toString(in.nextInt()));

			} catch (IOException e) {
				closeEverything(s, r, w);
			}
		}
	}
	
	public void listenForNigs() {
		while (s.isConnected()) {
			try {

				switch (r.read()) {
				// fresh start
				case (1): 
					out.println(r.readLine()); 
					out.print("What will you say? > ");
					sendMsg(Integer.toString(in.nextInt()));
					break;
				// normal
				case (2):
					out.println("🤡 " + r.read());
					askUserValidateAnswer();
					break;
				default:
					closeEverything(s, r, w); // should this method be called?
					break;
				}
				

			} catch (IOException e) {
				closeEverything(s, r, w);
			}
		}
	}

	
	private void askUserValidateAnswer() {
		out.print("Do you trust him? (y/n) > ");
		String answer;
		do {
			answer = in.next().trim().toLowerCase();
			if (answer.equals("y")) sendMsg("y");
			else if (answer.equals("n")) sendMsg("n");
		} while (!answer.equals("y") || !answer.equals("n"));
	}

	
	private void closeEverything(Socket s, BufferedReader r, BufferedWriter w) {
		try {
			if (r != null) r.close();
			if (w != null) w.close();
			if (s != null) s.close();
		} catch (IOException e) {}
	}
	

	public static void main(String[] args) {
		new Client("localhost", 5500).listenForMsg();
	}

}
