package compiler.parser;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Qianqian CHEN parser lexicalAnalyser 31/05/2013
 */
public class lexicalAnalyser {

	public char tempChar;
	public ArrayList<Instruction> resInstruArrayList = new ArrayList<Instruction>();

	public lexicalAnalyser() {
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
			// fr = new FileReader(file);
			System.out.println("Please input your function");
			br = new BufferedReader(new InputStreamReader(System.in));

			String targetString = br.readLine();
			System.out.println("Your function ");
			System.out.println(targetString);

			String[] tarStrings = targetString.split(";");
			lexicalAnalysis(tarStrings, count);


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