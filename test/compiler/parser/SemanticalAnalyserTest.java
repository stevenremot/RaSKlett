package compiler.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import compiler.CompilerException;

public class SemanticalAnalyserTest {

	@Test
	public void testGoodParenthesisWorks() throws CompilerException {
		Instruction i = new Instruction();
		i.addInstruction("(");
		i.addInstruction("A");
		i.addInstruction(")");
		
		ArrayList<Instruction> symbols = new ArrayList<Instruction>();
		symbols.add(i);
		
		SemanticalAnalyser sem = new SemanticalAnalyser(symbols);
		
		symbols = sem.getCombinators();
		
		ArrayList<String> combinators = symbols.get(0).getInstruction();
		
		assertEquals(3, combinators.size());
		assertEquals("(", combinators.get(0));
		assertEquals("A", combinators.get(1));
		assertEquals(")", combinators.get(2));
	}
	
	@Test(expected=CompilerException.class)
	public void testFewParenthesisWorks() throws CompilerException {
		Instruction i = new Instruction();
		i.addInstruction("(");
		i.addInstruction("A");
		
		ArrayList<Instruction> symbols = new ArrayList<Instruction>();
		symbols.add(i);
		
		new SemanticalAnalyser(symbols);
	}
	
	@Test(expected=CompilerException.class)
	public void testClosingParenthesisWorks() throws CompilerException {
		Instruction i = new Instruction();
		i.addInstruction(")");
		i.addInstruction("A");
		
		ArrayList<Instruction> symbols = new ArrayList<Instruction>();
		symbols.add(i);
		
		new SemanticalAnalyser(symbols);
	}
	
	@Test(expected=CompilerException.class)
	public void testEmptyParenthesisWorks() throws CompilerException {
		Instruction i = new Instruction();
		i.addInstruction("(");
		i.addInstruction(")");
		
		ArrayList<Instruction> symbols = new ArrayList<Instruction>();
		symbols.add(i);
		
		new SemanticalAnalyser(symbols);
	}

}
