package compiler.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import compiler.CompilerException;

public class SyntaxicalAnalyserTest {

	@Test
	public void testSimpleExpression() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction("b");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(2, symbols.size());
		assertEquals("a", symbols.get(0));
		assertEquals("b", symbols.get(1));
	}
	
	@Test
	public void testParenthesizedExpression() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction("(");
		ins.addInstruction("b");
		ins.addInstruction("c");
		ins.addInstruction(")");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(5, symbols.size());
		assertEquals("a", symbols.get(0));
		assertEquals("(", symbols.get(1));
		assertEquals("b", symbols.get(2));
		assertEquals("c", symbols.get(3));
		assertEquals(")", symbols.get(4));
	}
	
	@Test(expected=CompilerException.class)
	public void testUnclosedParenthesizedExpression() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction("(");
		ins.addInstruction("b");
		ins.addInstruction("c");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		new SyntaxicalAnalyser(instructions);
	}
	
	@Test(expected=CompilerException.class)
	public void testTooMuchclosedParenthesizedExpression() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction(")");
		ins.addInstruction("b");
		ins.addInstruction("c");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		new SyntaxicalAnalyser(instructions);
	}
	
	@Test
	public void testDoubleParnethesisWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction("(");
		ins.addInstruction("b");
		ins.addInstruction("(");
		ins.addInstruction("c");
		ins.addInstruction("d");
		ins.addInstruction(")");
		ins.addInstruction(")");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(8, symbols.size());
		assertEquals("a", symbols.get(0));
		assertEquals("(", symbols.get(1));
		assertEquals("b", symbols.get(2));
		assertEquals("(", symbols.get(3));
		assertEquals("c", symbols.get(4));
		assertEquals("d", symbols.get(5));
		assertEquals(")", symbols.get(6));
		assertEquals(")", symbols.get(7));
	}
	
	@Test
	public void testCommentWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("a");
		ins.addInstruction("b");
		ins.addInstruction("#");
		ins.addInstruction("(");
		ins.addInstruction("c");
		ins.addInstruction("d");
		ins.addInstruction(")");
		ins.addInstruction(")");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(2, symbols.size());
		assertEquals("a", symbols.get(0));
		assertEquals("b", symbols.get(1));
	}
	
	@Test
	public void testCommentLineErasesInstruction() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("#");
		ins.addInstruction("(");
		ins.addInstruction("c");
		ins.addInstruction("d");
		ins.addInstruction(")");
		ins.addInstruction(")");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		
		assertEquals(0, instructions.size());
	}
	
	@Test
	public void testOperatorWorkds() throws CompilerException {
		String[] ops = {"+", "-", "*", "/", "&&", "||",
				"<", ">", "<=", ">=", "=", "!="};
		
		for(String op: ops) {
			Instruction ins = new Instruction();
			ins.addInstruction("a");
			ins.addInstruction(op);
			ins.addInstruction("b");
			
			ArrayList<Instruction> instructions = new ArrayList<Instruction>();
			instructions.add(ins);
			
			SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
			
			instructions = sa.getInstructions();
			ins = instructions.get(0);
			
			ArrayList<String> symbols = ins.getInstruction();
			
			assertEquals(7, symbols.size());
            assertEquals("(", symbols.get(0));
            assertEquals(op, symbols.get(1));
            assertEquals("(", symbols.get(2));
			assertEquals("a", symbols.get(3));
            assertEquals(")", symbols.get(4));
			assertEquals("b", symbols.get(5));
            assertEquals(")", symbols.get(6));
		}
		
		for(String op: ops) {
			Instruction ins = new Instruction();
			ins.addInstruction("(");
			ins.addInstruction("a");
			ins.addInstruction("b");
			ins.addInstruction(")");
			ins.addInstruction(op);
			ins.addInstruction("c");
			
			ArrayList<Instruction> instructions = new ArrayList<Instruction>();
			instructions.add(ins);
			
			SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
			
			instructions = sa.getInstructions();
			ins = instructions.get(0);
			
			ArrayList<String> symbols = ins.getInstruction();
			
			assertEquals(10, symbols.size());
            assertEquals("(", symbols.get(0));
            assertEquals(op, symbols.get(1));
			assertEquals("(", symbols.get(2));
			assertEquals("(", symbols.get(3));
		}
	}
	
	@Test(expected=CompilerException.class)
	public void testStartingOperatorThrowsException() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("+");
		ins.addInstruction("a");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		new SyntaxicalAnalyser(instructions);
	}
	
	@Test
	public void testNotWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("!");
		ins.addInstruction("a");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(2, symbols.size());
		assertEquals("!", symbols.get(0));
		assertEquals("a", symbols.get(1));
	}
	
	@Test
	public void testVectorWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("[");
		ins.addInstruction("a");
		ins.addInstruction(",");
		ins.addInstruction("b");
		ins.addInstruction(",");
		ins.addInstruction("c");
		ins.addInstruction("]");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(9, symbols.size());
		assertEquals("(", symbols.get(0));
		assertEquals("vec", symbols.get(1));
		assertEquals("a", symbols.get(2));
		assertEquals("(", symbols.get(3));
		assertEquals("vec", symbols.get(4));
		assertEquals("b", symbols.get(5));
		assertEquals("c", symbols.get(6));
		assertEquals(")", symbols.get(7));
		assertEquals(")", symbols.get(8));
	}
	
	@Test
	public void testLambdaWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("lambda");
		ins.addInstruction("++");
		ins.addInstruction("x");
		ins.addInstruction(".");
		ins.addInstruction("[");
		ins.addInstruction("a");
		ins.addInstruction(",");
		ins.addInstruction("x");
		ins.addInstruction("]");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(11, symbols.size());
        assertEquals("I", symbols.get(0));
        assertEquals("(", symbols.get(1));
		assertEquals("lambda++", symbols.get(2));
        assertEquals("$x", symbols.get(3));
        assertEquals("I", symbols.get(4));
		assertEquals("(", symbols.get(5));
		assertEquals("vec", symbols.get(6));
		assertEquals("a", symbols.get(7));
		assertEquals("$x", symbols.get(8));
		assertEquals(")", symbols.get(9));
		assertEquals(")", symbols.get(10));
	}
	
	@Test
	public void testConditionWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("if");
		ins.addInstruction("x");
		ins.addInstruction("=");
		ins.addInstruction("0");
		ins.addInstruction("then");
		ins.addInstruction("a");
		ins.addInstruction("b");
		ins.addInstruction("else");
		ins.addInstruction("c");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(12, symbols.size());
        assertEquals("(", symbols.get(0));
		assertEquals("=", symbols.get(1));
        assertEquals("(", symbols.get(2));
        assertEquals("x", symbols.get(3));
        assertEquals(")", symbols.get(4));
		assertEquals("0", symbols.get(5));
        assertEquals(")", symbols.get(6));
		assertEquals("(", symbols.get(7));
		assertEquals("a", symbols.get(8));
		assertEquals("b", symbols.get(9));
		assertEquals(")", symbols.get(10));
		assertEquals("c", symbols.get(11));
	}
	
	@Test
	public void testNumberWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("25");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(1, symbols.size());
		assertEquals("25", symbols.get(0));
	}
	
	@Test
	public void testBasicDefinitionWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("f");
		ins.addInstruction(":=");
		ins.addInstruction("a");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(3, symbols.size());
		assertEquals(":=", symbols.get(0));
		assertEquals("$f", symbols.get(1));
		assertEquals("a", symbols.get(2));
	}
	
	@Test
	public void testDefinitionWithVarAndBackRefWorks() throws CompilerException {
		Instruction ins = new Instruction();
		ins.addInstruction("f");
		ins.addInstruction("x");
		ins.addInstruction("y");
		ins.addInstruction(":=");
		ins.addInstruction("f");
		ins.addInstruction("y");
		ins.addInstruction("x");
		
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		instructions.add(ins);
		
		SyntaxicalAnalyser sa = new SyntaxicalAnalyser(instructions);
		
		instructions = sa.getInstructions();
		ins = instructions.get(0);
		
		ArrayList<String> symbols = ins.getInstruction();
		
		assertEquals(18, symbols.size());
		assertEquals(":=", symbols.get(0));
        assertEquals("$f", symbols.get(1));
        assertEquals("I", symbols.get(2));
		assertEquals("(", symbols.get(3));
		assertEquals("lambda", symbols.get(4));
        assertEquals("$x", symbols.get(5));
        assertEquals("I", symbols.get(6));
        assertEquals("(", symbols.get(7));
        assertEquals("lambda", symbols.get(8));
		assertEquals("$y", symbols.get(9));
        assertEquals("I", symbols.get(10));
        assertEquals("(", symbols.get(11));
		assertEquals("@f", symbols.get(12));
		assertEquals("$y", symbols.get(13));
		assertEquals("$x", symbols.get(14));
        assertEquals(")", symbols.get(15));
        assertEquals(")", symbols.get(16));
        assertEquals(")", symbols.get(17));
	}

}
