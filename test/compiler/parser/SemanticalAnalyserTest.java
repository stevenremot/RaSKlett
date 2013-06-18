package compiler.parser;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class SemanticalAnalyserTest {

	@Test
	public void testBasicInstructionWorks() {
		Instruction i = new Instruction();
		i.addInstruction("(");
		i.addInstruction("A");
		i.addInstruction(")");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(3, result.size());
		assertEquals("(", result.get(0));
		assertEquals("A", result.get(1));
		assertEquals(")", result.get(2));
	}
	
	@Test
	public void testStrangerNameWorks() {
		Instruction i = new Instruction();
		i.addInstruction("_");
		i.addInstruction("a");
		i.addInstruction("a");
		i.addInstruction("0");
		i.addInstruction("a");
		i.addInstruction("_");
		i.addInstruction("a");
		i.addInstruction("J");
		i.addInstruction("a");
		i.addInstruction("a");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(1, result.size());
		assertEquals("_aa0a_aJaa", result.get(0));
	}
	
	@Test
	public void testAssigmentWorks() {
		Instruction i = new Instruction();
		i.addInstruction("A");
		i.addInstruction(":");
		i.addInstruction("=");
		i.addInstruction("B");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(3, result.size());
		assertEquals("A", result.get(0));
		assertEquals(":=", result.get(1));
		assertEquals("B", result.get(2));
	}
	
	@Test
	public void testSpacedExpressionsWork() {
		Instruction i = new Instruction();
		i.addInstruction("A");
		i.addInstruction(" ");
		i.addInstruction("B");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(2, result.size());
		assertEquals("A", result.get(0));
		assertEquals("B", result.get(1));
	}
	
	@Test
	public void testDoubleSemiColonWork() {
		Instruction i = new Instruction();
		i.addInstruction("A");
		i.addInstruction(" ");
		i.addInstruction("B");
		i.addInstruction(";");
		i.addInstruction(";");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(3, result.size());
		assertEquals("A", result.get(0));
		assertEquals("B", result.get(1));
		assertEquals(";;", result.get(2));
	}

	@Test
	public void testLambdaWork() {
		Instruction i = new Instruction();
		i.addInstruction("l");
		i.addInstruction("a");
		i.addInstruction("m");
		i.addInstruction("b");
		i.addInstruction("d");
		i.addInstruction("a");
		i.addInstruction("+");
		i.addInstruction("+");
		i.addInstruction("x");
		i.addInstruction(".");
		i.addInstruction("A");
		
		SemanticalAnalyser sem = new SemanticalAnalyser(i);
		
		sem.semanticAnalysis();
		
		LinkedList<String> result = sem.myresult;
		
		assertEquals(5, result.size());
		assertEquals("lambda", result.get(0));
		assertEquals("++", result.get(1));
		assertEquals("x", result.get(2));
		assertEquals(".", result.get(3));
		assertEquals("A", result.get(4));
	}

}
