package current;

public class Test {
	
	public static void main(String[] args) {
		System.out.println("hello");
		System.out.println("\b\b\b");
	}
	
	
	public static void diceTest() {
		Dice dice = new Dice();
		for (int i = 0; i < 10000; i++) {
			dice.shake();
			System.out.println(
					(Dice.calcVal(dice.get()) == Dice.calcVal(dice.get())) + 
					" " + dice.get() + " " + 
					Dice.isValid(dice.get()));
		}
	}

}
