package compiler.parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Qianqian CHEN parser lexicalAnalyser 31/05/2013
 */
public class lexicalAnalyser {

	public String strTest = new String(
			"int main(){int a=0;int b=0;int c=0; c=a+b}");
	public String[] keyWords = { "auto", "short", "int", "float", "long",
			"double", "char", "struct", "union", "enum", "typedef", "const",
			"unsigned", "signed", "extern", "register", "static ", "volatile",
			"void ", "if", "else", "switch", "case", "for", "do", "while",
			"goto", "continue", "break", "default", "sizeof", "return" ,"S","K","I"};
	public String[] operator = { "(", ")", "[", "]", "->", ".", "!", "~", "++",
			"--", "+", "-", "*", "&", "/", "%", "<<", ">>", "<", ">", ">=",
			"<=", "==", "!=", "^", "|", "&&", "||", "?", ":", "+=", "-=", "*=",
			"/=", "%=", "&=", "|=", "^=", "<<=", ">=", "=" };
	public String[] boundary = { ",", ";", "\"\"", "\'\'" };
	public String strTemp = "";
	public static String otherString = "";
	public char tempChar;
	public String result = "";
    public ArrayList<Instruction> instrArrayList=new ArrayList<Instruction>();
	
	
	public lexicalAnalyser() {
	}

	/**
	 * @param count
	 *            to see if it is a number or the words that are not keywords
	 */
	public void IsDigit(int count) {
		if (strTemp != "") {
			Object tempDouble = null;
			if (strTemp.charAt(0) >= '0' && strTemp.charAt(0) <= '9') {
				tempDouble = Double.parseDouble(strTemp);
				if (tempDouble != null
						&& (tempDouble + "").length() == strTemp.length()) {
					result += strTemp + "\t" + "78" + "\t" + count + "\n";
				} else {
					if (IsKeyWord(strTemp)) {
						result += count + "\n";
					} else {
						result += strTemp + "\t\t" + count + "\n";
					}
				}
			} else {
				if (IsKeyWord(strTemp)) {
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
	public boolean IsKeyWord(String string) {
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
	 * @return To see if it is contained in the string of operators
	 */
	public boolean IsOperator(String string) {
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
	public boolean IsBoundary(String string) {
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
	 * @return To see if it is a bracket
	 */
	public boolean Iscontained(String string) {
		if (string != "") {
			for (int i = 0; i < operator.length; i++) {
				if (operator[i].equals(string))
					return true;
			}
		}
		return false;
	}

	public void lexicalAnalysis(String targetString, int count) {
		
		Instruction intstru=new Instruction();
		intstru.setLine(count);
		instrArrayList.add(intstru);
		
		targetString = targetString.trim();
		for (int i = 0; i < targetString.length(); i++) {
			tempChar = targetString.charAt(i);
			if ((tempChar >= 'a' && tempChar <= 'z')
					|| (tempChar >= 'A' && tempChar <= 'Z')) {
				if (otherString != "") {
					if (IsBoundary(otherString) || IsOperator(otherString)) {
						result += count + "\n";
						
					} else {
						result += otherString + "\t\t" + count + "\n";
						intstru.addInstruction(otherString);
					}
					otherString = "";
				}
				strTemp += tempChar;
			} else if ((tempChar >= '0' && tempChar <= '9') || tempChar == '.') {
				if (otherString != "") {
					if (IsBoundary(otherString) || IsOperator(otherString)) {
						result += count + "\n";
					} else {
						result += otherString + "\t\t" + count + "\n";
						intstru.addInstruction(otherString);

					}
					otherString = "";
				}
				strTemp += tempChar;
			} else if (tempChar == ' ') {
				IsDigit(count);
				if (otherString != "") {
					if (IsBoundary(otherString) || IsOperator(otherString)) {
						result += count + "\n";
					} else {
						result += otherString + "\t\t" + count + "\n";
						intstru.addInstruction(otherString);

					}
					otherString = "";
				}
				strTemp = "";
			} else {
				IsDigit(count);
				String otherTmp = otherString;
				otherString += tempChar;
				if (Iscontained(otherString) && otherString.length() > 1) {
					if (IsOperator(otherString)) {
						result += count + "\n";
					}
					otherString = "";
				} else if (!Iscontained(otherString)
						&& otherString.length() > 1) {
					if (otherTmp != "") {
						if (IsBoundary(otherTmp) || IsOperator(otherTmp)) {
							result += count + "\n";
						} else {
							result += otherTmp + "\t\t" + count + "\n";
							intstru.addInstruction(otherTmp);

						}
					}
					if (IsBoundary(tempChar + "") || IsOperator(tempChar + "")) {
						result += count + "\n";
					} else {
						result += tempChar + "" + "\t\t" + count + "\n";
						intstru.addInstruction(Character.toString(tempChar));

					}
					otherString = "";
				} else if (!Iscontained(tempChar + "")) {
					if (IsBoundary(tempChar + "")) {
						result += count + "\n";
					} else {
						result += tempChar + "\t\t" + count + "\n";
						intstru.addInstruction(Character.toString(tempChar));

					}
					otherString = "";
				}
				strTemp = "";
			}
		}
	}

	/**
	 * @param fileName
	 *            open the file of code by the fileName
	 */
	public void readFile() {
		BufferedReader br = null;
		int count = 1;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));

			String targetString = br.readLine();
			System.out.println("Your function ");
			System.out.println(targetString);

			while (targetString != null) {
				lexicalAnalysis(targetString, count);
				count++;// count is used to calculate the numbers of
						// line,count=lineNumber-1
				targetString = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void readRowByRow(){
		BufferedReader br = null;
		int count = 1;
		try {
			// fr = new FileReader(file);
			System.out.println("Please input your function");
			br = new BufferedReader(new InputStreamReader(System.in));

			String targetString = br.readLine();
			System.out.println("Your function ");
			System.out.println(targetString);

				lexicalAnalysis(targetString, count);
				count++;// count is used to calculate the numbers of
						// line,count=lineNumber-1
				targetString = br.readLine();
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}