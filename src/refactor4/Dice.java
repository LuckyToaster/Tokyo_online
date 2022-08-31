package refactor4;

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

	
	public int historySize() {
		return history.size();
	}
	
	
	public void clearHistory() {
		history.clear();
	}
	
	
	public int getPrev() {
		if (history.size() >= 2) return history.get(history.size()-2);
		else return 0;
	}

}
