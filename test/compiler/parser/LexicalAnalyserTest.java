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
	public void testLines() throws CompilerException {
		StringReader input = new StringReader("A B;\nC D");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(2, instructions.size());
		
		Instruction ins = instructions.get(0);
		
		assertEquals(0, ins.getLine());
		assertEquals(0, ins.getPosition());
		
		ArrayList<String> symbols = ins.getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
		
		ins = instructions.get(1);
		
		assertEquals(1, ins.getLine());
		assertEquals(0, ins.getPosition());
		
		symbols = ins.getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("C", symbols.get(0));
		assertEquals("D", symbols.get(1));
	}
	
	@Test
	public void testSemiColons() throws CompilerException {
		StringReader input = new StringReader("A B; C D");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(2, instructions.size());
		
		Instruction ins = instructions.get(0);
		
		assertEquals(0, ins.getLine());
		assertEquals(0, ins.getPosition());
		
		ArrayList<String> symbols = ins.getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
		
		ins = instructions.get(1);
		
		assertEquals(0, ins.getLine());
		assertEquals(1, ins.getPosition());
		
		symbols = ins.getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("C", symbols.get(0));
		assertEquals("D", symbols.get(1));
	}
	
	@Test
	public void testMultiLineExpression() throws CompilerException {
		StringReader input = new StringReader("A B C\n  D;");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(1, instructions.size());
		
		ArrayList<String> symbols = instructions.get(0).getInstruction();
		assertEquals(4, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
		assertEquals("C", symbols.get(2));
		assertEquals("D", symbols.get(3));
	}
	
	@Test
	public void testDoubleSemiColon() throws CompilerException {
		StringReader input = new StringReader("A B;; C D");
		
		LexicalAnalyser lex = new LexicalAnalyser(input);
		
		ArrayList<Instruction> instructions = lex.getSymbols();
		
		assertEquals(2, instructions.size());
		
		Instruction ins = instructions.get(0);
		
		assertEquals(0, ins.getLine());
		assertEquals(0, ins.getPosition());
		
		ArrayList<String> symbols = ins.getInstruction();
		assertEquals(3, symbols.size());
		assertEquals("A", symbols.get(0));
		assertEquals("B", symbols.get(1));
		assertEquals(";;", symbols.get(2));
		
		ins = instructions.get(1);
		
		assertEquals(0, ins.getLine());
		assertEquals(1, ins.getPosition());
		
		symbols = ins.getInstruction();
		assertEquals(2, symbols.size());
		assertEquals("C", symbols.get(0));
		assertEquals("D", symbols.get(1));
	}
}
