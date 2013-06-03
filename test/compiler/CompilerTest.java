package compiler;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class CompilerTest {

	@Test
	public void testReducesOneStepCorrectly() {
		StringReader input = new StringReader("S X Y Z");
		
		Compiler comp = new Compiler(input);
		
		assertTrue(comp.reduceStep());
		assertFalse(comp.reduceStep());
		
		assertTrue(comp.isFinished());
		assertEquals("X Z ( Y Z )", comp.getResult());
	}
	
	@Test
	public void testReducesAllCorrectly() {
		StringReader input = new StringReader("S K K K");
		
		Compiler comp = new Compiler(input);
		
		comp.reduceAll();
		
		assertTrue(comp.isFinished());
		assertEquals("K", comp.getResult());
	}
}
