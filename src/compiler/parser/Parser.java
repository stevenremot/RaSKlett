package compiler.parser;

import java.io.Reader;
import java.util.ArrayList;

import compiler.CompilerException;

/**
 * @author qchen
 * For the call of lexicalAnalyser and SemanticalAnalyser
 * 03/06/2013
 */
public class Parser {
	public String resultString;
	
	/**
	 * to be a interface for call lexicalAnalyser and SemanticalAnalyser
	 * @throws CompilerException 
	 */
	public static ArrayList<Instruction> parse(Reader input) throws CompilerException {
		LexicalAnalyser lA = new LexicalAnalyser(input);

		ArrayList<Instruction> symbols = lA.getSymbols(); 
		
		SyntaxicalAnalyser sA = new SyntaxicalAnalyser(symbols);
		
		return sA.getInstructions();

	}
}
