package compiler.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import compiler.CompilerException;

/**
 * Classe réalisant l'analyse lexicale du code
 * 
 * @author steven
 *
 */
public class LexicalAnalyser {
	private Reader reader;
	private ArrayList<Instruction> instructions;
	private Instruction currentInstruction;
	
	private static String spaces = " \n\t";
	private static String boundaries = "();";
	
	private String curSymbol = "";
	private int curChar = 0;
	
	public LexicalAnalyser(Reader reader) throws CompilerException {
		this.reader = reader;
		
		instructions = new ArrayList<Instruction>();
		currentInstruction = new Instruction();
		
		try {
			analyse();
		} catch (IOException e) {
			throw new CompilerException(e.getMessage(),
						currentInstruction.getLine(),
						currentInstruction.getPosition());
		}
	}
	
	public ArrayList<Instruction> getSymbols() {
		return instructions;
	}
	
	private void analyse() throws IOException {
		nextChar();
		
		while(nextSymbol());
		
		if(currentInstruction != null) {
			instructions.add(currentInstruction);
		}
	}
	
	private void nextChar() throws IOException {
		curChar = reader.read();
	}
	
	private boolean isAtEnd() {
		return curChar < 0;
	}
	
	private boolean nextSymbol() throws IOException {
		getNextSymbolBeginning();
		
		if(isAtEnd()) {
			return false;
		}
		
		if(isBoundary()) {
			registerBoundary();
		}
		else {
			registerIdentifier();
		}
		
		return true;
	}
	
	private void registerSymbol() {
		if(curSymbol != "") {
			currentInstruction.addInstruction(curSymbol);
			curSymbol = "";
		}
	}
	
	private void registerInstruction() {
		registerSymbol();
		
		if(!currentInstruction.getInstruction().isEmpty()) {
			instructions.add(currentInstruction);
		}
	}
	
	private boolean isSpace() {
		return spaces.contains("" + (char)curChar);
	}
	
	private boolean isBoundary() {
		return boundaries.contains("" + (char)curChar);
	}
	
	private void registerBoundary() throws IOException {
		curSymbol = "" + (char)curChar;
		
		if(curSymbol.equals(";")) {
			Instruction next = new Instruction();
			next.setLine(currentInstruction.getLine());
			next.setPosition(currentInstruction.getPosition() + 1);
			
			registerInstruction();
			
			currentInstruction = next;
		}
		
		registerSymbol();
		nextChar();
	}
	
	private void registerIdentifier() throws IOException {
		do {
			curSymbol += (char)curChar;
			nextChar();
		} while(!isSpace() && !isBoundary() && !isAtEnd());
		
		registerSymbol();
	}
	
	private void checkNewLine() {
		if(curChar == '\n') {
			Instruction next = new Instruction();
			next.setLine(currentInstruction.getLine() + 1);
			next.setPosition(0);
			
			registerInstruction();
			
			currentInstruction = next;
		}
	}
	
	private void getNextSymbolBeginning() throws IOException {
		while(isSpace()) {
			nextChar();
			checkNewLine();
		}
	}
}
