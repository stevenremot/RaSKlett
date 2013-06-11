package compiler.parser;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @brief Associe à chaque symbole un code
 * 
 * Elle intervient après l'analyse lexicale, et associe
 * à chaque symbole un code indiquant si c'est un opérateur,
 * une parenthèse, un identifiant...
 * 
 * @author remot
 *
 */
public class Tokenizer {
	private static ArrayList<String> operators;
	
	static {
		operators = new ArrayList<String>();
		operators.add("+");
		operators.add("-");
		operators.add(":=");
		operators.add("*");
		operators.add("/");
	}
	
	/**
	 * @brief Codes des tokens
	 * 
	 * @author remot
	 *
	 */
	public enum Token {
		IDENTIFIER,
		OPERATOR,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		OPEN_PARENTHESIS,
		CLOSE_PARENTHESIS,
		INSTRUCTION_END,
	}
	
	private Stack<Instruction> instructions;
	private ArrayList<ArrayList<Token>> tokens;
	
	public Tokenizer(ArrayList<Instruction> instructions) {
		this.instructions = new Stack<Instruction>();
		this.instructions.addAll(instructions);
		
		tokens = new ArrayList<ArrayList<Token>>();
		tokenize();
	}
	
	public ArrayList<ArrayList<Token>> getTokens() {
		return tokens;
	}
	
	private void tokenize() {
		for(Instruction ins : this.instructions) {
			tokenizeInstruction(ins);
		}
	}
	
	private void tokenizeInstruction(Instruction ins) {
		ArrayList<Token> tokenIns = new ArrayList<Token>();
		
		for(String symbol : ins.getInstruction()) {
			tokenIns.add(getToken(symbol));
		}
		
		tokens.add(tokenIns);
	}
	
	private Token getToken(String symbol) {
		if(operators.contains(symbol)) {
			return Token.OPERATOR;
		}
		else if(symbol == ";" || symbol == ";;") {
			return Token.INSTRUCTION_END;
		}
		else if(symbol == "(") {
			return Token.OPEN_PARENTHESIS;
		}
		else if(symbol == ")") {
			return Token.CLOSE_PARENTHESIS;
		}
		else if(symbol == "[") {
			return Token.OPEN_BRACKET;
		}
		else if(symbol == "]") {
			return Token.CLOSE_BRACKET;
		}
		
		return Token.IDENTIFIER;
	}

}
