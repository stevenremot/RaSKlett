package compiler.parser;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @brief CLasse réalisant l'analyse sémantique du code
 * 
 * @author remot
 *
 */
public class SemanticalAnalyser {
	private ArrayList<Instruction> instructions;
	private Stack insStack;
	private Instruction currentInstruction;
	
	public SemanticalAnalyser(ArrayList<Instruction> instructions){
		this.instructions = instructions;
		
		insStack = new Stack();
		insStack.addAll(instructions);
	}
	
	private 
}
