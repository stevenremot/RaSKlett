package compiler.parser;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * @author Qianqian CHEN parser lexicalAnalyser 31/05/2013
 */
public class LexicalAnalyser {

	public char tempChar;
	public ArrayList<Instruction> resInstruArrayList = new ArrayList<Instruction>();
	private Reader input;

	public LexicalAnalyser(Reader input) {
		this.input = input;
		readRowByRow();
	}
	
	public ArrayList<Instruction> getSymbols() {
		return resInstruArrayList;
	}
	

	public void lexicalAnalysis(String[] targetStrings, int count) {

		
		int line = 0;
		for(int i=0;i<targetStrings.length;i++){
			String targetString=targetStrings[i];
			Instruction temInstruction = new Instruction();
			resInstruArrayList.add(temInstruction);
			line++;
			temInstruction.setLine(line);
			
			for (int j = 0; j < targetString.length(); j++) {
		        targetString = targetString.trim();
				
		        tempChar = targetString.charAt(j);		        	
				temInstruction.addInstruction(Character.toString(tempChar));
			
		}
		}
       
	}

	/**
	 * @param open
	 *            the file of code by the fileName
	 */

	public void readRowByRow() {
		BufferedReader br = null;
		int count = 1;
		try {
			br = new BufferedReader(input);

			String targetString = br.readLine();

			String[] tarStrings = targetString.split(";");
			lexicalAnalysis(tarStrings, count);


			count++;// count is used to calculate the numbers of
					// line,count=lineNumber-1
			targetString = br.readLine();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}