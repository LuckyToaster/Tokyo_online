package current;

public class Test {
	
	public static void main(String[] args) {
		Server server = new Server(5500, 2, 2);
		Client c1 = new Client("localhost", 5500, "c1");
		Client c2 = new Client("lcoalhost", 5500, "c2");
		
	}

}
