package current;

import static java.lang.Integer.parseInt;
import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dice {
	
	private static final List<Integer> VALID_NUMS = List.of(31,32,41,42,43,51,52,53,54,61,62,63,64,65,11,22,33,44,55,66,21);
	private final String[] ASCII;
	public List<Integer> history;
	private int[] dice;
	
	public Dice() {
		history = new ArrayList<>();
		dice = new int[2];
		ASCII = new String[] {
			"-------\n|     |\n|  •  |\n|     |\n-------",
			"-------\n| •   |\n|     |\n|   • |\n-------",
			"-------\n| •   |\n|  •  |\n|   • |\n-------",
			"-------\n| • • |\n|     |\n| • • |\n-------",
			"-------\n| • • |\n|  •  |\n| • • |\n-------",
			"-------\n| • • |\n| • • |\n| • • |\n-------"
		};
	}
	
	public void shake() {
		dice = new int[]{throwDie(), throwDie()};
		if (dice[0] >= dice[1])
			history.add(parseInt(valueOf(dice[0]) + valueOf(dice[1])));
		else history.add(parseInt(valueOf(dice[1]) + valueOf(dice[0])));
	}

	public String getDrawing() {
		String[] diceArt = new String[]{ASCII[dice[0]-1], ASCII[dice[1]-1]};
		String l1, l2, finalArt = "\n"; 
		BufferedReader d1, d2;
		try {
			d1 = new BufferedReader(new StringReader(diceArt[0]));
			d2 = new BufferedReader(new StringReader(diceArt[1]));
		
			while ((l1 = d1.readLine()) != null && (l2 = d2.readLine()) != null)
				finalArt += "\t  ".concat(l1).concat("   ").concat(l2).concat("\n");

			d1.close(); 
			d2.close();
		} catch (IOException e) {}

		return finalArt;
	}
	
	public void draw() {
		out.println(getDrawing());
	}
	
	private static int throwDie() {
		return (int) ( 1 + random() * 6); 
	}
	
	public int get() {
		if (history.size() > 0) 
			return history.get(history.size()-1);
		else return 0;
	}
	
	public void set(int n) {
		if (history.size() > 0) 
			history.set(history.size()-1, n);
	}

	public int getPrev() {
		if (history.size() > 1) 
			return history.get(history.size()-2);
		else return 0;
	}
	
	public void setPrev(int n) {
		if (history.size() > 1)
			history.set(history.size() -2, n);
	}
	
	public int getVal() {
		return calcVal(get());
	}

	public int getPrevVal() {
		return calcVal(getPrev());
	}

	public int historySize() {
		return history.size();
	}
	
	public void clearHistory() {
		history.clear();
	}
	
	public static boolean isValid(int n) {
		return VALID_NUMS.contains(n);
	}
	
	public static int calcVal(int n) {
		if (VALID_NUMS.contains(n))
			return VALID_NUMS.indexOf(n) + 1;
		else return 0;
	}
	
	public static void main(String[] args) {
		Dice dice = new Dice();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			dice.shake();
			System.out.println(Dice.calcVal(dice.get()) == Dice.calcVal(dice.get()));
		}
	}
}
