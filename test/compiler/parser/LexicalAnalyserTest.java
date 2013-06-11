package compiler.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import compiler.CompilerException;

public class LexicalAnalyserTest {

	@Test
	public void testSimpleExpression() throws CompilerException {
		StringReader input = new StringReader("A B");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(1, instructions.size());
		
		ArrayList<String> symbols = instructions.get(0).getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
	}
	
	@Test
	public void testParenthesis() throws CompilerException {
		StringReader input = new StringReader("A (B C)");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(1, instructions.size());
		
		ArrayList<String> symbols = instructions.get(0).getInstruction();
		assertEquals(5, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("(", symbols.get(1));
		assertEquals("B", symbols.get(2));
		assertEquals("C", symbols.get(3));
		assertEquals(")", symbols.get(4));
	}
}
