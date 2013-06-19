package compiler.parser;

import java.util.ArrayList;
import java.util.LinkedList;

public class SemanticalAnalyser {

	public String[] header = { "import" };

	public String[] keyWords = { "void", "main", "if", "for", "while", "char",
			"int", "float", "double", "else", "return", "break", "default",
			"case", "struct", "new", "printf" };


	public String[] operators = { "+", "-", "*", "/", "++", "--", "%" };
	public String[] coms = { "<", "<=", "=", ">", ">=", "<>", "==", "!=", "!" };
	public String[] inters = { ",", ";", "'", ".", "(", ")", "{", "}", "\"" };


	public char[] cha;
	public String string = "";
	public char tempChar;
	public String result = "";
	public String tempWord = "";
	public static String tempStr = "";

	public LinkedList<String> myresult = new LinkedList<String>();
	Instruction instruction = new Instruction();
	ArrayList<String> targetString;

	public int k = 0, sit;
	char ch = ' ';

	public SemanticalAnalyser(Instruction instru) {
		this.instruction = instru;
		myresult.clear();
		targetString = instruction.getInstruction();
		k = 0;
		string = "";
		semanticAnalysis();

	}

	/**
	 * @param count
	 *            to see if it is a number or the words that are not keywords
	 */
	public boolean isDigit() {
		if (ch >= '0' && ch <= '9') {
			return true;

		} else {
			return false;
		}
	}

	public boolean isLetter() {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
			return true;

		} else {
			return false;
		}
	}

	public boolean isHeader(String tempStr) {
		for (int n = 0; n < header.length; n++) {
			if (string.equals(header[n])) {
				return true;
			}
		}
		return false;
	}

	public boolean isOperator(String tempStr) {
		for (int i = 0; i < operators.length; i++) {
			if (tempStr.equals(operators[i])) {
				return true;
			}
		}
		return false;

	}

	public boolean isKeyword(String tempStr) {
		for (int i = 0; i < keyWords.length; i++) {
			if (tempStr.equals(keyWords[i])) {
				return true;
			}

		}
		return false;
	}

	private void getChar() {
		if (k<targetString.size()) {
			ch = targetString.get(k).charAt(0);
			k++;
		}else {
			return;
		}
	
	}

	private void conCat() {
		string += Character.toString(ch);
	}

	public void semanticAnalysis() {

		int n, symble = 0, sy = 0;
		while (ch == ' ') {
			getChar();
		}
		if (isLetter()) {
			while (isLetter()) {
				conCat();
				if (k<targetString.size()) {				
				getChar();
				}else {
					string=string.trim();
					myresult.add(string);
					return;
				}
				
			}

			if (isHeader(string)) {
				myresult.add(string);
			} else {
				if (isKeyword(string)) {

					myresult.add(string);
				} else {

					string = string.trim();
					myresult.add(string);

				}
			}

			k--;

		} else if (isDigit()) {
			while (isDigit()) {
				conCat();
				getChar();
			}
			myresult.add(string);
			k--;
		} else {
			string = string.trim();
			conCat();

			for (n = 0; n < operators.length; n++) {
				if (string.equals(operators[n])) {
					symble = 0;
					sit = n;
				}

			}
			for (n = 0; n < coms.length; n++) {
				if (string.equals(coms[n])) {
					symble = 1;
					sit = n;
				}

			}
			for (n = 0; n < inters.length; n++) {

				if (string.equals(inters[n])) {
					symble = 2;
					sit = n;
				}

			}
			if (symble == 0) {
				getChar();
				tempStr += Character.toString(ch);

				for (n = 0; n < operators.length; n++) {
					if (tempStr.equals(operators[n])) {
						sy = 0;
						conCat();
						for (n = 0; n < operators.length; n++) {
							if (string.equals(operators[n])) {
								sit = n;
							}

						}
					}
				}
				if (sy != 0)
					k--;
			} else if (symble == 1) {

				tempStr += Character.toString(ch);
				for (n = 0; n < coms.length; n++) {
					if (tempStr.equals(coms[n])) {
						sy = 0;
						conCat();
						for (n = 0; n < coms.length; n++) {
							if (string.equals(coms[n])) {
								sit = n;
							}
						}
					}
				}
				if (sy != 0)
					k--;
			}else {
				if (k<targetString.size()) {
					getChar();
				}
				
			}
			
			

			switch (symble) {
			case 0:
				myresult.add(string);
				break;
			case 1:
				myresult.add(string);
				// myresult.add("\n");
				break;
			case 2:
				myresult.add(string);
				break;
			}
		}
		while (k < targetString.size()) {
			string = " ";
			semanticAnalysis();

		}

	}

}
