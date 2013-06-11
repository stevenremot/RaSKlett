package compiler.parser;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import compiler.parser.LexicalAnalyser;


public class lexicalAnalyserTest {

	@Test
	public void testSimpleExpression() {
		StringReader input = new StringReader("A B");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(1, instructions.size());
		
		Instruction i = instructions.get(0);
		assertEquals(0, i.getLine());
		assertEquals(0, i.getPosition());
		
		ArrayList<String> symbols = i.getInstruction();
		
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
	}
	
	@Test
	public void testDoubleInstruction() {
		StringReader input = new StringReader("A ; B");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(2, instructions.size());
		
		Instruction i = instructions.get(0);
		assertEquals(0, i.getLine());
		assertEquals(0, i.getPosition());
		
		ArrayList<String> symbols = i.getInstruction();
		
		assertEquals("A", symbols.get(0));
		assertEquals(";", symbols.get(1));
		
		i = instructions.get(1);
		assertEquals(0, i.getLine());
		assertEquals(1, i.getInstruction());
		
		symbols = i.getInstruction();
		
		assertEquals("B", symbols.get(0));
	}
	
	@Test
	public void testLine() {
		StringReader input = new StringReader("A ;\nB");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(2, instructions.size());
		
		Instruction i = instructions.get(0);
		assertEquals(0, i.getLine());
		assertEquals(0, i.getPosition());
		
		ArrayList<String> symbols = i.getInstruction();
		
		assertEquals("A", symbols.get(0));
		assertEquals(";", symbols.get(1));
		
		i = instructions.get(1);
		
		assertEquals(1, i.getLine());
		assertEquals(0, i.getInstruction());
		
		symbols = i.getInstruction();
		
		assertEquals("B", symbols.get(0));
	}
	
	@Test
	public void testParenthesis() {
		StringReader input = new StringReader("A (B C)");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(1, instructions.size());
		
		Instruction i = instructions.get(0);
		assertEquals(0, i.getLine());
		assertEquals(0, i.getPosition());
		
		ArrayList<String> symbols = i.getInstruction();
		
		assertEquals("A", symbols.get(0));
		assertEquals("(", symbols.get(1));
		assertEquals("B", symbols.get(2));
		assertEquals("C", symbols.get(3));
		assertEquals(")", symbols.get(4));
	}
	
	@Test
	public void testOperators() {
		String[] operators = {"+", "-", ":=", "*", "/"};
		
		for(String op : operators) {
			StringReader input = new StringReader("A" + op + "B");

			LexicalAnalyser lex = new LexicalAnalyser(input);
			ArrayList<Instruction> instructions = lex.getSymbols();

			assertEquals(1, instructions.size());

			Instruction i = instructions.get(0);
			assertEquals(0, i.getLine());
			assertEquals(0, i.getPosition());

			ArrayList<String> symbols = i.getInstruction();

			assertEquals("A", symbols.get(0));
			assertEquals(op, symbols.get(1));
			assertEquals("B", symbols.get(2));
		}
	}

}
