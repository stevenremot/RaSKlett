package compiler.parser;

import java.io.Reader;
import java.util.ArrayList;

/**
 * @author qchen
 * For the call of lexicalAnalyser and SemanticalAnalyser
 * 03/06/2013
 */
public class Parser {
	public String resultString;
	
	/**
	 * to be a interface for call lexicalAnalyser and SemanticalAnalyser
	 */
	public static ArrayList<Instruction> parse(Reader input) {
		LexicalAnalyser lA = new LexicalAnalyser(input);
		SemanticalAnalyser sA=new SemanticalAnalyser();

		ArrayList<Instruction>symbols = lA.getSymbols(); 
		
		for(Instruction instruction : symbols){
			sA.semanticAnalysis(instruction);
			
			instruction.setInstruction(new ArrayList<String>(sA.myresult));
		}
		
		return symbols;
		
		/*
		for(int i=0;i<symbols.size();i++)
		{
			System.out.println("============================START===========================================");

			
			line=lA.resInstruArrayList.get(i).getLine();
			tempArrayList=lA.resInstruArrayList.get(i).getInstruction();
			System.out.println("LineNumber:   "+line+"   Content:   "+tempArrayList);

			sA.semanticAnalysis(lA.resInstruArrayList.get(i));

			for (int j=0;j<sA.myresult.size();j++) {				
				System.out.println(" ("+sA.myresult.get(j)+" )");
			}
			
			
			
			System.out.println("------------------------------END--------------------------------------------");
			System.out.println("");




			
		}*/
		//textArea.setText(resultString);

	}
}
