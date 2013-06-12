package compiler.parser;

import java.util.ArrayList;
import java.util.Stack;

import compiler.CompilerException;

/**
 * @brief CLasse réalisant l'analyse sémantique du code
 * 
 * Pas trop vilaine pour l'instant, compte juste le nombre de parenthèses, vire les vides
 * 
 * @author remot
 *
 */
public class SemanticalAnalyser {
	private ArrayList<Instruction> instructions;
	private Stack<Instruction> insStack;
	private Stack<String> symbolStack;
	private Instruction currentInstruction;
	
	public SemanticalAnalyser(ArrayList<Instruction> instructions) throws CompilerException{
		this.instructions = instructions;
		
		insStack = new Stack<Instruction>();
		insStack.addAll(instructions);
		
		for(Instruction ins : insStack) {
			currentInstruction = ins;
			parseInstruction();
		}
	}
	
	public ArrayList<Instruction> getCombinators() {
		return instructions;
	}
	
	private void parseInstruction() throws CompilerException {
		symbolStack = new Stack<String>();
		symbolStack.addAll(currentInstruction.getInstruction());
		
		checkParenthesis();
	}
	
	private void checkParenthesis() throws CompilerException {
		if(seekFirstParenthesis()) {
			boolean firstSymbolAfterParenthesis = true;
			
			if(symbolStack.isEmpty()) {
				throw new CompilerException("')' expected", currentInstruction.getLine(), currentInstruction.getPosition());
			}

			do {
				if(symbolStack.get(0).equals("(")) {
					checkParenthesis();
				}

				if(firstSymbolAfterParenthesis && symbolStack.get(0).equals(")")) {
					firstSymbolAfterParenthesis = false;
					throw new CompilerException("Empty parenthesis forbidden", currentInstruction.getLine(), currentInstruction.getPosition());
				}
				
				symbolStack.remove(0);


			} while(!symbolStack.isEmpty() && !symbolStack.get(0).equals(")"));
			
			if(symbolStack.isEmpty()) {
				throw new CompilerException("')' expected", currentInstruction.getLine(), currentInstruction.getPosition());
			}
		}
	}
	
	private boolean seekFirstParenthesis() throws CompilerException {
		while(!symbolStack.isEmpty()) {
			if(symbolStack.get(0).equals(")")) {
					throw new CompilerException("')' unexpected", currentInstruction.getLine(), currentInstruction.getPosition());
			}
			else if(symbolStack.get(0).equals("(")) {
				symbolStack.remove(0);
				return true;
			}
			
			symbolStack.remove(0);
		}
		
		return false;
	}
}
