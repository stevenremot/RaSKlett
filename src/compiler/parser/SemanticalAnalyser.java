package compiler.parser;

import java.util.ArrayList;
import java.util.LinkedList;

public class SemanticalAnalyser {



	public String[] keyWords = { "auto", "short", "int", "float", "long",
			"double", "char", "struct", "union", "enum", "typedef", "const",
			"unsigned", "signed", "extern", "register", "static ", "volatile",
			"void ", "if", "else", "switch", "case", "for", "do", "while",
			"goto", "continue", "break", "default", "sizeof", "return" };

	// public String[] operator = { "(", ")", "[", "]", "->", ".", "!", "~",
	// "++",
	// "--", "+", "-", "*", "&", "/", "%", "<<", ">>", "<", ">", ">=",
	// "<=", "==", "!=", "^", "|", "&&", "||", "?", "+=", "-=", "*=",
	// "/=", "%=", "&=", "|=", "^=", "<<=", ">=", "=" };

	public String[] operators = { "+", "-", "*", "/", "= ", "!=", "<", " >",
			"<=", ">=", " && ", "||" };
	public String[] commentaire = {};
	public String[] boundary = { ",", ";", "\"\"", "\'\'" };

	// public String[] skMachOpera = { "S", "K", "I", "s", "k", "i" };
	// public String[] definition = { ":=" };
	public String strTemp = "";
	public static String otherString = "";
	public char tempChar;
	public String result = "";
	public static String tempWord = "";

	public LinkedList<String> myresult = new LinkedList<String>();
	Instruction instruction = new Instruction();

	public SemanticalAnalyser(Instruction instru) {
		this.instruction=instru;
	}

	/**
	 * @param count
	 *            to see if it is a number or the words that are not keywords
	 */
	public boolean isDigit(char tempCh) {
		if (tempCh >= '0' && tempCh <= '9') {
			return true;

		} else {
			return false;
		}
	}

	public boolean isLetter(char tempCh) {
		if (tempCh >= 'a' && tempCh <= 'z' || tempCh >= 'A' && tempCh <= 'Z') {
			return true;

		} else {
			return false;
		}
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

	public boolean isBoundrary(String tempStr) {
		for (int i = 0; i < boundary.length; i++) {
			if (tempStr.equals(boundary[i])) {
				return true;
			}
		}
		return false;
	}
	

	public void semanticAnalysis() {
		ArrayList<String> targetString = instruction.getInstruction();

		myresult.clear();

		for (int i = 0; i < targetString.size(); i++) {

			tempChar = targetString.get(i).charAt(0);
			if (isLetter(tempChar)) {
				if (tempWord != "") {
					if (isOperator(tempWord) || isBoundrary(tempWord)
							|| isKeyword(tempWord)) {
						myresult.add(tempWord);
						tempWord = "";
					}
				}
				tempWord += tempChar;

			} else if (isDigit(tempChar)) {
				if (tempWord != "") {
					if (isOperator(tempWord) || isBoundrary(tempWord)
							|| isKeyword(tempWord)) {
						myresult.add(tempWord);
						tempWord = "";
					}
				}
				tempWord += tempChar;
			} else if (tempChar == ' ') {
                  if (tempWord!="") {
					if (isOperator(tempWord)||isBoundrary(tempWord)||isKeyword(tempWord)) {
						myresult.add(tempWord);
					}
				}
                  tempWord+=tempChar;
			}

		}

	}

	// if ((tempChar >= 'a' && tempChar <= 'z')
	// || (tempChar >= 'A' && tempChar <= 'Z')) {
	// if (otherString != "") {
	// if (isBoundary(otherString) || isOperator(otherString)) {
	// }
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempWord += tempChar;
	// } else if ((tempChar >= '0' && tempChar <= '9') || tempChar == '.') {
	// if (otherString != "") {
	// if (isBoundary(otherString) || isOperator(otherString)) {
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempWord += tempChar;
	// } else if (tempChar == ' ') {
	// isDigit(count);
	// if (otherString != "") {
	// if (isBoundary(otherString) || isOperator(otherString)) {
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempWord = "";
	// } else {
	// isDigit(count);
	// String otherTmp = otherString;
	// otherString += tempChar;
	// if (isContained(otherString) && otherString.length() > 1) {
	// if (isOperator(otherString)) {
	// result += count + "\n";
	// }
	// otherString = "";
	// } else if (!isContained(otherString) && otherString.length() > 1) {
	// if (otherTmp != "") {
	// if (isBoundary(otherTmp) || isOperator(otherTmp)) {
	// result += count + "\n";
	// } else {
	// result += otherTmp + "\t\t" + count + "\n";
	// }
	// }
	// if (isBoundary(tempChar + "") || isOperator(tempChar + "")) {
	// result += count + "\n";
	// } else {
	// result += tempChar + "" + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// } else if (!iscontained(tempChar + "")) {
	// if (isBoundary(tempChar + "")) {
	// result += count + "\n";
	// } else {
	// result += tempChar + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempWord = "";
	// }

}
