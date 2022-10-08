package refactor5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameUtils {
	
	public static String os = System.getProperty("os.name");
	
	public static void clear() {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows"))
				new ProcessBuilder("cls.exe").inheritIO().start().waitFor();
			else new ProcessBuilder("clear").inheritIO().start().waitFor();
			//Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getIP() {
		String IP = null;
		try {
			IP = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} return IP;
	}

}
