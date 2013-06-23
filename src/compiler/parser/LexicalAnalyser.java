package compiler.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

import compiler.CompilerException;

/**
 * Classe d'analyse lexicale
 * 
 * Renvoie les instructions contenant les différents symboles du code.
 * 
 * @author qchen
 *
 */
class LexicalAnalyser {
	private ArrayList<Instruction> result;
	private Reader input;
	private int currentChar;
	private String currentSymbol;
	private Instruction currentInstruction;
	
	private int currentLine, currentPos;
	
	private static String boundaries = "()[],;#.";
	private static String spaces = " \t\n";
	private static ArrayList<String> operators;
	
	static {
		operators = new ArrayList<String>();
		
		String[] ops = {"+", "++", "+++", "++++", "-", "*", "/", "||", "&&", "!", "=", "!=", "<", ">", "<=", ">=", ":="};

        Collections.addAll(operators, ops);
	}
	
	public LexicalAnalyser(Reader input) throws CompilerException {
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
	
	private void registerOperator() throws CompilerException {
        String oldSymbol = "";
		currentSymbol = "";
		
		do {
            oldSymbol = currentSymbol;
			currentSymbol += (char) currentChar;
			
			ArrayList<String> candidates = new ArrayList<String>();
			
			for(String op: operators) {
				if(op.startsWith(currentSymbol)) {
					candidates.add(op);
				}
			}
				
			if(candidates.isEmpty()) {
				break;
			}
				
		} while(nextChar() && !isSpace() && !isBoundary());
		
		for(String op: operators) {
			if(currentSymbol.equals(op)) {
				registerSymbol();
				return;
			}
		}

        for(String op: operators) {
            if(oldSymbol.equals(op)) {
                String tmp = currentSymbol;
                currentSymbol = oldSymbol;
                registerSymbol();
                currentSymbol = tmp.substring(tmp.length() - 1);
                return;
            }
        }
		
		throw new CompilerException("Opérateur inconnu: " + currentSymbol, currentLine, currentPos);
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

            currentInstruction.setLastLine(currentLine);

			skipSpaces();
			
			registerSymbol();
			registerInstruction();
			return;
		}
		
		currentSymbol = "" + (char)currentChar;
		registerSymbol();
		nextChar();
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
	
	private void analyseNextSymbol() throws CompilerException {
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