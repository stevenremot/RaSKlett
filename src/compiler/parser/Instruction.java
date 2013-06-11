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

	public int getPosition() {
		return numberI;
	}

	public void setPosition(int numberI) {
		this.numberI = numberI;
	}

	public ArrayList<String> getInstruction() {
		return contentStrings;
	}

	public void setInstruction(ArrayList<String> contentStrings) {
		this.contentStrings=contentStrings;
	}
	
	public void addInstruction(String temStr) {
		this.contentStrings.add(temStr);
	}
}
