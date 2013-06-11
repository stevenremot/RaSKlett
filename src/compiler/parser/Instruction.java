package compiler.parser;

import java.util.ArrayList;

public class Instruction {
	private int numberL = 0;
	private int numberI = 0;

	private ArrayList<String> contentStrings = new ArrayList<String>();

	public int getLine() {
		return numberL;
	}

	public void setLine(int numberL) {
		this.numberL = numberL;
	}

	public int getNumberI() {
		return numberI;
	}

	public void setNumberI(int numberI) {
		this.numberI = numberI;
	}

	public ArrayList<String> getContentStrings() {
		return contentStrings;
	}

	public void setContentStrings(ArrayList<String> contentStrings) {
		this.contentStrings=contentStrings;
	}
	
	public void addContentStrings(String temStr) {
		this.contentStrings.add(temStr);
	}
}
