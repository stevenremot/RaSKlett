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
	
	public static ArrayList<Instruction> parse(Reader input) throws CompilerException {
		LexicalAnalyser lex = new LexicalAnalyser(input);
		return lex.getSymbols();
	}
}
