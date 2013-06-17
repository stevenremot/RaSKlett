package compiler.parser;

import java.util.ArrayList;
import java.util.LinkedList;

public class SemanticalAnalyser {
	public String strTest = new String(
			"int main(){int a=0;int b=0;int c=0; c=a+b}");
	public String[] keyWords = { "auto", "short", "int", "float", "long",
			"double", "char", "struct", "union", "enum", "typedef", "const",
			"unsigned", "signed", "extern", "register", "static ", "volatile",
			"void ", "if", "else", "switch", "case", "for", "do", "while",
			"goto", "continue", "break", "default", "sizeof", "return" };

	public String[] operator = { "(", ")", "[", "]", "->", ".", "!", "~", "++",
			"--", "+", "-", "*", "&", "/", "%", "<<", ">>", "<", ">", ">=",
			"<=", "==", "!=", "^", "|", "&&", "||", "?", "+=", "-=", "*=",
			"/=", "%=", "&=", "|=", "^=", "<<=", ">=", "=" };

	public String[] skMachOpera = { "S", "K", "I", "s", "k", "i" };
	public String[] definition = { ":=" };
	public String[] boundary = { ",", ";", "\"\"", "\'\'" };
	public String strTemp = "";
	public static String otherString = "";
	public char tempChar;
	public String result = "";
	public static String tempString = "";

	public LinkedList<String> myresult = new LinkedList<String>();

	public SemanticalAnalyser() {

	}

	/**
	 * @param count
	 *            to see if it is a number or the words that are not keywords
	 */
	public void isDigit(int count) {
		if (strTemp != "") {
			Object tempDouble = null;
			if (strTemp.charAt(0) >= '0' && strTemp.charAt(0) <= '9') {
				tempDouble = Double.parseDouble(strTemp);
				if (tempDouble != null
						&& (tempDouble + "").length() == strTemp.length()) {
					result += strTemp + "\t" + "78" + "\t" + count + "\n";
				} else {
					if (isKeyWord(strTemp)) {
						result += count + "\n";
					} else {
						result += strTemp + "\t\t" + count + "\n";
					}
				}
			} else {
				if (isKeyWord(strTemp)) {
					result += count + "\n";
				} else {
					result += strTemp + "\t\t" + count + "\n";
				}
			}
		}
	}

	/**
	 * @param string
	 * @return To see whether it is a keyword
	 */
	public boolean isKeyWord(String string) {
		if (string != "") {
			for (int i = 0; i < keyWords.length; i++) {
				if (string.equals(keyWords[i])) {
					result += string + "\t" + "keyword " + "\t";
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @return To see whether it is a keyword
	 */
	public boolean isSkMachOpera(String string) {
		if (string != "") {
			for (int i = 0; i < skMachOpera.length; i++) {
				if (string.equals(skMachOpera[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @return To see if it is contained in the string of operators
	 */
	public boolean isOperator(String string) {
		if (string != "") {
			for (int i = 0; i < operator.length; i++) {
				if (string.equals(operator[i])) {
					result += string + "\t" + "Operator" + "\t";
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @return In order to see whether it is the end of a row or the end of a
	 *         operation
	 */
	public boolean isBoundary(String string) {
		if (string != "") {
			for (int i = 0; i < boundary.length; i++) {
				if (string.equals(boundary[i])) {
					result += string + "\t" + "Boundary" + "\t";
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @return In order to see whether it is the end of a row or the end of a
	 *         operation
	 */
	public boolean isDefinition(String string) {
		if (string != "") {
			for (int i = 0; i < definition.length; i++) {
				if (string.equals(definition[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @return To see if it is a bracket
	 */
	public boolean isContained(String string) {
		if (string != "") {
			for (int i = 0; i < operator.length; i++) {
				if (operator[i].equals(string))
					return true;
			}
		}
		return false;
	}

	public void semanticAnalysis(Instruction instru) {
		ArrayList<String> targetString = instru.getInstruction();
		
		String tempStrDef = "";
		
		myresult.clear();
	
		for (int i = 0; i < targetString.size(); i++) {

			tempChar = targetString.get(i).charAt(0);
			if (tempStrDef != "" && tempChar == '=') {
				tempStrDef += tempChar;
				myresult.addFirst(tempStrDef);
			} else {
				if (tempChar == ':') {
					tempStrDef += tempChar;
					myresult.add(tempString);
					tempString="";
					otherString="";
					continue;
				} else {
					tempString += tempChar;
					if (tempChar >= 'a' && tempChar <= 'z' || tempChar >= 'A'
							&& tempChar <= 'Z') {												
						if (isSkMachOpera(Character.toString(tempChar)) && otherString == "") {
							myresult.add(tempString);
							tempString = "";

						} else {						
							if (otherString != "") {
								otherString = tempString;
								if (isKeyWord(otherString)) {
									myresult.add(otherString);
									tempString = "";
									otherString = "";

								}
							} else {
								otherString = tempString;
								if (isKeyWord(otherString)) {
									myresult.add(otherString);
									tempString = "";
									otherString = "";
								}
								otherString += tempString;
							}
						}
					}
				}
			}
		}


	}

	// if ((tempChar >= 'a' && tempChar <= 'z')
	// || (tempChar >= 'A' && tempChar <= 'Z')) {
	// if (otherString != "") {
	// if (IsBoundary(otherString) || IsOperator(otherString)) {
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempString += tempChar;
	// } else if ((tempChar >= '0' && tempChar <= '9') || tempChar == '.') {
	// if (otherString != "") {
	// if (IsBoundary(otherString) || IsOperator(otherString)) {
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempString += tempChar;
	// } else if (tempChar == ' ') {
	// IsDigit(count);
	// if (otherString != "") {
	// if (IsBoundary(otherString) || IsOperator(otherString)) {
	// result += count + "\n";
	// } else {
	// result += otherString + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempString = "";
	// } else {
	// IsDigit(count);
	// String otherTmp = otherString;
	// otherString += tempChar;
	// if (Iscontained(otherString) && otherString.length() > 1) {
	// if (IsOperator(otherString)) {
	// result += count + "\n";
	// }
	// otherString = "";
	// } else if (!Iscontained(otherString) && otherString.length() > 1) {
	// if (otherTmp != "") {
	// if (IsBoundary(otherTmp) || IsOperator(otherTmp)) {
	// result += count + "\n";
	// } else {
	// result += otherTmp + "\t\t" + count + "\n";
	// }
	// }
	// if (IsBoundary(tempChar + "") || IsOperator(tempChar + "")) {
	// result += count + "\n";
	// } else {
	// result += tempChar + "" + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// } else if (!Iscontained(tempChar + "")) {
	// if (IsBoundary(tempChar + "")) {
	// result += count + "\n";
	// } else {
	// result += tempChar + "\t\t" + count + "\n";
	// }
	// otherString = "";
	// }
	// tempString = "";
	// }

}
