package compiler.parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.CompilerException;

/**
 * @brief Réalise l'analyse syntaxique du code en sortie de SemanticalAnalyser
 * 
 * Transforme les suites de symboles du langage en suite de noms de combinateurs
 * 
 * @author remot
 *
 */
public class SyntaxicalAnalyser {
	ArrayList<Instruction> instructions;
	
	int currentInstructionIndex;
	Instruction currentInstruction;
	
	int currentSymbolIndex;
	String currentSymbol;
	
	private static ArrayList<String> operators;
	
	static {
		operators = new ArrayList<String>();
		String[] ops = {"+", "-", "*", "/", "&&", "||",
				"<", ">", "<=", ">=", "=", "!="};
		
		for(String op: ops) {
			operators.add(op);
		}
	}
	
	public SyntaxicalAnalyser(ArrayList<Instruction> instructions) throws CompilerException {
		this.instructions = instructions;
		currentInstructionIndex = -1;
		
		while(nextInstruction()) {
			parseExpression();
		}
	}
	
	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}
	
	private boolean isAtEndOfInstructions() {
		return currentInstructionIndex >= instructions.size();
	}
	
	private boolean nextInstruction() {
		currentInstructionIndex++;
		
		if(isAtEndOfInstructions()) {
			return false;
		}
		
		currentInstruction = instructions.get(currentInstructionIndex);
		
		currentSymbolIndex = -1;
		
		if(!nextSymbol()) {
			return nextInstruction();
		}
		
		return true;
	}
	
	private boolean isAtEndOfSymbols() {
		return currentSymbolIndex >= currentInstruction.getInstruction().size();
	}
	
	private void registerSymbol() {
		currentSymbol = currentInstruction.getInstruction().get(currentSymbolIndex);
	}
	
	private void setCurrentSymbolIndex(int index) {
		currentSymbolIndex = index;
		registerSymbol();
	}
	
	private boolean nextSymbol() {
		currentSymbolIndex++;
		
		if(isAtEndOfSymbols()) {
			return false;
		}
		
		registerSymbol();
		
		return true;
	}
	
	private boolean previousSymbol() {
		if(currentSymbolIndex <= 0) {
			currentSymbolIndex = 0;
			return false;
		}
		
		currentSymbolIndex--;
		
		registerSymbol();
		
		return true;
	}
	
	private void parseExpression() throws CompilerException {
		boolean isDefinition = false;
		
		do {
			if(currentSymbol.equals(":=")) {
				isDefinition = true;
				break;
			}
		} while(nextSymbol() && !isDefinition);
		
		ArrayList<String> expr;
		
		if(isDefinition) {
			expr = parseDefinition();
		}
		else {
			setCurrentSymbolIndex(0);
			expr = parseEvaluable();
		}
		
		if(currentSymbol.equals(";;")) {
			expr.add(0, "debug");
			nextSymbol();
		}
		
		if(!isAtEndOfSymbols() && !currentSymbol.equals("#")) {
			error("Symbole inattendu à la fin de l'expression: " + currentSymbol);
		}
	}
	
	private ArrayList<String> parseDefinition() throws CompilerException {
		Stack<String> definitionHead = new Stack<String>();
		
		ArrayList<String> result = new ArrayList<String>();
		result.add(":=");
		
		int defSymbolIndex = currentSymbolIndex;
		
		while(previousSymbol()) {
			if(!isName()) {
				error("Les symboles à gauche de := doivent être des noms de combinateur, et pas " + currentSymbol);
			}
			
			definitionHead.push(currentSymbol);
		}
		
		if(definitionHead.isEmpty()) {
			error("Il faut au moins le nom du combinateur à définir à gauche de :=");
		}
		
		String funcName = definitionHead.pop();
		result.add("$" + funcName);
		result.add("(");

		
		setCurrentSymbolIndex(defSymbolIndex);
		
		if(!nextSymbol()) {
			error("Une expression doit se trouver à droite de :=");
		}
		
		if(!definitionHead.isEmpty()) {
			ArrayList<String> varNames = new ArrayList<String>();
			
			for(String var: definitionHead) {
				result.add("lambda");
				result.add("$" + var);
				varNames.add(var);
			}
			
			ArrayList<String> expr = new ArrayList<String>();
			
			for(String symbol: parseEvaluable()) {
				if(symbol.equals(funcName)) {
					expr.add("@" + symbol);
				}
				else {
					boolean wasVarName = false;
					
					for(String varName: varNames) {
						if(symbol.equals(varName)) {
							expr.add("$" + symbol);
							wasVarName = true;
							break;
						}
					}
					
					if(!wasVarName) {
						expr.add(symbol);
					}
				}
			}
			
			result.addAll(expr);
		}
		else {
			ArrayList<String> expr = new ArrayList<String>();
			
			for(String symbol: parseEvaluable()) {
				if(symbol.equals(funcName)) {
					expr.add("@" + symbol);
				}
				else {
					expr.add(symbol);
				}
			}
			
			result.addAll(expr);
		}
		
		result.add(")");
		
		return result;
	}
	
	private boolean isName() {
		Pattern p = Pattern.compile("^[a-zA-Z_][a-zA-Z_0-9]*$");
		Matcher m = p.matcher(currentSymbol);
		
		return m.find();
	}
	
	private ArrayList<String> parseEvaluable() throws CompilerException {
		ArrayList<String> result = new ArrayList<String>();
		
		Stack<Integer> operandsPosition = new Stack<Integer>(); 
		
		do {
			operandsPosition.push(result.size());
			
			if(currentSymbol.equals("(")) {
				result.addAll(parseParenthesis());
			}
			else if(currentSymbol.equals("[")) {
				result.addAll(parseVector());
			}
			else if(isEvaluableSeparator()) {
				break;
			}
			else if(currentSymbol.equals("if")) {
				result.addAll(parseCondition());
			}
			else if(currentSymbol.equals("lambda")) {
					result.addAll(parseLambda());
				}
			else if(isName() || currentSymbol.equals("!")) {
				result.add(currentSymbol);
			}
			else if(isOperator()) {
				operandsPosition.pop();
				
				if(operandsPosition.isEmpty()) {
					error("Opérateur " + currentSymbolIndex + "n'a pas d'opérande gauche");
				}
				
				result.add(operandsPosition.peek(), currentSymbol);
			}
			
		} while(nextSymbol());
		
		return result;
	}
	
	private boolean isEvaluableSeparator() {
		return currentSymbol.equals("#") ||
				currentSymbol.equals(")") ||
				currentSymbol.equals("]") ||
				currentSymbol.equals(",") ||
				currentSymbol.equals("then") ||
				currentSymbol.equals("else") ||
				currentSymbol.charAt(0) == ';';
	}
	
	private ArrayList<String> parseParenthesis() throws CompilerException {
		if(!nextSymbol()) {
			error("'(' à la fin de l'instruction");
		}
		
		ArrayList<String> result = new ArrayList<String>();
		result.add("(");
		result.addAll(parseEvaluable());
		result.add(")");
		
		if(isAtEndOfSymbols() || !currentSymbol.equals(")")) {
			error("Parenthèses non fermées");
		}
		
		return result;
	}
	
	private ArrayList<String> parseVector() throws CompilerException {
		ArrayList<String> result = new ArrayList<String>();
		
		Stack<ArrayList<String>> elements = new Stack<ArrayList<String>>();
		
		while(!isAtEndOfSymbols() && !currentSymbol.equals("]")) {
			elements.add(parseEvaluable());
			
			if(currentSymbol.equals(",")) {
				nextSymbol();
			}
		}
		
		if(!currentSymbol.equals("]")) {
			error("Vecteur non terminé, il faut ajouter ']'");
		}
		
		for(ArrayList<String> element: elements) {
			if(result.isEmpty()) {
				result.addAll(element);
			}
			else {
				result.addAll(0, element);
				result.add(0, "vec");
				result.add(0, "(");
				result.add(")");
			}
		}
		
		return result;
	}
	
	private ArrayList<String> parseLambda() throws CompilerException {
		ArrayList<String> result = new ArrayList<String>();
		
		String lambdaName = "lambda";
		
		if(!nextSymbol()) {
			error("'lambda' ne peut être à la fin de l'instruction");
		}
		
		if(currentSymbol.charAt(0) == '+') {
			lambdaName += currentSymbol;
			
			if(!nextSymbol()) {
				error("'" + lambdaName + "' ne peut être à la fin de l'instruction");
			}
		}
		
		ArrayList<String> varNames = new ArrayList<String>();
		
		do {
			if(currentSymbol.equals(",")) {
				continue;
			}
			
			if(!isName()) {
				error("'" + currentSymbol + "' n'est pas un nom de variable valide pour une abstraction");
			}
			
			varNames.add(currentSymbol);
		} while(nextSymbol() && !currentSymbol.equals("."));
		
		if(!currentSymbol.equals(".")) {
			error("Expression d'abstraction mal formée, il manque '. <expression à abstraire>");
		}
		
		if(!nextSymbol()) {
			error("Expression d'abstraction mal formée, il manque l'expression à abstraire après le point");
		}
		
		ArrayList<String> expr = new ArrayList<String>();
		
		for(String symbol: parseEvaluable()) {
			boolean wasVar = false;
			
			for(String varName: varNames) {
				if(symbol.equals(varName)) {
					expr.add("$" + symbol);
					wasVar = true;
					break;
				}
			}
			
			if(!wasVar) {
				expr.add(symbol);
			}
		}
		
		result.add("(");
		
		for(String varName: varNames) {
			result.add(lambdaName);
			result.add("$" + varName);
		}
		
		result.addAll(expr);
		
		result.add(")");
		
		return result;
	}
	
	private ArrayList<String> parseCondition() throws CompilerException {
		ArrayList<String> result = new ArrayList<String>();
		
		if(!nextSymbol()) {
			error("Un 'if' doit être suivi d'une condition, d'une clause then, puis d'une clause else");
		}
		
		ArrayList<String> condition = parseEvaluable();
		
		result.add("(");
		result.addAll(condition);
		result.add(")");
		
		if(!currentSymbol.equals("then")) {
			error("La condition d'un 'if' doit être suivie d'une clause then");
		}
		
		ArrayList<String> thenClause = parseEvaluable();
		
		result.add("(");
		result.addAll(thenClause);
		result.add("(");
		
		if(!currentSymbol.equals("else")) {
			error("La clause then d'un if doit être suivie d'une clause else");
		}
		
		ArrayList<String> elseClause = parseEvaluable();
		
		result.add("(");
		result.addAll(elseClause);
		result.add(")");
		
		return result;
	}
	
	private void error(String msg) throws CompilerException {
		throw new CompilerException(
				msg,
				currentInstruction.getLine(),
				currentInstruction.getPosition());
	}
	
	private boolean isOperator() {
		for(String op: operators) {
			if(op.equals(currentSymbol)) {
				return true;
			}
		}
		return false;
	}

}
