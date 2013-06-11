package compiler.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TokenizerTest {
	
	public void testAToken(String symbol, Tokenizer.Token token) {
		Instruction ins = new Instruction();
		ins.addInstruction(symbol);

		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);

		Tokenizer t = new Tokenizer(instructions);

		ArrayList<ArrayList<Tokenizer.Token>> tokens = t.getTokens();

		assertEquals(1, tokens.size());
		assertEquals(1, tokens.get(0).size());
		assertEquals(token, tokens.get(0).get(0));
	}

	@Test
	public void testIdentifier() {
		testAToken("blablou", Tokenizer.Token.IDENTIFIER);
		testAToken("65.2", Tokenizer.Token.IDENTIFIER);
	}
	
	@Test
	public void testOperator() {
		String[] operators = {"+", "-", ":=", "*", "/"};
		
		for(String symbol : operators) {
			testAToken(symbol, Tokenizer.Token.OPERATOR);
		}
	}
	
	@Test
	public void testParenthesisAndBraces() {
		testAToken("(", Tokenizer.Token.OPEN_PARENTHESIS);
		testAToken(")", Tokenizer.Token.CLOSE_PARENTHESIS);
		testAToken("[", Tokenizer.Token.OPEN_BRACKET);
		testAToken("]", Tokenizer.Token.CLOSE_BRACKET);
	}
	
	@Test
	public void testEndInstruction() {
		testAToken(";", Tokenizer.Token.INSTRUCTION_END);
		testAToken(";;", Tokenizer.Token.INSTRUCTION_END);
		testAToken(";;;", Tokenizer.Token.IDENTIFIER);
	}
	
	@Test
	public void testMultipleSymbols() {
		Instruction ins1 = new Instruction();
		ins1.addInstruction("A");
		ins1.addInstruction(";");
		
		Instruction ins2 = new Instruction();
		ins2.addInstruction("C");

		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins1);
		instructions.add(ins2);

		Tokenizer t = new Tokenizer(instructions);

		ArrayList<ArrayList<Tokenizer.Token>> tokens = t.getTokens();

		assertEquals(2, tokens.size());
		assertEquals(2, tokens.get(0).size());
		assertEquals(Tokenizer.Token.IDENTIFIER, tokens.get(0).get(0));
		assertEquals(Tokenizer.Token.INSTRUCTION_END, tokens.get(0).get(1));
		
		assertEquals(1, tokens.get(1).size());
		assertEquals(Tokenizer.Token.IDENTIFIER, tokens.get(1).get(0));
	}

}
