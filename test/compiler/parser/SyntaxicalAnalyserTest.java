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

}
