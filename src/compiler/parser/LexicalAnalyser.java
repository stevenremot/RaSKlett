package compiler.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * @breif Classe d'analyse lexicale
 * 
 * Renvoie les instructions contenant les différents symboles du code.
 * 
 * @author remot
 *
 */
class LexicalAnalyser {
	private ArrayList<Instruction> result;
	private Reader input;
	private int currentChar;
	private String currentSymbol;
	private Instruction currentInstruction;
	
	private int currentLine, currentPos;
	
	private static String boundaries = "()[],;#";
	private static String spaces = " \t\n";
	private static ArrayList<String> operators;
	
	static {
		operators = new ArrayList<String>();
		
		String[] ops = {"+", "++", "+++", "++++", "-", "*", "/", "||", "&&", "!", "=", "!=", "<", ">", "<=", ">="};
		
		for(String op:ops) {
			operators.add(op);
		}
	}
	
	public LexicalAnalyser(Reader input) {
		this.input = input;
		currentInstruction = new Instruction();
		result = new ArrayList<Instruction>();
		currentLine = currentPos = 0;
		
		nextChar();
		
		while(!isAtEnd()) {
			analyseNextSymbol();
		}
		registerInstruction();
	}
	
	public ArrayList<Instruction> getSymbols() {
		return result;
	}
	
	private boolean nextChar() {
		try {
			currentChar = input.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return !isAtEnd();
	}
	
	private boolean isAtEnd() {
		return currentChar < 0;
	}
	
	private void registerSymbol() {
		if(currentSymbol != null && !currentSymbol.isEmpty()) {
			currentInstruction.addInstruction(currentSymbol);
		}
		
		currentSymbol = "";
	}
	
	private void registerInstruction() {
		if(currentInstruction != null && !currentInstruction.getInstruction().isEmpty()) {
			result.add(currentInstruction);
		}
		currentInstruction = new Instruction();
		currentInstruction.setLine(currentLine);
		currentInstruction.setPosition(currentPos);
	}
	
	private boolean isBoundary() {
		return boundaries.contains("" + (char)currentChar);
	}
	
	private boolean isOperatorBeginning() {
		for(String op: operators) {
			if(op.startsWith("" + (char)currentChar)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean registerOperator() {
		currentSymbol = "";
		
		ArrayList<String> candidates = new ArrayList<String>(operators);
		
		do {
			currentSymbol += (char) currentChar;
			
			for(String cand: candidates) {
				if(!cand.startsWith(currentSymbol)) {
					candidates.remove(cand);
				}
			}
				
			if(candidates.isEmpty()) {
				break;
			}
			else if(candidates.size() == 1) {
				registerSymbol();
				return true;
			}
				
		} while(nextChar());
		
		return false;
	}
	
	private void registerIdentifier() {
		currentSymbol = "";
		
		do {
			currentSymbol += "" + (char) currentChar;
		} while(nextChar() && !isBoundary() && !isSpace() && !isOperatorBeginning());
		
		registerSymbol();
	}
	
	private void registerBoundary() {
		if(currentChar == ';') {
			currentPos++;
			
			nextChar();
			if(currentChar == ';') {
				currentSymbol = ";;";
				nextChar();
			}
			else {
				currentSymbol = "";
			}
			
			skipSpaces();
			
			registerSymbol();
			registerInstruction();
			return;
		}
		
		currentSymbol = "" + (char)currentChar;
		registerSymbol();
	}
	
	private void skipSpaces() {
		do {
			if(currentChar == '\n') {
				currentPos = 0;
				currentLine++;
			}
		} while(isSpace() && nextChar());
	}
	
	private boolean isSpace() {
		return spaces.contains("" + (char)currentChar);
	}
	
	private void analyseNextSymbol() {
		skipSpaces();
		
		if(!isAtEnd()) {
			if(isBoundary()) {
				registerBoundary();
			}
			else if(isOperatorBeginning()) {
				registerOperator();
			}
			else {
				registerIdentifier();
			}
		}
	}
}