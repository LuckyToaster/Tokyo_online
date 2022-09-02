package refactor5;

import static java.lang.Integer.parseInt;
import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dice {
	
	private final String[] ASCII;
	private List<Integer> history;
	private int[] dice;
	private int value;
	
	public Dice() {
		history = new ArrayList<>();
		dice = new int[2];
		this.ASCII = new String[] {
			"-------\n|     |\n|  ●  |\n|     |\n-------",
			"-------\n| ●   |\n|     |\n|   ● |\n-------",
			"-------\n| ●   |\n|  ●  |\n|   ● |\n-------",
			"-------\n| ● ● |\n|     |\n| ● ● |\n-------",
			"-------\n| ● ● |\n|  ●  |\n| ● ● |\n-------",
			"-------\n| ● ● |\n| ● ● |\n| ● ● |\n-------"
		};
	}

	
	/**
	 * Throws or shakes two dice.
	 * Sets the value of {@code dice} and {@code value} to the value of the 
	 * newly thrown dice, and adds that value to {@code history} ArrayList
	 * @return void
	 */
	public void shake() {
		dice = new int[]{throwDie(), throwDie()};

		if (dice[0] >= dice[1])
			value = parseInt(valueOf(dice[0]) + valueOf(dice[1]));
		else value = parseInt(valueOf(dice[1]) + valueOf(dice[0]));

		history.add(value);
	}

	
	public String getDrawing() {
		String[] diceArt = new String[]{ASCII[dice[0]-1], ASCII[dice[1]-1]};
		String l1, l2, finalArt = "\n\n"; 
		BufferedReader d1, d2;

		try {
			d1 = new BufferedReader(new StringReader(diceArt[0]));
			d2 = new BufferedReader(new StringReader(diceArt[1]));
		
			while ((l1 = d1.readLine()) != null && (l2 = d2.readLine()) != null)
				finalArt += "\t\t".concat(l1).concat("\t").concat(l2).concat("\n");

			d1.close(); 
			d2.close();
		} catch (IOException e) {}

		return finalArt;
	}
	
	
	private int throwDie() {
		return (int) (1 + random() * 6);
	}
	
	
	public void draw() {
		out.println(getDrawing());
	}


	public int get() {
		return value;
	}


	public int getPrev() {
		if (history.size() > 1) return history.get(history.size()-2);
		else return 0;
	}
	
	public int getVal() {
		return calcVal(value);
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
	
	
	private int calcVal(int n) {
		switch(n) {
		case (21):
			return 21;
		case (66):
			return 20;
		case (55):
			return 19;
		case (44):
			return 18;
		case (33):
			return 17;
		case (22):
			return 16;
		case (11):
			return 15;
		case (65):
			return 14;
		case (64):
			return 13;
		case (63):
			return 12;
		case (62):
			return 11;
		case (61):
			return 10;
		case (54):
			return 9;
		case (53):
			return 8;
		case (52):
			return 7;
		case (51):
			return 6;
		case (43):
			return 5;
		case (42):
			return 4;
		case (41):
			return 3;
		case (32):
			return 2;
		case (31):
			return 1;
		default:
			return 0;
		}
	}

}
